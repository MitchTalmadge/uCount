package org.BinghamTSA.uCount.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import org.BinghamTSA.uCount.core.entityBeans.Contestant;
import org.BinghamTSA.uCount.core.entityBeans.Poll;
import org.BinghamTSA.uCount.core.entityBeans.QPoll;
import org.BinghamTSA.uCount.core.entityBeans.VoteGroup;
import org.BinghamTSA.uCount.core.utilities.PollLogger;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPAUpdateClause;

@Stateless
public class PollService extends Service<Poll> {
	QPoll pollTable = QPoll.poll;

	public PollService() {
		this.type = Poll.class;
	}

	public Poll getEnabledPoll() {
		return new JPAQuery(entityManager).from(pollTable).where(pollTable.enabled.isTrue()).singleResult(pollTable);
	}

	public void disableAllPolls() {
		new JPAUpdateClause(entityManager, pollTable).set(pollTable.enabled, false).where(pollTable.enabled.isTrue())
				.execute();
	}

	public void enablePoll(Poll poll) {
		disableAllPolls();

		if (poll != null) {
			PollLogger.logVerbose("Setting Enabled Poll to " + poll.getName());
			poll.setEnabled(true);
			merge(poll);
		} else {
			PollLogger.logVerbose("Clearing Enabled Poll.");
		}
	}

	@Override
	public void delete(int id) {
		Poll poll = get(id);
		if (poll != null) {
			for (VoteGroup votegroup : poll.getVoteGroups()) {
				for (Contestant contestant : votegroup.getContestants()) {
					ContestantService.deleteContestantImage(contestant.getPictureFileName());
				}
			}
		}
		
		super.delete(id);
	}

	public Map<VoteGroup, Integer> getResults(int pollId) {
		Poll poll = get(pollId);
		if (poll != null) {
			List<VoteGroup> voteGroups = poll.getVoteGroups();
			Map<VoteGroup, Integer> results = new HashMap<>();

			for (VoteGroup voteGroup : voteGroups) {
				results.put(voteGroup, voteGroup.getEntries().size());
			}
			return results;
		}
		return null;
	}

}
