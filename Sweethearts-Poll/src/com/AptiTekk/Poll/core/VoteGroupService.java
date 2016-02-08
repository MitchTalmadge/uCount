package com.AptiTekk.Poll.core;

import java.util.List;

import javax.ejb.Singleton;

import com.AptiTekk.Poll.core.entityBeans.Contestant;
import com.AptiTekk.Poll.core.entityBeans.Poll;
import com.AptiTekk.Poll.core.entityBeans.QVoteGroup;
import com.AptiTekk.Poll.core.entityBeans.VoteGroup;
import com.mysema.query.jpa.impl.JPAQuery;

@Singleton
public class VoteGroupService extends Service<VoteGroup> {
	private QVoteGroup voteGroupTable = QVoteGroup.voteGroup;

	public VoteGroupService() {
		this.type = VoteGroup.class;
	}

	public List<VoteGroup> getVoteGroupsFromPoll(Poll poll) {
		return new JPAQuery(entityManager).from(voteGroupTable).where(voteGroupTable.poll.eq(poll)).list(voteGroupTable);
	}
	
	@Override
	public void delete(int id)
	{
	    VoteGroup voteGroup = get(id);
	    if(voteGroup != null)
	    {
		for(Contestant contestant : voteGroup.getContestants())
		    ContestantService.deleteContestantImage(contestant.getPictureFileName());
	    }
	}

}
