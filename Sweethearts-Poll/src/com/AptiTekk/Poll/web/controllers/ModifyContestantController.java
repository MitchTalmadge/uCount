package com.AptiTekk.Poll.web.controllers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.Part;

import com.AptiTekk.Poll.core.ViewModelConverter;
import com.AptiTekk.Poll.core.VoteGroupService;
import com.AptiTekk.Poll.core.entityBeans.VoteGroup;
import com.AptiTekk.Poll.web.ViewModels.ContestantViewModel;

@ManagedBean
@SessionScoped
public class ModifyContestantController {

	public static final String[] VALID_IMAGE_TYPES = { "image/png", "image/jpeg", "image/gif" };

	@EJB
	VoteGroupService voteGroupService;
	
	private List<ContestantViewModel> entries;
	private VoteGroup voteGroup;
	
	private int voteGroupId;
	
	@PostConstruct
	public void init() {
		System.out.println("Init ModifyContestantController");
		entries = ViewModelConverter.toContestantViewModels(voteGroup.getContestants());
		entries.add(new ContestantViewModel());
		System.out.println("Entries len: " + entries.size());
		System.out.println("Entries initialized: " + entries.hashCode());
	}
	
	public void addContestant() {
		entries.add(new ContestantViewModel());
		System.out.println("Entries len: " + entries.size());
	}

	public void validateImage(FacesContext ctx, UIComponent comp, Object value) {
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

	public void upload(ContestantViewModel newContestant) {
		try (InputStream input = newContestant.getPictureUpload().getInputStream()) {
			Files.copy(input,
					new File("/resources/contestant_images/", newContestant.getPictureUpload().getSubmittedFileName())
							.toPath());
			newContestant.setPictureFileName(newContestant.getPictureUpload().getSubmittedFileName());
		} catch (IOException e) {
			// Show faces message?
		}
	}

	public List<ContestantViewModel> getEntries() {
		return entries;
	}

	public void setEntries(List<ContestantViewModel> entries) {
		this.entries = entries;
	}

	public VoteGroup getVoteGroup() {
		return voteGroup;
	}

	public void setVoteGroup(VoteGroup voteGroup) {
		this.voteGroup = voteGroup;
	}
	public int getVoteGroupId() {
		return voteGroupId;
	}

	public void setVoteGroupId(int voteGroupId) {
		this.voteGroupId = voteGroupId;
	}

}
