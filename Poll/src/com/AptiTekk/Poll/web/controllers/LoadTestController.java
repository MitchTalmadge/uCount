package com.AptiTekk.Poll.web.controllers;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.AptiTekk.Poll.core.utilities.StudentIDAuthenticator;
import com.AptiTekk.Poll.core.utilities.StudentIDAuthenticator.AuthenticationException;

@ManagedBean
@ViewScoped
public class LoadTestController {

	@EJB
	StudentIDAuthenticator studentIdAuthenticator;
	
	public void authenticate() {
		try {
			studentIdAuthenticator.authenticateIdUsingOverdrive((int) (8220000 + (Math.random() * 9999)));
		} catch (AuthenticationException e) {
		}
	}

}
