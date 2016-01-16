package com.AptiTekk.Poll.core;

import java.util.List;

import javax.ejb.Stateless;

import com.AptiTekk.Poll.core.entityBeans.Poll;
import com.AptiTekk.Poll.core.entityBeans.QVoteGroup;
import com.AptiTekk.Poll.core.entityBeans.VoteGroup;
import com.mysema.query.jpa.impl.JPAQuery;

@Stateless
public class VoteGroupService extends Service<VoteGroup> {
	private QVoteGroup voteGroupTable = QVoteGroup.voteGroup;

	public VoteGroupService() {
		this.type = VoteGroup.class;
	}

	public List<VoteGroup> getVoteGroupsFromPoll(Poll poll) {
		return new JPAQuery(entityManager).from(voteGroupTable).where(voteGroupTable.poll.eq(poll)).list(voteGroupTable);
	}

}
