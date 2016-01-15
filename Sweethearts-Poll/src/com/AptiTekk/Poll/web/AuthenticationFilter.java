package com.AptiTekk.Poll.web;

import java.io.IOException;

import javax.ejb.EJB;
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

/**
 * Servlet Filter implementation class AuthenticationFilter
 */
@WebFilter("/AuthenticationFilter")
public class AuthenticationFilter implements Filter {

	private ServletContext context;

	HttpServletRequest currentReq;
	HttpServletResponse currentRes;

	public void init(FilterConfig fConfig) throws ServletException {
		this.context = fConfig.getServletContext();
		this.context.log("AuthenticationFilter initialized");
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		currentReq = (HttpServletRequest) request;
		currentRes = (HttpServletResponse) response;

		String uri = currentReq.getRequestURI();
		// this.context.log("Requested Resource::"+uri);

		if (uri.contains("/admin")) {
			if (currentReq.isUserInRole("admin")) {
				chain.doFilter(request, response);
			}
		} else {
			chain.doFilter(request, response);
			return;
		}
		this.context.log("Unauthorized access request to " + uri);
		currentRes.sendRedirect("/Sweethearts-Poll/login.xhtml");

	}
	public void destroy() {
		// close any resources here
	}

}
