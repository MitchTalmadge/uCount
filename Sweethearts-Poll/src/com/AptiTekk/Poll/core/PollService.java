package com.AptiTekk.Poll.core;

import javax.ejb.Stateless;

import com.AptiTekk.Poll.core.entityBeans.Poll;
import com.AptiTekk.Poll.core.entityBeans.QPoll;
import com.mysema.query.jpa.impl.JPAQuery;

@Stateless
public class PollService extends Service<Poll> {
	private QPoll pollTable = QPoll.poll;
	
	public PollService() {
		this.type = Poll.class;	
	}
	
	public Poll getPollByName(String name) {
		return new JPAQuery(entityManager).from(pollTable).where(pollTable.name.eq(name)).uniqueResult(pollTable);
	}
	
	public Poll getEnabledPoll() {
		return new JPAQuery(entityManager).from(pollTable).where(pollTable.enabled.eq(true)).uniqueResult(pollTable);
	}

}
