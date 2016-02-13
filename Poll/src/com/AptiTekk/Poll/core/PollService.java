package com.AptiTekk.Poll.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;

import com.AptiTekk.Poll.core.entityBeans.Contestant;
import com.AptiTekk.Poll.core.entityBeans.Entry;
import com.AptiTekk.Poll.core.entityBeans.Poll;
import com.AptiTekk.Poll.core.entityBeans.VoteGroup;
import com.AptiTekk.Poll.core.utilities.PollLogger;

@Singleton
public class PollService extends Service<Poll> {
	private Poll enabledPoll;

	public PollService() {
		this.type = Poll.class;
	}

	@PostConstruct
	public void init() {
		List<Poll> polls = getAll();
		for (Poll poll : polls) {
			if (poll.isEnabled()) {
				this.enabledPoll = poll;
				break;
			}
		}
	}

	public Poll getEnabledPoll() {
		return this.enabledPoll;
	}

	public void enablePoll(Poll poll) {
		if (this.enabledPoll != null) {
			enabledPoll.setEnabled(false);
			merge(enabledPoll);
		}
		if (poll != null) {
			PollLogger.logVerbose("Setting Enabled Poll to " + poll.getName());
			poll.setEnabled(true);
			merge(poll);
		} else {
			PollLogger.logVerbose("Clearing Enabled Poll.");
		}
		this.enabledPoll = poll;
	}

	public void refreshEnabledPoll() {
		if (this.enabledPoll != null)
			this.enabledPoll = get(enabledPoll.getId());
		else
			this.enabledPoll = null;
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
