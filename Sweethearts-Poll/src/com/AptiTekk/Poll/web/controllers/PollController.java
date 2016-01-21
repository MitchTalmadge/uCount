package com.AptiTekk.Poll.web.controllers;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
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
@RequestScoped
public class PollController {
	
	@Inject
	PollService pollService;
	
	@Inject
	VoteGroupService voteGroupService;
	
	@Inject
	ContestantService contestantService;
	
	private Poll enabledPoll;
	private List<VoteGroup> voteGroups;
	
	@PostConstruct
	public void init() {
		setEnabledPoll(pollService.getEnabledPoll());
		if(enabledPoll != null)
			setVoteGroups(voteGroupService.getVoteGroupsFromPoll(enabledPoll));
	}
	
	public Poll getEnabledPoll() {
		return enabledPoll;
	}

	public void setEnabledPoll(Poll enabledPoll) {
		this.enabledPoll = enabledPoll;
	}
	
	public void addDummyPolls() {
		Poll o = new Poll("Enabled", "Test Poll", true);
		Poll o2 = new Poll("Disabled", "Test Poll", false);
		pollService.insert(o);
		pollService.insert(o2);
		
		VoteGroup o3 = new VoteGroup(o);
		VoteGroup o4 = new VoteGroup(o);
		voteGroupService.insert(o3);
		voteGroupService.insert(o4);
		
		Contestant contestant = new Contestant(o3, "Kevin", "Thorne", "notFound.png");
		Contestant contestant1 = new Contestant(o3, "Megan", "Church", "notFound.png");
		
		Contestant contestant2 = new Contestant(o3, "Andrew", "Meservy", "notFound.png");
		Contestant contestant3 = new Contestant(o3, "Emma", "Lowe", "notFound.png");
		
		contestantService.insert(contestant);
		contestantService.insert(contestant1);
		contestantService.insert(contestant2);
		contestantService.insert(contestant3);
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		try {
			response.sendRedirect("index.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<VoteGroup> getVoteGroups() {
		return voteGroups;
	}

	public void setVoteGroups(List<VoteGroup> voteGroups) {
		this.voteGroups = voteGroups;
	}

}
