package com.AptiTekk.Poll.web.controllers;

import java.io.IOException;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.AptiTekk.Poll.core.EntryService;
import com.AptiTekk.Poll.core.PollService;
import com.AptiTekk.Poll.web.ViewModels.ResultViewModel;
import com.google.gson.Gson;

/**
 * Servlet implementation class ResultsProvider
 */
@WebServlet("/ResultsProvider")
public class ResultsProvider extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@EJB
	PollService pollService;

	public ResultsProvider() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<ResultViewModel> results = ResultViewModel
				.toViewModels(pollService.getResults(pollService.getEnabledPoll().getId()));

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
