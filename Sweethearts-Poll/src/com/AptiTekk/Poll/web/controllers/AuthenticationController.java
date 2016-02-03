package com.AptiTekk.Poll.web.controllers;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.AptiTekk.Poll.core.ContestantService;
import com.AptiTekk.Poll.core.PollService;
import com.AptiTekk.Poll.core.VoteGroupService;
import com.AptiTekk.Poll.core.entityBeans.Poll;

@ManagedBean
@ViewScoped
public class AuthenticationController {

	private static final boolean USE_JSD_AUTH = false;

	@EJB
	PollService pollService;

	@EJB
	VoteGroupService voteGroupService;

	@EJB
	ContestantService contestantService;

	public Poll getEnabledPoll() {
		return pollService.getEnabledPoll();
	}

	public String authenticate(String studentIdInput) {
		boolean valid = false;

		if (USE_JSD_AUTH) {
			// TODO: Get reply from JSD and see how they want us to auth
		} else { // Use basic authentication methods
			try {
				if (!studentIdInput.startsWith("8") || studentIdInput.length() != 7)
					valid = false;
				else {
					int studentId = Integer.parseInt(studentIdInput);
					FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("studentId", studentId);
					valid = true;
				}
			} catch (NumberFormatException e) {
				valid = false;
			}
		}

		if (!valid) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("The Student ID you entered is invalid. Please try again."));
			return "authenticate";
		} else
			return "vote";
	}

}
