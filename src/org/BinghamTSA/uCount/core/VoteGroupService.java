package org.BinghamTSA.uCount.core;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.Part;

import org.BinghamTSA.uCount.core.entityBeans.Contestant;
import org.BinghamTSA.uCount.core.entityBeans.Poll;
import org.BinghamTSA.uCount.core.entityBeans.QVoteGroup;
import org.BinghamTSA.uCount.core.entityBeans.VoteGroup;
import org.BinghamTSA.uCount.core.utilities.FileUploadUtilities;
import org.BinghamTSA.uCount.core.utilities.PollLogger;

import com.mysema.query.jpa.impl.JPADeleteClause;
import com.mysema.query.jpa.impl.JPAQuery;

/**
 * The VoteGroupService provides methods for accessing and modifying VoteGroup database objects
 * using JPA.
 */
@Stateless
public class VoteGroupService extends Service<VoteGroup> {
  private QVoteGroup voteGroupTable = QVoteGroup.voteGroup;

  @EJB
  ContestantService contestantService;

  @EJB
  EntryService entryService;

  public VoteGroupService() {
    this.type = VoteGroup.class;
  }

  /**
   * Returns a List of VoteGroups from a given Poll.
   * 
   * @param poll The Poll to get VoteGroups from.
   * @return A List of VoteGroups from the given Poll.
   */
  public List<VoteGroup> getVoteGroupsFromPoll(Poll poll) {
    if (poll != null)
      return new JPAQuery(entityManager).from(voteGroupTable).where(voteGroupTable.poll.eq(poll))
          .list(voteGroupTable);
    return null;
  }

  @Override
  public void delete(int id) {
    VoteGroup voteGroup = get(id);
    if (voteGroup != null) {
      for (Contestant contestant : voteGroup.getContestants())
        ContestantService.deleteContestantImage(contestant.getPictureFileName());
    }

    super.delete(id);
  }

  /**
   * Uploads the given Part object and formats it as an image for the given VoteGroup. Automatically
   * crops and resizes the image to 300px and gives the image a unique name.
   * 
   * @param voteGroup The VoteGroup to upload the image to.
   * @param part The Part object containing the image to be uploaded.
   * @throws IOException
   */
  public void uploadVoteGroupImage(VoteGroup voteGroup, Part part) throws IOException {
    if (voteGroup == null) {
      PollLogger.logError("VoteGroup was null.");
    }

    // Generate a random file name.
    String fileName = UUID.randomUUID().toString();

    FileUploadUtilities.uploadImageToPathAndCrop(part, fileName, 300);

    deleteVoteGroupImage(voteGroup.getPictureFileName());

    voteGroup.setPictureFileName(fileName);
    merge(voteGroup);

    PollLogger.logVerbose("VoteGroup Image Updated.");
  }

  /**
   * Deletes an image from the VoteGroup images by a given file name.
   * 
   * @param fileName The name of the VoteGroup image to delete.
   */
  public static void deleteVoteGroupImage(String fileName) {
    FileUploadUtilities.deleteUploadedImage(fileName);
  }

  /**
   * Deletes all VoteGroups by a given Poll.
   * 
   * @param pollId The ID of the Poll to remove all VoteGroups from.
   */
  public void deleteAll(int pollId) {
    List<VoteGroup> voteGroups = new JPAQuery(entityManager).from(voteGroupTable)
        .where(voteGroupTable.poll.id.eq(pollId)).list(voteGroupTable);
    for (VoteGroup voteGroup : voteGroups) {
      contestantService.deleteAllContestants(voteGroup.getId());
      entryService.deleteAllVoteGroupEntries(voteGroup.getId());
    }
    new JPADeleteClause(entityManager, voteGroupTable).where(voteGroupTable.poll.id.eq(pollId))
        .execute();
  }

}
