package com.AptiTekk.Poll.core;

import javax.ejb.Stateless;

import com.AptiTekk.Poll.core.entityBeans.Entry;
import com.AptiTekk.Poll.core.entityBeans.QEntry;
import com.mysema.query.jpa.impl.JPADeleteClause;
import com.mysema.query.jpa.impl.JPAQuery;

@Stateless
public class EntryService extends Service<Entry> {
	QEntry entryTable = QEntry.entry;

	public EntryService() {
		this.type = Entry.class;
	}

	public boolean hasStudentVoted(int studentNumber, int pollId) {
		Entry entry = new JPAQuery(entityManager).from(entryTable)
				.where(entryTable.studentNumber.eq(studentNumber).and(entryTable.poll.id.eq(pollId)))
				.singleResult(entryTable);
		return entry != null;
	}

	public void deleteAllPollEntries(int pollId) {
		new JPADeleteClause(entityManager, entryTable).where(entryTable.poll.id.eq(pollId)).execute();
	}
	
	public void deleteAllVoteGroupEntries(int voteGroupId) {
		new JPADeleteClause(entityManager, entryTable).where(entryTable.voteGroup.id.eq(voteGroupId)).execute();
	}

}
