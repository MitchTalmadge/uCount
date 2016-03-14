package org.BinghamTSA.uCount.core;

import javax.ejb.Stateless;

import org.BinghamTSA.uCount.core.entityBeans.Entry;
import org.BinghamTSA.uCount.core.entityBeans.QEntry;
import org.BinghamTSA.uCount.core.utilities.Sha256Helper;

import com.mysema.query.jpa.impl.JPADeleteClause;
import com.mysema.query.jpa.impl.JPAQuery;

/**
 * The EntryService provides methods for accessing and modifying Entry database objects using JPA.
 */
@Stateless
public class EntryService extends Service<Entry> {
  QEntry entryTable = QEntry.entry;

  public EntryService() {
    this.type = Entry.class;
  }

  /**
   * Used to find out whether or not a student has already voted (they already have an entry in the
   * given Poll).
   * 
   * @param studentId The student's ID
   * @param pollId The Poll which we are checking the student against.
   * @return True if the student has voted on the given poll; false if they have not.
   */
  public boolean hasStudentVoted(int studentId, int pollId) {
    byte[] hashedStudentId = Sha256Helper.rawToSha(studentId + "");
    Entry entry = new JPAQuery(entityManager).from(entryTable)
        .where(entryTable.hashedStudentId.eq(hashedStudentId).and(entryTable.poll.id.eq(pollId)))
        .singleResult(entryTable);
    return entry != null;
  }

  public void deleteAllPollEntries(int pollId) {
    new JPADeleteClause(entityManager, entryTable).where(entryTable.poll.id.eq(pollId)).execute();
  }

  public void deleteAllVoteGroupEntries(int voteGroupId) {
    new JPADeleteClause(entityManager, entryTable).where(entryTable.voteGroup.id.eq(voteGroupId))
        .execute();
  }

}
