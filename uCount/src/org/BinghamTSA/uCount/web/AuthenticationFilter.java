package org.BinghamTSA.uCount.web;

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

import org.BinghamTSA.uCount.core.utilities.PollLogger;

import io.undertow.server.session.Session;

/**
 * The AuthenticationFilter ensures that users are logged in before being able to access
 * administration pages, and redirects them back to the login page if they attempt to access an
 * admin page while not logged in.
 */
@WebFilter("/AuthenticationFilter")
public class AuthenticationFilter implements Filter {

  private ServletContext context;

  HttpServletRequest httpRequest;
  HttpServletResponse httpResponse;
  HttpSession httpSession;

  public void init(FilterConfig fConfig) throws ServletException {
    this.context = fConfig.getServletContext();
    PollLogger.logVerbose("AuthenticationFilter initialized");
  }

  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    httpRequest = (HttpServletRequest) request;
    httpResponse = (HttpServletResponse) response;
    httpSession = httpRequest.getSession(false);

    String uri = httpRequest.getRequestURI();

    if (uri.contains("/admin")) {
      if (httpSession != null && httpSession.getAttribute("user") != null
          && httpSession.getAttribute("userRole").equals("admin")) {
        chain.doFilter(request, response);
        return;
      } else {
        PollLogger.logVerbose("Unauthorized access request to " + uri);
        PollLogger.logVerbose("Redirecting to: " + context.getContextPath() + "/login.xhtml");
        httpResponse.sendRedirect(context.getContextPath() + "/login.xhtml");
      }
    } else if (uri.contains("login.xhtml")) {
      if (httpSession != null && httpSession.getAttribute("user") != null
          && httpSession.getAttribute("userRole").equals("admin")) {
        this.context.log("User tried to access " + uri + " but was already logged in.");
        PollLogger
            .logVerbose("Redirecting to: " + context.getContextPath() + "/admin/manage.xhtml");
        httpResponse.sendRedirect(context.getContextPath() + "/admin");
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
