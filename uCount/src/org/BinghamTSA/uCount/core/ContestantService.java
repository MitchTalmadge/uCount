package org.BinghamTSA.uCount.core;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.Part;

import org.BinghamTSA.uCount.core.entityBeans.Contestant;
import org.BinghamTSA.uCount.core.entityBeans.Poll;
import org.BinghamTSA.uCount.core.entityBeans.QContestant;
import org.BinghamTSA.uCount.core.entityBeans.VoteGroup;
import org.BinghamTSA.uCount.core.utilities.FileUploadUtilities;
import org.BinghamTSA.uCount.core.utilities.PollLogger;

import com.mysema.query.jpa.impl.JPADeleteClause;
import com.mysema.query.jpa.impl.JPAQuery;

@Stateless
public class ContestantService extends Service<Contestant> {
	QContestant contestantTable = QContestant.contestant;
	
	@EJB
	VoteGroupService voteGroupService;

	public ContestantService() {
		this.type = Contestant.class;
	}

	public List<Contestant> getContestantsByPoll(Poll poll) {
		return new JPAQuery(entityManager).from(contestantTable)
				.where(contestantTable.voteGroup.poll.name.eq(poll.getName())).list(contestantTable);
	}

	public void deleteAllContestants(int voteGroupId) {
		VoteGroup voteGroup = voteGroupService.get(voteGroupId);
		if(voteGroup != null)
		{
			for(Contestant contestant : voteGroup.getContestants())
			{
				FileUploadUtilities.deleteUploadedImage(contestant.getPictureFileName());
			}
		}
		
		new JPADeleteClause(entityManager, contestantTable).where(contestantTable.voteGroup.id.eq(voteGroupId))
				.execute();
	}
	
	@Override
	public void delete(int id)
	{
		Contestant contestant = get(id);
		if(contestant != null)
		{
			deleteContestantImage(contestant.getPictureFileName());
		}
		
		super.delete(id);
	}
	
	public void uploadContestantImage(Contestant contestant, Part part) throws IOException {
		if(contestant == null)
		{
			PollLogger.logError("Contestant was null.");
		}
		
		// Generate a random file name.
		String fileName = UUID.randomUUID().toString();

		FileUploadUtilities.uploadImageToPathAndCrop(part, fileName, 300);

		deleteContestantImage(contestant.getPictureFileName());

		contestant.setPictureFileName(fileName);
		merge(contestant);

		PollLogger.logVerbose("Contestant Image Updated.");
	}
	
	public static void deleteContestantImage(String fileName)
	{
	    FileUploadUtilities.deleteUploadedImage(fileName);
	}

}
