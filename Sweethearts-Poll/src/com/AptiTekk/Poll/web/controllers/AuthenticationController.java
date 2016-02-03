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

	@EJB
	PollService pollService;

	@EJB
	VoteGroupService voteGroupService;

	@EJB
	ContestantService contestantService;

	public Poll getEnabledPoll() {
		return pollService.getEnabledPoll();
	}
	
	public String authenticate(String studentIdInput)
	{
		//TODO: Authenticate user, send to vote if valid, send back to authenticate if invalid.
		boolean valid = true;
	
		if(!valid)
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The Student ID you entered is invalid. Please try again."));
			return "authenticate";
		}
		else
			return "vote";
	}

}
