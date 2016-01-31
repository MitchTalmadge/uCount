package com.AptiTekk.Poll.web.controllers;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
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

	private boolean editingDescription = false;

	@PostConstruct
	public void init() {
		if (!pollService.getAll().isEmpty())
			selectedPoll = pollService.getAll().get(0);
	}

	public Poll getSelectedPoll() {
		return selectedPoll;
	}

	public void setSelectedPoll(Poll selectedPoll) {
		this.selectedPoll = selectedPoll;
	}

	public boolean getEditingDescription() {
		return editingDescription;
	}

	public void setEditingDescription(boolean editingDescription) {
		this.editingDescription = editingDescription;
	}

}
