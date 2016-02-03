package com.AptiTekk.Poll.core;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Stateful;

import com.AptiTekk.Poll.core.entityBeans.Poll;
import com.AptiTekk.Poll.core.entityBeans.QPoll;

@Singleton
public class PollService extends Service<Poll> {
	private QPoll pollTable = QPoll.poll;

	public PollService() {
		this.type = Poll.class;
	}

	public Poll getEnabledPoll() {
		List<Poll> polls = getAll();
		for (Poll poll : polls) {
			if (poll.isEnabled())
				return poll;
		}
		return null;
	}

	public void enablePoll(Poll poll) {
		Poll currentEnabledPoll = getEnabledPoll();
		if (currentEnabledPoll != null) {
			currentEnabledPoll.setEnabled(false);
			merge(currentEnabledPoll);
		}
		if (poll != null) {
			System.out.println("Setting Enabled Poll to " + poll.getName());
			poll.setEnabled(true);
			merge(poll);
		} else {
			System.out.println("Clearing Enabled Poll.");
		}
	}

}
