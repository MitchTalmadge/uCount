package com.AptiTekk.Poll.web.controllers;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.AptiTekk.Poll.core.ContestantService;
import com.AptiTekk.Poll.core.VoteGroupService;
import com.AptiTekk.Poll.core.entityBeans.Contestant;
import com.AptiTekk.Poll.core.entityBeans.VoteGroup;

@ManagedBean
@ViewScoped
public class ModifyVoteGroupController {

	@EJB
	VoteGroupService voteGroupService;

	@EJB
	ContestantService contestantService;

	private VoteGroup voteGroup;

	/**
	 * Refers to the id of the Contestant currently being edited. -1 for none.
	 */
	private int contestantIdBeingEdited = -1;

	/**
	 * Temporary fields used for validation before inserting values into the
	 * Contestant itself.
	 */
	private String editableFirstName = "";
	private String editableLastName = "";

	@PostConstruct
	public void init() {
		String voteGroupIdParam = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get("voteGroupId");
		try {
			int voteGroupId = Integer.parseInt(voteGroupIdParam);
			setVoteGroup(voteGroupService.get(voteGroupId));
		} catch (NumberFormatException ignored) {
		}
	}

	public VoteGroup getVoteGroup() {
		return voteGroup;
	}

	public void setVoteGroup(VoteGroup voteGroup) {
		this.voteGroup = voteGroup;
	}

	public void addNewContestant() {
		Contestant contestant = new Contestant(voteGroup, "John", "Doe", "notFound.png");
		contestantService.insert(contestant);

		voteGroup = voteGroupService.get(voteGroup.getId()); // Refresh
																// voteGroup
		System.out.println("Contestant Added");
	}

	public int getContestantIdBeingEdited() {
		return contestantIdBeingEdited;
	}

	public void setContestantIdBeingEdited(int contestantIdBeingEdited) {
		if (contestantIdBeingEdited == -1) {
			this.contestantIdBeingEdited = contestantIdBeingEdited;
		} else {
			Contestant contestant = contestantService.get(contestantIdBeingEdited);
			if (contestant != null) {
				this.contestantIdBeingEdited = contestantIdBeingEdited;
				this.setEditableFirstName(contestant.getFirstName());
				this.setEditableLastName(contestant.getLastName());
				System.out.println("Contestant Editing ID has been set to " + this.contestantIdBeingEdited);
			} else {
				this.contestantIdBeingEdited = -1;
			}
		}
	}

	public String getEditableFirstName() {
		return editableFirstName;
	}

	public void setEditableFirstName(String editableFirstName) {
		this.editableFirstName = editableFirstName;
	}

	public String getEditableLastName() {
		return editableLastName;
	}

	public void setEditableLastName(String editableLastName) {
		this.editableLastName = editableLastName;
	}

	public void onEditButtonFired(int contestantId) {
		System.out.println("Editing Contestant with ID: " + contestantId);
		this.setContestantIdBeingEdited(contestantId);
	}

	public void onEditDoneButtonFired() {
		System.out.println("Editing Done Button Fired.");
		if (this.getEditableFirstName().isEmpty()) {
			System.out.println("First name was empty");
			FacesContext.getCurrentInstance().addMessage("contestantEditForm",
					new FacesMessage("The First Name cannot be empty!"));
			return;
		} else if (this.getEditableLastName().isEmpty()) {
			System.out.println("Last name was empty");
			FacesContext.getCurrentInstance().addMessage("contestantEditForm",
					new FacesMessage("The Last Name cannot be empty!"));
			return;
		}
		Contestant contestant = contestantService.get(contestantIdBeingEdited);
		if (contestant != null) {
			System.out.println("Setting contestant fields...");
			contestant.setFirstName(getEditableFirstName());
			contestant.setLastName(getEditableLastName());
			contestantService.merge(contestant);

			voteGroup = voteGroupService.get(voteGroup.getId()); // Refresh
																	// voteGroup
		}
		this.setContestantIdBeingEdited(-1);
	}

}
