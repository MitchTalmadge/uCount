package com.AptiTekk.Poll.web.controllers;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import com.AptiTekk.Poll.core.PollService;
import com.AptiTekk.Poll.core.entityBeans.Poll;

@ManagedBean
@ViewScoped
public class ManagerController {

	@Inject
	PollService pollService;

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
		if (!pollService.getPolls().isEmpty()) {
			this.selectedPoll = pollService.getPolls().get(0);
		}
	}

	public Poll getSelectedPoll() {
		return selectedPoll;
	}

	public void setSelectedPoll(Poll selectedPoll) {
		System.out.println("Setting Selected Poll to "+selectedPoll.getName());
		this.selectedPoll = pollService.getPollById(selectedPoll.getId());
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
		pollService.merge(selectedPoll); // Updates Poll in database with new
											// description.
		System.out.println("Saved Poll Description to Database.");
	}

}
