package com.AptiTekk.Poll.web.controllers;

import java.security.Principal;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.AptiTekk.Poll.core.utilities.BanHelper;
import com.AptiTekk.Poll.core.utilities.PollLogger;

@ManagedBean
@SessionScoped
public class LoginController {

	private final static String BAN_NAME = "Login";

	@PostConstruct
	public void init() {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		username = externalContext.getRequestHeaderMap().get("REMOTE_USER");
	}

	private String username;
	private String password;

	private boolean isBanned = BanHelper.isUserBanned(BAN_NAME);

	public Principal getUser() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		return request.getUserPrincipal();
	}

	public String login() {
		FacesContext context = FacesContext.getCurrentInstance();
		PollLogger.logVerbose("Logging In");
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
					PollLogger.logVerbose("Login Successful - Adding Session Variables");
					context.getExternalContext().getSessionMap().put("user", this.username);
					context.getExternalContext().getSessionMap().put("userRole", "admin");
					BanHelper.unBanUser(BAN_NAME);
					BanHelper.clearFailedAttempts(BAN_NAME);
				}
			} else {
				request.logout();
				context.addMessage(null, new FacesMessage("Login Failed: User is null."));
				return "error";
			}

		} catch (ServletException e) {
			context.addMessage(null, new FacesMessage("Login Failed: Incorrect Credentials."));
			BanHelper.recordFailedAttempt(BAN_NAME);
			if (BanHelper.getNumberFailedAttempts(BAN_NAME) >= 3) {
				BanHelper.banUser(BAN_NAME, 6);
				setBanned(true);
			}
			return "error";
		}
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

	public boolean getBanned() {
		return isBanned;
	}

	public void setBanned(boolean isBanned) {
		this.isBanned = isBanned;
	}

}
