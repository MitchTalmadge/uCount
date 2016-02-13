package com.AptiTekk.Poll.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Singleton;

import com.AptiTekk.Poll.core.entityBeans.Entry;
import com.AptiTekk.Poll.core.entityBeans.QEntry;
import com.AptiTekk.Poll.core.entityBeans.VoteGroup;
import com.mysema.query.jpa.impl.JPADeleteClause;
import com.mysema.query.jpa.impl.JPAQuery;

@Singleton
public class EntryService extends Service<Entry> {
  QEntry entryTable = QEntry.entry;

  public EntryService() {
    this.type = Entry.class;
  }

  public boolean hasStudentVoted(int credentialId, int pollId) {
    Entry entry = new JPAQuery(entityManager).from(entryTable)
        .where(entryTable.credential.id.eq(credentialId).and(entryTable.poll.id.eq(pollId)))
        .singleResult(entryTable);
    return entry != null;
  }

  public void deleteAllEntries(int pollId) {
    new JPADeleteClause(entityManager, entryTable).where(entryTable.poll.id.eq(pollId)).execute();
  }
  
}
