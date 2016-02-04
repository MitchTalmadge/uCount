package com.AptiTekk.Poll.web.controllers;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.AptiTekk.Poll.core.VoteGroupService;
import com.AptiTekk.Poll.core.entityBeans.VoteGroup;

@ManagedBean
@ViewScoped
public class ModifyVoteGroupController {

	@EJB
	VoteGroupService voteGroupService;

	private VoteGroup voteGroup;

	@PostConstruct
	public void init() {

	}

	public String getVoteGroupIdParam()
	{
		return "";
	}
	
	public void setVoteGroupIdParam(String voteGroupIdParam) {
		try {
			int voteGroupId = Integer.parseInt(voteGroupIdParam);
			setVoteGroup(voteGroupService.get(voteGroupId));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

	public VoteGroup getVoteGroup() {
		return voteGroup;
	}

	public void setVoteGroup(VoteGroup voteGroup) {
		this.voteGroup = voteGroup;
	}

}
