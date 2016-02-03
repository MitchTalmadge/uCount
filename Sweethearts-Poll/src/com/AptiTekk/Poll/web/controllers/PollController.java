package com.AptiTekk.Poll.web.controllers;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;

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

	private List<Poll> polls;

	private Poll enabledPoll;

	@PostConstruct
	public void init() {
		polls = pollService.getAll();
		enabledPoll = pollService.getEnabledPoll();
	}

	public List<Poll> getPolls() {
		System.out.println("Getting all Polls...");
		return polls;
	}

	public Poll getEnabledPoll() {
		if (enabledPoll != null)
			System.out.println("The enabled poll's description is: " + enabledPoll.getDescription());
		return enabledPoll;
	}

	public void setEnabledPoll(Poll poll) {
		enabledPoll = poll;
		pollService.enablePoll(poll);
	}

	public void enabledPollChanged() {
		Poll currentEnabledPoll = enabledPoll;
		if (currentEnabledPoll != null)
			System.out.println("Enabled Poll has been set to: " + enabledPoll.getName());
		else
			System.out.println("Enabled Poll has been cleared. No polls are currently enabled.");
	}

	public String addDummyPolls() {
		System.out.println("Adding Dummy Polls...");
		Poll poll1 = new Poll("Poll 1", "This is a test description for Poll 1", false);
		Poll poll2 = new Poll("Poll 2", "This is a test description for Poll 2", false);
		pollService.insert(poll1);
		pollService.insert(poll2);

		VoteGroup voteGroup1 = new VoteGroup(poll1);
		VoteGroup voteGroup2 = new VoteGroup(poll1);
		voteGroupService.insert(voteGroup1);
		voteGroupService.insert(voteGroup2);

		Contestant contestant1 = new Contestant(voteGroup1, "Kevin", "Thorne", "notFound.png");
		Contestant contestant2 = new Contestant(voteGroup1, "Megan", "Church", "notFound.png");

		Contestant contestant3 = new Contestant(voteGroup2, "Andrew", "Meservy", "notFound.png");
		Contestant contestant4 = new Contestant(voteGroup2, "Emma", "Lowe", "notFound.png");

		contestantService.insert(contestant1);
		contestantService.insert(contestant2);
		contestantService.insert(contestant3);
		contestantService.insert(contestant4);

		System.out.println("Dummy Polls Added.");

		return "manage";
	}

}
