package com.AptiTekk.Poll.core;

import java.util.List;

import javax.ejb.Singleton;

import com.AptiTekk.Poll.core.entityBeans.Contestant;
import com.AptiTekk.Poll.core.entityBeans.Poll;
import com.AptiTekk.Poll.core.entityBeans.QContestant;
import com.mysema.query.jpa.impl.JPAQuery;

@Singleton
public class ContestantService extends Service<Contestant> {
	QContestant contestantTable = QContestant.contestant;

	public ContestantService() {
		this.type = Contestant.class;
	}

	public List<Contestant> getContestantsByPoll(Poll poll) {
		return new JPAQuery(entityManager).from(contestantTable)
				.where(contestantTable.voteGroup.poll.name.eq(poll.getName())).list(contestantTable);
	}

}
