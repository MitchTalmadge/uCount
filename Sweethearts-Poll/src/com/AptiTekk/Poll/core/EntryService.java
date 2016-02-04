package com.AptiTekk.Poll.core;

import javax.ejb.Stateless;

import com.AptiTekk.Poll.core.entityBeans.Entry;
import com.AptiTekk.Poll.core.entityBeans.Poll;
import com.AptiTekk.Poll.core.entityBeans.QEntry;
import com.mysema.query.jpa.impl.JPAQuery;

@Stateless
public class EntryService extends Service<Entry> {
	QEntry entryTable = QEntry.entry;

	public EntryService() {
		this.type = Entry.class;
	}

	public boolean hasStudentVoted(int studentId, int pollId) {
		Entry entry = new JPAQuery(entityManager).from(entryTable)
				.where(entryTable.credential.studentNumber.eq(studentId).and(entryTable.poll.id.eq(pollId)))
				.singleResult(entryTable);
		return entry != null;
	}

}
