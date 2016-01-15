package com.AptiTekk.Poll.web.controllers;

import java.io.IOException;
import java.security.Principal;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ManagedBean
@SessionScoped
public class LoginController {

	@PostConstruct
	public void init() {
		
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		username = externalContext.getRequestHeaderMap().get("REMOTE_USER");

	}

	private String username;
	private String password;
	
	public Principal getUser() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		return request.getUserPrincipal();
	}

	public String login() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		try {
			request.login(this.username, this.password);
		} catch (ServletException e) {
			context.addMessage(null, new FacesMessage("Login failed."));
			return "error";
		}
		/*
		 * The Authentication filter will put the original page they wanted
		 * into a header named ORIGINAL_PAGE if they previously tried to
		 * access the page but weren't authenticated.
		 * 
		 * This is where we send them back to where they wanted
		 * 
		 */
		if(request.getHeader("ORIGINAL_PAGE") != null) {
			HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
			try {
				response.sendRedirect(request.getHeader("ORIGINAL_PAGE"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//context.addMessage(null, new FacesMessage("redirect page was null."));
		return "manage";
	}

	public String logout() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		try {
			request.logout();
		} catch (ServletException e) {
			context.addMessage(null, new FacesMessage("Logout failed."));
		}
		return "login";
	}

	public String getSubmitUsername() {
		return username;
	}

	public void setSubmitUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
