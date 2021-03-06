package org.BinghamTSA.uCount.web.controllers;

import java.io.IOException;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.BinghamTSA.uCount.core.PollService;
import org.BinghamTSA.uCount.core.entityBeans.Poll;
import org.BinghamTSA.uCount.web.ViewModels.ResultViewModel;

import com.google.gson.Gson;

/**
 * The ResultsProvider is a Servlet that is only accessible as an administrator, and provides the
 * results for the enabled Poll. It is used in the JavaScript of the live results page.
 */
@WebServlet("/admin/ResultsProvider")
public class ResultsProvider extends HttpServlet {
  private static final long serialVersionUID = 1L;

  @EJB
  PollService pollService;

  public ResultsProvider() {
    super();
  }

  /**
   * Gathers the results of the enabled Poll and writes it to the response stream
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    List<ResultViewModel> results = null;

    Poll enabledPoll = pollService.getEnabledPoll();

    if (enabledPoll != null)
      results = ResultViewModel.toViewModels(pollService.getResults(enabledPoll.getId()));

    String json = new Gson().toJson(results != null ? results : "");

    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write(json);
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    doGet(request, response);
  }

}
