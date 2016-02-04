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
		System.out.println("Init ManagerController");
		polls = pollService.getAll();
		if (!polls.isEmpty()) {
			this.selectedPoll = polls.get(0);
		}
		if (selectedPoll != null) {
			this.setEditablePollName(selectedPoll.getName());
			this.setEditablePollDescription(selectedPoll.getDescription());
		}
	}

	public Poll getEnabledPoll() {
		return pollService.getEnabledPoll();
	}

	public void setEnabledPoll(Poll poll) {
		pollService.enablePoll(poll);
	}

	public void onEnabledPollChanged() {
		Poll currentEnabledPoll = pollService.getEnabledPoll();
		if (currentEnabledPoll != null)
			System.out.println("Enabled Poll has been set to: " + currentEnabledPoll.getName());
		else
			System.out.println("Enabled Poll has been cleared. No polls are currently enabled.");
	}

	public List<Poll> getPolls() {
		return polls;
	}

	public Poll getSelectedPoll() {
		return selectedPoll;
	}

	public void setSelectedPoll(Poll selectedPoll) {
		System.out.println("Setting Selected Poll to " + selectedPoll.getName());
		this.selectedPoll = selectedPoll;
		if (selectedPoll != null) {
			this.setEditablePollName(selectedPoll.getName());
			this.setEditablePollDescription(selectedPoll.getDescription());
		}
		this.editingDescription = false;
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
