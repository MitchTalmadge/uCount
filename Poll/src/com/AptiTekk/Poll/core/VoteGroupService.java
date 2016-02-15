package com.AptiTekk.Poll.core;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.Part;

import com.AptiTekk.Poll.core.entityBeans.Contestant;
import com.AptiTekk.Poll.core.entityBeans.Poll;
import com.AptiTekk.Poll.core.entityBeans.QVoteGroup;
import com.AptiTekk.Poll.core.entityBeans.VoteGroup;
import com.AptiTekk.Poll.core.utilities.FileUploadUtilities;
import com.AptiTekk.Poll.core.utilities.PollLogger;
import com.mysema.query.jpa.impl.JPADeleteClause;
import com.mysema.query.jpa.impl.JPAQuery;

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

	public static void deleteVoteGroupImage(String fileName) {
		FileUploadUtilities.deleteUploadedImage(fileName);
	}

	public void deleteAll(int pollId) {
		List<VoteGroup> voteGroups = new JPAQuery(entityManager).from(voteGroupTable)
				.where(voteGroupTable.poll.id.eq(pollId)).list(voteGroupTable);
		for (VoteGroup voteGroup : voteGroups) {
			contestantService.deleteAllContestants(voteGroup.getId());
			entryService.deleteAllVoteGroupEntries(voteGroup.getId());
		}
		new JPADeleteClause(entityManager, voteGroupTable).where(voteGroupTable.poll.id.eq(pollId)).execute();
	}

}
