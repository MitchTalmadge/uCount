package com.AptiTekk.Poll.web.controllers;

import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import com.AptiTekk.Poll.core.PollService;
import com.AptiTekk.Poll.core.VoteGroupService;
import com.AptiTekk.Poll.core.entityBeans.Poll;
import com.AptiTekk.Poll.core.entityBeans.VoteGroup;

@ManagedBean
@ViewScoped
public class ManagerController {

	@Inject
	PollService pollService;

	@Inject
	VoteGroupService voteGroupService;

	/**
	 * The poll currently being looked at on the manage page.
	 */
	private Poll selectedPoll;

	/**
	 * Whether or not we are currently editing the description.
	 */
	private boolean editingDescription = false;

	@PostConstruct
	public void init() {
		System.out.println("Init ManagerController");
		List<Poll> polls = pollService.getAll();
		if (!polls.isEmpty()) {
			this.selectedPoll = polls.get(0);
		}
	}

	public Poll getSelectedPoll() {
		return selectedPoll;
	}

	public void setSelectedPoll(Poll selectedPoll) {
		System.out.println("Setting Selected Poll to " + selectedPoll.getName());
		this.selectedPoll = selectedPoll;
		this.editingDescription = false;
	}

	public boolean getEditingDescription() {
		return editingDescription;
	}

	public void setEditingDescription(boolean editingDescription) {
		this.editingDescription = editingDescription;
	}

	public void onEditDescriptionButtonFired() {
		setEditingDescription(true);
	}

	public void onEditDescriptionDoneButtonFired() {
		setEditingDescription(false);
		pollService.merge(selectedPoll);
	}

	public void addNewVoteGroup() {
		if (selectedPoll != null) {
			VoteGroup voteGroup = new VoteGroup(selectedPoll);
			voteGroupService.insert(voteGroup);
			selectedPoll.getVoteGroups().add(voteGroup);
		}
	}

	public void deleteVoteGroup(VoteGroup voteGroup) {
		if (selectedPoll != null) {
			System.out.println("Deleting VoteGroup with ID: " + voteGroup.getId());
			voteGroupService.delete(voteGroup.getId());
			Iterator<VoteGroup> iterator = selectedPoll.getVoteGroups().iterator();
			while (iterator.hasNext()) {
				if (iterator.next().getId() == voteGroup.getId()) {
					iterator.remove();
					break;
				}
			}
		}
	}

}
