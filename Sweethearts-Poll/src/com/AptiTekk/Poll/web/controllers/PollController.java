package com.AptiTekk.Poll.web.controllers;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import com.AptiTekk.Poll.core.ContestantService;
import com.AptiTekk.Poll.core.PollService;
import com.AptiTekk.Poll.core.VoteGroupService;
import com.AptiTekk.Poll.core.entityBeans.Contestant;
import com.AptiTekk.Poll.core.entityBeans.Poll;
import com.AptiTekk.Poll.core.entityBeans.VoteGroup;

@ManagedBean
@ApplicationScoped
public class PollController {

	@Inject
	PollService pollService;

	@Inject
	VoteGroupService voteGroupService;

	@Inject
	ContestantService contestantService;

	private Poll enabledPoll;
	private List<Poll> polls;

	@PostConstruct
	public void init() {
		setEnabledPoll(pollService.getEnabledPoll());
		setPolls(pollService.getAll());
	}

	public Poll getEnabledPoll() {
		return enabledPoll;
	}

	public void setEnabledPoll(Poll poll) {
		if (enabledPoll != null) {
			enabledPoll.setEnabled(false);
			pollService.merge(enabledPoll);
		}
		poll.setEnabled(true);
		poll = pollService.merge(poll);
		this.enabledPoll = poll;
	}

	public void enabledPollChanged() {
		System.out.println("Enabled Poll has been set to: " + enabledPoll.getName());
	}

	public void addDummyPolls() {
		Poll o = new Poll("EnabledPoll", "This is a test description for the Enabled Poll", true);
		Poll o2 = new Poll("DisabledPoll", "This is a test description for the Disabled Poll", false);
		pollService.insert(o);
		pollService.insert(o2);

		VoteGroup o3 = new VoteGroup(o);
		VoteGroup o4 = new VoteGroup(o);
		voteGroupService.insert(o3);
		voteGroupService.insert(o4);

		Contestant contestant = new Contestant(o3, "Kevin", "Thorne", "notFound.png");
		Contestant contestant1 = new Contestant(o3, "Megan", "Church", "notFound.png");

		Contestant contestant2 = new Contestant(o4, "Andrew", "Meservy", "notFound.png");
		Contestant contestant3 = new Contestant(o4, "Emma", "Lowe", "notFound.png");

		contestantService.insert(contestant);
		contestantService.insert(contestant1);
		contestantService.insert(contestant2);
		contestantService.insert(contestant3);

		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
				.getResponse();
		try {
			response.sendRedirect("manage.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<Poll> getPolls() {
		return polls;
	}

	public void setPolls(List<Poll> polls) {
		this.polls = polls;
	}

}
