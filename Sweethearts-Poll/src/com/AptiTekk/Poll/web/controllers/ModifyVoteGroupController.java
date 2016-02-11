package com.AptiTekk.Poll.web.controllers;

import java.io.IOException;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;

import com.AptiTekk.Poll.core.ContestantService;
import com.AptiTekk.Poll.core.VoteGroupService;
import com.AptiTekk.Poll.core.entityBeans.Contestant;
import com.AptiTekk.Poll.core.entityBeans.VoteGroup;
import com.AptiTekk.Poll.core.utilities.FileUploadUtilities;
import com.AptiTekk.Poll.core.utilities.PollLogger;

@ManagedBean
@ViewScoped
public class ModifyVoteGroupController {

	@EJB
	VoteGroupService voteGroupService;

	@EJB
	ContestantService contestantService;

	private VoteGroup voteGroup;

	private Part pictureUpload;
	
	/**
	 * Refers to the id of the Contestant currently being edited. -1 for none.
	 */
	private int contestantIdBeingEdited = -1;

	/**
	 * Temporary fields used for validation before inserting values into the
	 * Contestant itself.
	 */
	private String editableName = "";
	
	private String editableVoteGroupName = "";
	private boolean editingVoteGroup = false;

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
		Contestant contestant = new Contestant(voteGroup, "John Doe", "notFound.png");
		contestantService.insert(contestant);

		voteGroup = voteGroupService.get(voteGroup.getId()); // Refresh
																// voteGroup
		PollLogger.logVerbose("Contestant Added");
	}

	public void deleteContestant(int contestantId) {
		contestantService.delete(contestantId);

		voteGroup = voteGroupService.get(voteGroup.getId()); // Refresh
																// voteGroup
		PollLogger.logVerbose("Contestant Deleted");
	}

	public void deleteAllContestants() {
		contestantService.deleteAllContestants(voteGroup.getId());

		voteGroup = voteGroupService.get(voteGroup.getId()); // Refresh
																// voteGroup
		PollLogger.logVerbose("All Contestants Deleted");
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
				this.setEditableName(contestant.getName());
				PollLogger.logVerbose("Contestant Editing ID has been set to " + this.contestantIdBeingEdited);
			} else {
				this.contestantIdBeingEdited = -1;
			}
		}
	}

	public String getEditableName() {
		return editableName;
	}

	public void setEditableName(String editableName) {
		this.editableName = editableName;
	}

	public void startVoteGroupEditing() {
		this.setEditingVoteGroup(true);
	}
	
	public void applyVoteGroupChanges() {
		if (this.getEditableVoteGroupName().isEmpty()) {
			FacesContext.getCurrentInstance().addMessage("voteGroupEditForm",
					new FacesMessage("The Vote Group Name cannot be empty!"));
			return;
		}
		voteGroup.setName(editableVoteGroupName);
		voteGroupService.merge(voteGroup);

		voteGroup = voteGroupService.get(voteGroup.getId()); // Refresh
																// voteGroup
		this.setEditingVoteGroup(false);
	}

	public void onEditButtonFired(int contestantId) {
		PollLogger.logVerbose("Editing Contestant with ID: " + contestantId);
		this.setContestantIdBeingEdited(contestantId);
	}

	public void onEditDoneButtonFired() {
		PollLogger.logVerbose("Editing Done Button Fired.");
		if (this.getEditableName().isEmpty()) {
			PollLogger.logVerbose("Contestant name was empty");
			FacesContext.getCurrentInstance().addMessage("contestantEditForm",
					new FacesMessage("The Contestant Name cannot be empty!"));
			return;
		}
		Contestant contestant = contestantService.get(contestantIdBeingEdited);
		if (contestant != null) {
			PollLogger.logVerbose("Setting contestant fields...");
			contestant.setName(getEditableName());
			contestantService.merge(contestant);

			voteGroup = voteGroupService.get(voteGroup.getId()); // Refresh
																	// voteGroup
		}
		this.setContestantIdBeingEdited(-1);
	}

	public void uploadContestantImage() {
		Contestant contestant = contestantService.get(contestantIdBeingEdited);
		if (contestant == null) {
			PollLogger.logError("Contestant not found.");
			return;
		}

		PollLogger.logVerbose("Uploading...");

		// Generate a random file name.
		String fileName = UUID.randomUUID().toString();

		try {
			FileUploadUtilities.uploadPartToPathAndCrop(getPictureUpload(), "/resources/contestant_images/", fileName,
					300);

			contestant.setPictureFileName(fileName);
			contestantService.merge(contestant);

			voteGroup = voteGroupService.get(voteGroup.getId()); // Refresh
																	// voteGroup
			PollLogger.logVerbose("Contestant Image Added.");
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage("contestantEditForm",
					new FacesMessage("The image could not be applied."));
			e.printStackTrace();
		}

		setPictureUpload(null);
	}
	
	public void uploadVoteGroupImage() {
		PollLogger.logVerbose("Uploading...");

		// Generate a random file name.
		String fileName = String.valueOf(voteGroup.getId());

		try {
			FileUploadUtilities.uploadPartToPathAndCrop(getPictureUpload(), "/resources/votegroup_images/", fileName,
					300);

			voteGroup.setPictureFileName(fileName);
			voteGroupService.merge(voteGroup);

			voteGroup = voteGroupService.get(voteGroup.getId()); // Refresh
																	// voteGroup
			PollLogger.logVerbose("VoteGroup Image Updated.");
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage("contestantEditForm",
					new FacesMessage("The image could not be applied."));
			e.printStackTrace();
		}

		setPictureUpload(null);
	}

	public Part getPictureUpload() {
		return pictureUpload;
	}

	public void setPictureUpload(Part pictureUpload) {
		this.pictureUpload = pictureUpload;
	}

	public boolean isEditingVoteGroup() {
		return editingVoteGroup;
	}

	public void setEditingVoteGroup(boolean editingVoteGroup) {
		this.editingVoteGroup = editingVoteGroup;
		this.editableVoteGroupName = voteGroup.getName();
	}

	public String getEditableVoteGroupName() {
		return editableVoteGroupName;
	}

	public void setEditableVoteGroupName(String editableVoteGroupName) {
		this.editableVoteGroupName = editableVoteGroupName;
	}

}
