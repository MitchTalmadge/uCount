package com.AptiTekk.Poll.core;

import javax.ejb.Stateless;

import com.AptiTekk.Poll.core.entityBeans.Credential;
import com.AptiTekk.Poll.core.entityBeans.QCredential;
import com.mysema.query.jpa.impl.JPADeleteClause;
import com.mysema.query.jpa.impl.JPAQuery;

@Stateless
public class CredentialService extends Service<Credential> {
	QCredential credentialTable = QCredential.credential;

	public CredentialService() {
		this.type = Credential.class;
	}

	public Credential getByStudentId(int studentId) {
		Credential credential = new JPAQuery(entityManager).from(credentialTable)
				.where(credentialTable.studentId.eq(studentId)).singleResult(credentialTable);
		return credential;
	}

	public void deleteAllCredentials() {
		new JPADeleteClause(entityManager, credentialTable).execute();
	}

}
