package com.AptiTekk.Poll.web.controllers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

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

@ManagedBean
@ViewScoped
public class ModifyVoteGroupController {

	public static final String[] VALID_IMAGE_TYPES = { "image/png", "image/jpeg", "image/gif" };

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

	public void validateImage(FacesContext ctx, UIComponent comp, Object value) {
		System.out.println("Caught upload, validating)");
		List<FacesMessage> msgs = new ArrayList<FacesMessage>();
		Part file = (Part) value;
		if (file.getSize() > 1024 * 1024) {
			msgs.add(new FacesMessage("Too big must be at most 5MB"));
		}
		boolean valid = false;
		for (String validType : VALID_IMAGE_TYPES) {
			if (validType.equals(file.getContentType())) {
				valid = true;
			}
		}
		if (!valid) {
			msgs.add(new FacesMessage("file not valid image"));
		}
		if (!msgs.isEmpty()) {
			throw new ValidatorException(msgs);
		}
	}

	public void upload(Contestant newContestant) {
		System.out.println("Uploading...");
		String fileName = hash(getPictureUpload().getSubmittedFileName());
		try (InputStream input = getPictureUpload().getInputStream()) {
			Files.copy(input,
					new File("/resources/contestant_images/", fileName)
							.toPath());
			newContestant.setPictureFileName(fileName);
		} catch (IOException e) {
			// Show faces message?
		}
		
		setPictureUpload(null);
	}

	private String hash(String string) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(string.getBytes());
			byte byteData[] = md.digest();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Part getPictureUpload() {
		return pictureUpload;
	}

	public void setPictureUpload(Part pictureUpload) {
		this.pictureUpload = pictureUpload;
	}

}
