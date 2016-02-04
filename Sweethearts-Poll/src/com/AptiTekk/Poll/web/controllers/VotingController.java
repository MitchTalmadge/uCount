package com.AptiTekk.Poll.web.controllers;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.AptiTekk.Poll.core.ContestantService;
import com.AptiTekk.Poll.core.PollService;
import com.AptiTekk.Poll.core.VoteGroupService;
import com.AptiTekk.Poll.core.entityBeans.Poll;

@ManagedBean
@ViewScoped
public class VotingController {

	private static final boolean USE_JSD_AUTH = false;

	@EJB
	PollService pollService;

	@EJB
	VoteGroupService voteGroupService;

	@EJB
	ContestantService contestantService;

	private int studentId;

	private String studentIdInput;

	@PostConstruct
	public void init() {
		this.setStudentId(-1);
	}

	public Poll getEnabledPoll() {
		return pollService.getEnabledPoll();
	}

	public void authenticate() {
		System.out.println("Authenticating...");
		this.setStudentId(-1);

		if (studentIdInput != null && !studentIdInput.isEmpty()) {
			if (USE_JSD_AUTH) {
				System.out.println("Using JSD Authentication...");
				// TODO: Get reply from JSD and see how they want us to auth
			} else { // Use basic authentication methods
				System.out.println("Using Basic Authentication...");
				try {
					if (!studentIdInput.startsWith("8") || studentIdInput.length() != 7) {
						System.out.println("Invalid Format!");
						FacesContext.getCurrentInstance().addMessage(null,
								new FacesMessage("The Student ID you entered is invalid. Please try again."));
					} else {
						System.out.println("ID Was Valid!");
						setStudentId(Integer.parseInt(studentIdInput)); //Sets the valid Student ID for use when voting.
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
		studentIdInput = null; //Clear input
	}

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public String getStudentIdInput() {
		return studentIdInput;
	}

	public void setStudentIdInput(String studentIdInput) {
		System.out.println("Setting Student ID Input to " + studentIdInput);
		this.studentIdInput = studentIdInput;
	}

}
