package org.BinghamTSA.uCount.web.controllers;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.BinghamTSA.uCount.core.EntryService;
import org.BinghamTSA.uCount.core.PollService;
import org.BinghamTSA.uCount.core.entityBeans.Entry;
import org.BinghamTSA.uCount.core.entityBeans.Poll;

@ManagedBean
@ViewScoped
public class PollEntriesController {

	@EJB
	PollService pollService;

	@EJB
	EntryService entryService;

	private Poll poll;

	@PostConstruct
	public void init() {
		String pollIdParam = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get("pollId");
		try {
			int pollId = Integer.parseInt(pollIdParam);
			setPoll(pollService.get(pollId));
		} catch (NumberFormatException ignored) {
		}
	}

	public Poll getPoll() {
		return poll;
	}

	public void setPoll(Poll poll) {
		this.poll = poll;
	}

	public void deleteEntry(Entry entry) {
		if (entry != null) {
			entryService.delete(entry.getId());

			poll = pollService.get(poll.getId()); // Refresh poll
		}
	}

	public void deleteAllEntries() {
		entryService.deleteAllPollEntries(poll.getId());

		poll = pollService.get(poll.getId());
	}

}
