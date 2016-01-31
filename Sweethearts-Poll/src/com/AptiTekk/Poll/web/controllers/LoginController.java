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
		System.out.println("Logging In");
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		try {
			request.login(this.username, this.password);
			password = null;
			if (getUser() != null) {
				if (!request.isUserInRole("admin")) {
					context.addMessage(null, new FacesMessage("Login Failed: User is not an admin."));
					request.logout();
					return "error";
				} else {
					System.out.println("Login Successful - Adding Session Variables");
					context.getExternalContext().getSessionMap().put("user", this.username);
					context.getExternalContext().getSessionMap().put("userRole",
							((request.isUserInRole("admin")) ? "admin" : "user"));
				}
			} else {
				request.logout();
				context.addMessage(null, new FacesMessage("Login Failed: User is null."));
				return "error";
			}

		} catch (ServletException e) {
			context.addMessage(null, new FacesMessage("Login Failed: Incorrect Credentials."));
			return "error";
		}
		/*
		 * The Authentication filter will put the original page they wanted into
		 * a header named ORIGINAL_PAGE if they previously tried to access the
		 * page but weren't authenticated.
		 * 
		 * This is where we send them back to where they wanted
		 * 
		 */
		if (request.getHeader("ORIGINAL_PAGE") != null) {
			HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
			try {
				response.sendRedirect(request.getHeader("ORIGINAL_PAGE"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// context.addMessage(null, new FacesMessage("redirect page was
		// null."));
		return "manage";
	}

	public String logout() {
		System.out.println("Logging Out");
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		try {
			request.logout();
		} catch (ServletException e) {
			context.addMessage(null, new FacesMessage("Logout failed."));
		} finally {
			FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
			username = null;
		}
		return "index";
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
