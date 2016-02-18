package com.AptiTekk.Poll.core;

import javax.ejb.Stateless;

import com.AptiTekk.Poll.core.entityBeans.Entry;
import com.AptiTekk.Poll.core.entityBeans.QEntry;
import com.AptiTekk.Poll.core.utilities.Sha256Helper;
import com.mysema.query.jpa.impl.JPADeleteClause;
import com.mysema.query.jpa.impl.JPAQuery;

@Stateless
public class EntryService extends Service<Entry> {
	QEntry entryTable = QEntry.entry;

	public EntryService() {
		this.type = Entry.class;
	}

	public boolean hasStudentVoted(int studentNumber, int pollId) {
		byte[] hashedStudentId = Sha256Helper.rawToSha(studentNumber + "");
		Entry entry = new JPAQuery(entityManager).from(entryTable)
				.where(entryTable.hashedStudentId.eq(hashedStudentId).and(entryTable.poll.id.eq(pollId)))
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
