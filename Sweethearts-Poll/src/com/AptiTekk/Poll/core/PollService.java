package com.AptiTekk.Poll.core;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;

import com.AptiTekk.Poll.core.entityBeans.Poll;
import com.AptiTekk.Poll.core.entityBeans.QPoll;

@Singleton
public class PollService extends Service<Poll> {
	private QPoll pollTable = QPoll.poll;

	private List<Poll> polls;
	private Poll enabledPoll;

	public PollService() {
		this.type = Poll.class;
	}

	@PostConstruct
	public void init() {
		this.polls = getAll();
		for (Poll poll : polls) {
			if (poll.isEnabled()) {
				enabledPoll = poll;
				break;
			}
		}
	}

	public void refreshPollsList() {
		this.init();
	}

	public Poll getPollByName(String name) {
		for (Poll poll : polls) {
			if (poll.getName().equals(name))
				return poll;
		}
		return null;
	}

	public Poll getPollById(int id) {
		for (Poll poll : polls) {
			if (poll.getId() == id)
				return poll;
		}
		return null;
	}

	public List<Poll> getPolls() {
		return polls;
	}

	public Poll getEnabledPoll() {
		return enabledPoll;
	}

	public void setEnabledPoll(int pollId) {
		Poll newEnabledPoll = getPollById(pollId);
		if (newEnabledPoll != null) {
			if (this.enabledPoll != null) {
				this.enabledPoll.setEnabled(false);
				merge(enabledPoll);
			}
			newEnabledPoll.setEnabled(true);
			this.enabledPoll = newEnabledPoll;
			merge(newEnabledPoll);
		}
	}

	public void disableAllPolls() {
		System.out.println("Disabling Polls...");
		if (this.enabledPoll != null) {
			this.enabledPoll.setEnabled(false);
			merge(enabledPoll);
			this.enabledPoll = null;
		}
	}

}
