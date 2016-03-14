package org.BinghamTSA.uCount.core;

import javax.ejb.Stateless;

import org.BinghamTSA.uCount.core.entityBeans.Credential;
import org.BinghamTSA.uCount.core.entityBeans.QCredential;

import com.mysema.query.jpa.impl.JPADeleteClause;
import com.mysema.query.jpa.impl.JPAQuery;

/**
 * The CredentialService provides methods for accessing and modifying Credential database objects
 * using JPA.
 */
@Stateless
public class CredentialService extends Service<Credential> {
  QCredential credentialTable = QCredential.credential;

  public CredentialService() {
    this.type = Credential.class;
  }

  /**
   * Returns a Credential when given a student ID, or null if one does not exist.
   * 
   * @param studentId The student ID of the Credential.
   * @return The Credential object that is assigned to the given student ID.
   */
  public Credential getByStudentId(int studentId) {
    Credential credential = new JPAQuery(entityManager).from(credentialTable)
        .where(credentialTable.studentId.eq(studentId)).singleResult(credentialTable);
    return credential;
  }

  public void deleteAllCredentials() {
    new JPADeleteClause(entityManager, credentialTable).execute();
  }

}
