package com.AptiTekk.Poll.web.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.GenerationType;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import com.AptiTekk.Poll.core.ContestantService;
import com.AptiTekk.Poll.core.CredentialService;
import com.AptiTekk.Poll.core.EntryService;
import com.AptiTekk.Poll.core.PollService;
import com.AptiTekk.Poll.core.VoteGroupService;
import com.AptiTekk.Poll.core.entityBeans.Credential;
import com.AptiTekk.Poll.core.entityBeans.Entry;
import com.AptiTekk.Poll.core.entityBeans.Poll;
import com.AptiTekk.Poll.core.entityBeans.VoteGroup;

@ManagedBean
@ViewScoped
public class VotingController {

	public static final boolean USE_JSD_AUTH = true;

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

	/**
	 * The student's credential. Null if the user has not yet validated their
	 * student ID.
	 */
	private Credential credential;

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
	
	private List<VoteGroup> votingOptions;

	@PostConstruct
	public void init() {
		this.setCredential(null);
		
		votingOptions = voteGroupService.getVoteGroupsFromPoll(getEnabledPoll());
		System.out.println("Found " + votingOptions.size() + " voting options");
	}

	public Poll getEnabledPoll() {
		return pollService.getEnabledPoll();
	}

	public List<VoteGroup> getVoteGroups() {
		return voteGroupService.getVoteGroupsFromPoll(getEnabledPoll());
	}

	public void authenticate() {
		System.out.println("Authenticating...");
		this.setCredential(null);

		if (studentIdInput != null && !studentIdInput.isEmpty()) {
			if (USE_JSD_AUTH) {
				try { // VERY HACKY METHOD of authenticating student IDs ---
						// Uses Jordan School District's Overdrive login.
					int studentId = Integer.parseInt(studentIdInput);
					System.out.println("Using JSD Authentication...");

					String url = "https://jordanut.libraryreserve.com/10/45/en/BANGAuthenticate.dll";

					HttpClient httpClient = HttpClientBuilder.create().build();
					HttpPost httpPost = new HttpPost(url);

					httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
					httpPost.setHeader("User-Agent",
							"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.103 Safari/537.36");
					httpPost.setHeader("Cookie", pollService.getCookie());

					List<NameValuePair> urlParameters = new ArrayList<>();
					urlParameters.add(new BasicNameValuePair("URL", "Default.htm"));
					urlParameters.add(new BasicNameValuePair("LibraryCardILS", "jordan"));
					urlParameters.add(new BasicNameValuePair("lcn", studentId + ""));

					httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));

					HttpResponse httpResponse = httpClient.execute(httpPost);
					String location = httpResponse.getFirstHeader("Location").getValue();

					if (location.contains("Error.htm")) {
						System.out.println("ID Was Invalid!");
						FacesContext.getCurrentInstance().addMessage(null,
								new FacesMessage("The entered Student ID is invalid."));
					} else {
						System.out.println("ID Was Valid!");
						Credential credential;
						// Inserts a Credential row so that an Entry can be
						// made.
						if ((credential = credentialService.getByStudentNumber(studentId)) == null) {
							System.out.println("Creating new Credential.");
							credential = new Credential(studentId);
							credentialService.insert(credential);
						} else {
							System.out.println("Found existing Credential.");
						}
						setCredential(credential); // Sets the valid Student ID
													// for use when voting.
					}

				} catch (NumberFormatException e) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Please only enter numbers."));
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else { // Use basic authentication methods
				System.out.println("Using Basic Authentication...");
				try {
					if (!studentIdInput.startsWith("8") || studentIdInput.length() != 7) {
						System.out.println("Invalid Format!");
						FacesContext.getCurrentInstance().addMessage(null,
								new FacesMessage("The Student ID you entered is invalid. Please try again."));
					} else {
						System.out.println("ID Was Valid!");
						int studentId = Integer.parseInt(studentIdInput);
						Credential credential;
						// Inserts a Credential row so that an Entry can be
						// made.
						if ((credential = credentialService.getByStudentNumber(studentId)) == null) {
							System.out.println("Creating new Credential.");
							credential = new Credential(studentId);
							credentialService.insert(credential);
						} else {
							System.out.println("Found existing Credential.");
						}
						setCredential(credential); // Sets the valid Student ID
													// for use when voting.
					}
				} catch (NumberFormatException e) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Please only enter numbers."));
				}
			}
		} else {
			System.out.println("Input was empty!");
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("You must enter your Student ID to continue."));
		}
		studentIdInput = null; // Clear input
	}

	public Credential getCredential() {
		return credential;
	}

	public void setCredential(Credential credential) {
		this.credential = credential;
		if (credential == null)
			this.setStudentHasAlreadyVoted(false);
		else
			this.setStudentHasAlreadyVoted(
					entryService.hasStudentVoted(credential.getId(), pollService.getEnabledPoll().getId()));
	}

	public String getStudentIdInput() {
		return studentIdInput;
	}

	public void setStudentIdInput(String studentIdInput) {
		System.out.println("Setting Student ID Input to " + studentIdInput);
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

	public void dummyVote() {
		System.out.println("Adding Dummy Vote");
		if (credential != null && pollService.getEnabledPoll() != null) {
			List<VoteGroup> voteGroups = pollService.getEnabledPoll().getVoteGroups();
			if (!voteGroups.isEmpty()) {
				Entry entry = new Entry(getCredential(), voteGroups.get(0), pollService.getEnabledPoll());
				entryService.insert(entry);
				pollService.getEnabledPoll().getEntries().add(entry);
				setVotingComplete(true);
				System.out.println("Dummy Vote Added");
			} else {
				System.out.println("Dummy Vote could not be added. VoteGroups was empty.");
			}
		}
	}
	
	public void recordVote(VoteGroup voteGroup) {
		Entry entry = new Entry(getCredential(), voteGroup, pollService.getEnabledPoll());
		entryService.insert(entry);
		pollService.getEnabledPoll().getEntries().add(entry);
		setVotingComplete(true);
	}

	public List<VoteGroup> getVotingOptions() {
		return votingOptions;
	}

	public void setVotingOptions(List<VoteGroup> votingOptions) {
		this.votingOptions = votingOptions;
	}

}
