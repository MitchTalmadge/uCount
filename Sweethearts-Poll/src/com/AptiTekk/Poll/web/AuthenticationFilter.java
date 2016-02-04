package com.AptiTekk.Poll.web;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import io.undertow.server.session.Session;

/**
 * Servlet Filter implementation class AuthenticationFilter
 */
@WebFilter("/AuthenticationFilter")
public class AuthenticationFilter implements Filter {

	private ServletContext context;

	HttpServletRequest currentReq;
	HttpServletResponse currentRes;
	HttpSession currentSession;

	public void init(FilterConfig fConfig) throws ServletException {
		this.context = fConfig.getServletContext();
		this.context.log("AuthenticationFilter initialized");
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		currentReq = (HttpServletRequest) request;
		currentRes = (HttpServletResponse) response;
		currentSession = currentReq.getSession(false);

		String uri = currentReq.getRequestURI();
		// this.context.log("Requested Resource::"+uri);

		if (uri.contains("/admin")) {
			if (currentSession != null && currentSession.getAttribute("user") != null
					&& currentSession.getAttribute("userRole").equals("admin")) {
				chain.doFilter(request, response);
				return;
			} else {
				this.context.log("Unauthorized access request to " + uri);
				currentRes.sendRedirect("/Sweethearts-Poll/login.xhtml");
			}
		} else if (uri.contains("login.xhtml")) {
			if (currentSession != null && currentSession.getAttribute("user") != null
					&& currentSession.getAttribute("userRole").equals("admin")) {
				this.context.log("User tried to access " + uri + " but was already logged in.");
				currentRes.sendRedirect("/Sweethearts-Poll/admin/manage.xhtml");
				return;
			} else {
				chain.doFilter(request, response);
				return;
			}
		} else {
			chain.doFilter(request, response);
			return;
		}

	}

	public void destroy() {
		// close any resources here
	}

}
