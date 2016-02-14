package com.AptiTekk.Poll.web.controllers;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.AptiTekk.Poll.core.CredentialService;
import com.AptiTekk.Poll.core.entityBeans.Credential;

@ManagedBean
@ViewScoped
public class CredentialsController {

	@EJB
	CredentialService credentialService;

	private List<Credential> credentials;
	
	@PostConstruct
	public void init() {
		credentials = credentialService.getAll();
	}

	public void deleteCredential(Credential credential) {
		if (credential != null) {
			credentialService.delete(credential.getId());
		}
	}

	public void deleteAllCredentials() {
		credentialService.deleteAllCredentials();
	}

	public List<Credential> getCredentials() {
		return credentials;
	}

	public void setCredentials(List<Credential> credentials) {
		this.credentials = credentials;
	}

}
