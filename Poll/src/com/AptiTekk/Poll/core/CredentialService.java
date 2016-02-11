package com.AptiTekk.Poll.core;

import javax.ejb.Stateless;

import com.AptiTekk.Poll.core.entityBeans.Credential;
import com.AptiTekk.Poll.core.entityBeans.QCredential;
import com.mysema.query.jpa.impl.JPAQuery;

@Stateless
public class CredentialService extends Service<Credential> {
  QCredential credentialTable = QCredential.credential;

  public CredentialService() {
    this.type = Credential.class;
  }

  public Credential getByStudentNumber(int studentNumber) {
    Credential credential = new JPAQuery(entityManager).from(credentialTable)
        .where(credentialTable.studentNumber.eq(studentNumber)).singleResult(credentialTable);
    return credential;
  }

}
