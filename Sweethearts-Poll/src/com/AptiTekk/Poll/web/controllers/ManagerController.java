package com.AptiTekk.Poll.web.controllers;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.AptiTekk.Poll.core.ContestantService;
import com.AptiTekk.Poll.core.PollService;
import com.AptiTekk.Poll.core.VoteGroupService;
import com.AptiTekk.Poll.core.entityBeans.Contestant;
import com.AptiTekk.Poll.core.entityBeans.Poll;
import com.AptiTekk.Poll.core.entityBeans.VoteGroup;
import com.AptiTekk.Poll.core.utilities.Logger;

@ManagedBean
@ViewScoped
public class ManagerController {

	@EJB
	PollService pollService;

	@EJB
	VoteGroupService voteGroupService;

	@EJB
	ContestantService contestantService;

	private List<Poll> polls;

	/**
	 * The poll currently being looked at on the manage page.
	 */
	private Poll selectedPoll;

	/**
	 * Whether or not we are currently editing the poll name.
	 */
	private boolean editingName = false;

	/**
	 * Whether or not we are currently editing the poll description.
	 */
	private boolean editingDescription = false;

	/**
	 * These variables are used when editing the poll name/description so that
	 * input can be verified.
	 */
	private String editablePollName = "";
	private String editablePollDescription = "";

	@PostConstruct
	public void init() {
		Logger.logVerbose("Initialized ManagerController");
		polls = pollService.getAll();
		if (!polls.isEmpty()) {
			setSelectedPoll(polls.get(0));
		}
	}

	public Poll getEnabledPoll() {
		return pollService.getEnabledPoll();
	}

	public void setEnabledPoll(Poll poll) {
		pollService.enablePoll(poll);
	}

	public List<Poll> getPolls() {
		return polls;
	}

	public Poll getSelectedPoll() {
		return selectedPoll;
	}

	public void setSelectedPoll(Poll selectedPoll) {
		Logger.logVerbose("Setting Selected Poll to " + selectedPoll.getName());
		this.selectedPoll = selectedPoll;
		if (selectedPoll != null) {
			this.setEditablePollName(selectedPoll.getName());
			this.setEditablePollDescription(selectedPoll.getDescription());
		}
		this.editingDescription = false;
	}

	public void addNewPoll() {
		Poll poll = new Poll("New Poll", "This is a new poll. Edit its description here!", false);
		pollService.insert(poll);
		Logger.logVerbose("Added New Poll.");
		if(polls.isEmpty()) //If the number of available polls right now is 0, call init so that a poll is selected.
			init();
		else //Otherwise, just refresh the polls list; don't leave the currently selected poll.
			polls = pollService.getAll();
	}

	public void deleteSelectedPoll() {
		if (selectedPoll != null) {
			pollService.delete(selectedPoll.getId());
			pollService.refreshEnabledPoll();
			selectedPoll = null;
			Logger.logVerbose("Deleted Selected Poll.");
			init();
		}
	}

	public String getEditablePollName() {
		return editablePollName;
	}

	public void setEditablePollName(String editablePollName) {
		this.editablePollName = editablePollName;
	}

	public boolean getEditingName() {
		return editingName;
	}

	public void setEditingName(boolean editingName) {
		this.editingName = editingName;
	}

	public void onEditNameButtonFired() {
		this.setEditablePollName(selectedPoll.getName());
		setEditingName(true);
	}

	public void onEditNameDoneButtonFired() {
		if (this.getEditablePollName().isEmpty()) {
			FacesContext.getCurrentInstance().addMessage("nameForm",
					new FacesMessage("The Poll Name cannot be empty!"));
			return;
		}
		setEditingName(false);
		selectedPoll.setName(editablePollName);
		pollService.merge(selectedPoll);
		if (pollService.getEnabledPoll() != null) {
			if (selectedPoll.getId() == pollService.getEnabledPoll().getId())
				pollService.refreshEnabledPoll();
		}
	}

	public String getEditablePollDescription() {
		return editablePollDescription;
	}

	public void setEditablePollDescription(String editablePollDescription) {
		this.editablePollDescription = editablePollDescription;
	}

	public boolean getEditingDescription() {
		return editingDescription;
	}

	public void setEditingDescription(boolean editingDescription) {
		this.editingDescription = editingDescription;
	}

	public void onEditDescriptionButtonFired() {
		this.setEditablePollDescription(selectedPoll.getDescription());
		setEditingDescription(true);
	}

	public void onEditDescriptionDoneButtonFired() {
		if (this.getEditablePollDescription().isEmpty()) {
			FacesContext.getCurrentInstance().addMessage("descriptionForm",
					new FacesMessage("The Poll Description cannot be empty!"));
			return;
		}
		setEditingDescription(false);
		selectedPoll.setDescription(editablePollDescription);
		pollService.merge(selectedPoll);
		if (pollService.getEnabledPoll() != null) {
			if (selectedPoll.getId() == pollService.getEnabledPoll().getId())
				pollService.refreshEnabledPoll();
		}
	}

	public void addNewVoteGroup() {
		if (selectedPoll != null) {
			VoteGroup voteGroup = new VoteGroup(selectedPoll, "New Votegroup");
			voteGroupService.insert(voteGroup);
			selectedPoll.getVoteGroups().add(voteGroup);
			pollService.refreshEnabledPoll();
		}
	}

	public void deleteVoteGroup(VoteGroup voteGroup) {
		if (selectedPoll != null) {
			Logger.logVerbose("Deleting VoteGroup with ID: " + voteGroup.getId());
			voteGroupService.delete(voteGroup.getId());
			Iterator<VoteGroup> iterator = selectedPoll.getVoteGroups().iterator();
			while (iterator.hasNext()) {
				if (iterator.next().getId() == voteGroup.getId()) {
					iterator.remove();
					break;
				}
			}
			pollService.refreshEnabledPoll();
		}
	}
	
}
