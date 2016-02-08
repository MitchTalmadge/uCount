package com.AptiTekk.Poll.web.controllers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.Part;

import com.AptiTekk.Poll.core.ContestantService;
import com.AptiTekk.Poll.core.VoteGroupService;
import com.AptiTekk.Poll.core.entityBeans.Contestant;
import com.AptiTekk.Poll.core.entityBeans.VoteGroup;
import com.AptiTekk.Poll.core.utilities.FileUploadUtilities;
import com.sun.mail.imap.protocol.ID;

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

	public void deleteContestant(int contestantId) {
		contestantService.delete(contestantId);

		voteGroup = voteGroupService.get(voteGroup.getId()); // Refresh
																// voteGroup
		System.out.println("Contestant Deleted");
	}

	public void deleteAllContestants() {
		contestantService.deleteAllContestants(voteGroup.getId());

		voteGroup = voteGroupService.get(voteGroup.getId()); // Refresh
																// voteGroup
		System.out.println("All Contestants Deleted");
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

	public void uploadContestantImage() {
		Contestant contestant = contestantService.get(contestantIdBeingEdited);
		if (contestant == null) {
			System.out.println("Contestant not found.");
			return;
		}

		System.out.println("Uploading...");

		// Generate a random file name.
		String fileName = UUID.randomUUID().toString();

		try {
			FileUploadUtilities.uploadPartToPathAndCrop(getPictureUpload(), "/resources/contestant_images/", fileName,
					300);

			contestant.setPictureFileName(fileName);
			contestantService.merge(contestant);

			voteGroup = voteGroupService.get(voteGroup.getId()); // Refresh
																	// voteGroup
			System.out.println("Contestant Image Added.");
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

}
