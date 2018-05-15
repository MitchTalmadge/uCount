package org.BinghamTSA.uCount.web.controllers;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;

import org.BinghamTSA.uCount.core.ContestantService;
import org.BinghamTSA.uCount.core.VoteGroupService;
import org.BinghamTSA.uCount.core.entityBeans.Contestant;
import org.BinghamTSA.uCount.core.entityBeans.VoteGroup;
import org.BinghamTSA.uCount.core.utilities.PollLogger;

/**
 * The ModifyVoteGroupController is the backing bean for the VoteGroup modification page. It handles
 * adding and removing contestants, changing contestant names and images, and changing the VoteGroup
 * name and image.
 */
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
   * Temporary fields used for validation before inserting values into the Contestant itself.
   */
  private String editableName = "";

  private String editableVoteGroupName = "";
  private boolean editingVoteGroup = false;

  @PostConstruct
  public void init() {
    String voteGroupIdParam = FacesContext.getCurrentInstance().getExternalContext()
        .getRequestParameterMap().get("voteGroupId");
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
    Contestant contestant = new Contestant(voteGroup, "John Doe");
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
        PollLogger
            .logVerbose("Contestant Editing ID has been set to " + this.contestantIdBeingEdited);
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

    if (getPictureUpload() != null)
      uploadVoteGroupImage();
    else
      PollLogger.logVerbose("Not uploading picture -- it is null.");

    voteGroup.setName(editableVoteGroupName);
    voteGroupService.merge(voteGroup);
    voteGroup = voteGroupService.get(voteGroup.getId()); // Refresh
    // VoteGroup

    this.setEditingVoteGroup(false);
  }

  public void startContestantEditing(int contestantId) {
    PollLogger.logVerbose("Editing Contestant with ID: " + contestantId);
    this.setContestantIdBeingEdited(contestantId);
  }

  public void applyContestantChanges() {
    if (this.getEditableName().isEmpty()) {
      FacesContext.getCurrentInstance().addMessage("contestantEditForm",
          new FacesMessage("The Contestant Name cannot be empty!"));
      return;
    }

    if (getPictureUpload() != null)
      uploadContestantImage();
    else
      PollLogger.logVerbose("Not uploading picture -- it is null.");

    Contestant contestant = contestantService.get(contestantIdBeingEdited);
    if (contestant != null) {
      contestant.setName(getEditableName());
      contestantService.merge(contestant);

      voteGroup = voteGroupService.get(voteGroup.getId()); // Refresh
      // voteGroup
    }

    this.setContestantIdBeingEdited(-1);
  }

  public void uploadContestantImage() {
    PollLogger.logVerbose("Uploading Contestant Image...");

    Contestant contestant = contestantService.get(contestantIdBeingEdited);

    try {
      contestantService.uploadContestantImage(contestant, getPictureUpload());
    } catch (IOException e) {
      FacesContext.getCurrentInstance().addMessage("contestantEditForm",
          new FacesMessage("The image could not be applied."));
      e.printStackTrace();
    }

    voteGroup = voteGroupService.get(voteGroup.getId()); // Refresh
    // VoteGroup

    setPictureUpload(null);
  }

  public void uploadVoteGroupImage() {
    PollLogger.logVerbose("Uploading Votegroup Image...");

    try {
      voteGroupService.uploadVoteGroupImage(voteGroup, getPictureUpload());
    } catch (IOException e) {
      FacesContext.getCurrentInstance().addMessage("contestantEditForm",
          new FacesMessage("The image could not be applied."));
      e.printStackTrace();
    }

    voteGroup = voteGroupService.get(voteGroup.getId()); // Refresh
    // VoteGroup

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
