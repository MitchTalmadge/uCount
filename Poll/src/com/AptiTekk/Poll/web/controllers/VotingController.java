package com.AptiTekk.Poll.web.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.AptiTekk.Poll.core.ContestantService;
import com.AptiTekk.Poll.core.CredentialService;
import com.AptiTekk.Poll.core.EntryService;
import com.AptiTekk.Poll.core.PollService;
import com.AptiTekk.Poll.core.VoteGroupService;
import com.AptiTekk.Poll.core.entityBeans.Credential;
import com.AptiTekk.Poll.core.entityBeans.Entry;
import com.AptiTekk.Poll.core.entityBeans.Poll;
import com.AptiTekk.Poll.core.entityBeans.VoteGroup;
import com.AptiTekk.Poll.core.utilities.BanHelper;
import com.AptiTekk.Poll.core.utilities.PollLogger;
import com.AptiTekk.Poll.core.utilities.PropertiesHelper;
import com.AptiTekk.Poll.core.utilities.StudentIDAuthenticator;
import com.AptiTekk.Poll.core.utilities.StudentIDAuthenticator.AuthenticationException;

@ManagedBean
@ViewScoped
public class VotingController {

	@EJB
	PollService pollService;

	@EJB
	VoteGroupService voteGroupService;

	@EJB
	ContestantService contestantService;

	@EJB
	EntryService entryService;

	@EJB
	CredentialService credentialService;

	@EJB
	StudentIDAuthenticator studentIdAuthenticator;

	private static String AUTH_METHOD = "Basic";

	static {
		String authMethod = PropertiesHelper.getApplicationProperty("authentication_method");
		if (authMethod != null
				&& (authMethod.equals("JSD") || authMethod.equals("Basic") || authMethod.equals("Table"))) {
			PollLogger.logVerbose("Authentication Method has been set to " + authMethod);
			AUTH_METHOD = authMethod;
		}
	}

	/**
	 * The student's ID. Will be -1 if no student ID is specified.
	 */
	private int studentId = -1;

	/**
	 * The input string when the student types in their ID.
	 */
	private String studentIdInput;

	/**
	 * Will be set to true if an entry is found with the student's ID.
	 */
	private boolean studentHasAlreadyVoted = false;

	/**
	 * Will be set to true upon voting completion. Used to display thank-you
	 * page.
	 */
	private boolean votingComplete = false;

	private final static String BAN_NAME = "Vote";
	private final static int MAX_FAILED_COUNT = 4;
	private final static int BAN_LENGTH_HOURS = 6;

	/**
	 * True if the user has entered too many invalid ids and was banned.
	 */
	private boolean isBanned = BanHelper.isUserBanned(BAN_NAME);

	/**
	 * The enabled poll, cached for the life of the view.
	 */
	private Poll enabledPoll;

	@PostConstruct
	public void init() {
		this.enabledPoll = pollService.getEnabledPoll();

		if (enabledPoll != null) {
			Collections.shuffle(enabledPoll.getVoteGroups()); // Randomize the
																// order the
																// vote groups
																// are shown
		}
	}

	public Poll getEnabledPoll() {
		return enabledPoll;
	}

	public void authenticate() {
		PollLogger.logVerbose("Authenticating...");
		setStudentId(-1);

		if (studentIdInput != null && !studentIdInput.isEmpty()) {
			try {
				int studentId = Integer.parseInt(studentIdInput);

				if (AUTH_METHOD.equals("JSD"))
					studentIdAuthenticator.authenticateIdUsingOverdrive(studentId);
				else if (AUTH_METHOD.equals("Basic"))
					studentIdAuthenticator.authenticateIdUsingBasicVerification(studentId);
				else if (AUTH_METHOD.equals("Table"))
					studentIdAuthenticator.authenticateIdUsingCredentialsTable(studentId);

				BanHelper.clearFailedAttempts(BAN_NAME);
				BanHelper.unBanUser(BAN_NAME);

				setStudentId(studentId);
			} catch (NumberFormatException e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Please only enter numbers."));
			} catch (AuthenticationException e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));

				BanHelper.recordFailedAttempt(BAN_NAME);
				if (BanHelper.getNumberFailedAttempts(BAN_NAME) >= MAX_FAILED_COUNT) {
					BanHelper.banUser(BAN_NAME, BAN_LENGTH_HOURS);
					isBanned = true;
				}
			}
		} else {
			PollLogger.logVerbose("Input was empty!");
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("You must enter your Student ID to continue."));
		}
		studentIdInput = null; // Clear input

	}

	public String getStudentIdInput() {
		return studentIdInput;
	}

	public void setStudentIdInput(String studentIdInput) {
		PollLogger.logVerbose("Setting Student ID Input to " + studentIdInput);
		this.studentIdInput = studentIdInput;
	}

	public boolean getStudentHasAlreadyVoted() {
		return studentHasAlreadyVoted;
	}

	public void setStudentHasAlreadyVoted(boolean studentHasAlreadyVoted) {
		this.studentHasAlreadyVoted = studentHasAlreadyVoted;
	}

	public boolean isVotingComplete() {
		return votingComplete;
	}

	public void setVotingComplete(boolean votingComplete) {
		this.votingComplete = votingComplete;
	}

	public void recordVote(VoteGroup voteGroup) {
		if (entryService.hasStudentVoted(studentId, enabledPoll.getId()))
			setStudentHasAlreadyVoted(true);
		else {
			Entry entry = new Entry(studentId, voteGroup, enabledPoll);
			entryService.insert(entry);
			setVotingComplete(true);
		}
	}

	public boolean getBanned() {
		return isBanned;
	}

	public void setBanned(boolean isBanned) {
		this.isBanned = isBanned;
	}

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;

		if (studentId > -1) {
			this.setStudentHasAlreadyVoted(entryService.hasStudentVoted(studentId, enabledPoll.getId()));
		}
	}

}
