package com.AptiTekk.Poll.core;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import com.AptiTekk.Poll.core.entityBeans.Poll;

@Startup
@Singleton
public class PollService extends Service<Poll> {
	private Poll enabledPoll;
	
	public PollService() {
		this.type = Poll.class;
	}
	
	@PostConstruct
	public void init()
	{
		System.out.println("Starting PollService");
		List<Poll> polls = getAll();
		for (Poll poll : polls) {
			if (poll.isEnabled())
			{
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
			System.out.println("Setting Enabled Poll to " + poll.getName());
			poll.setEnabled(true);
			merge(poll);
		} else {
			System.out.println("Clearing Enabled Poll.");
		}
		this.enabledPoll = poll;
	}

	public void refreshEnabledPoll() {
		if(this.enabledPoll != null)
			this.enabledPoll = get(enabledPoll.getId());
		else
			this.enabledPoll = null;
	}

}
