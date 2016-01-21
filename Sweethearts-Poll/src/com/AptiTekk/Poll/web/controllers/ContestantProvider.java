package com.AptiTekk.Poll.web.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.AptiTekk.Poll.core.ContestantService;
import com.AptiTekk.Poll.core.PollService;
import com.AptiTekk.Poll.core.ViewModelConverter;
import com.AptiTekk.Poll.core.entityBeans.Contestant;
import com.AptiTekk.Poll.web.ViewModels.ContestantViewModel;
import com.AptiTekk.Poll.web.ViewModels.PollViewModel;
import com.google.gson.Gson;

/**
 * Servlet implementation class ContestantProvider
 */
@WebServlet("/ContestantProvider")
public class ContestantProvider extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@Inject
	private PollService pollService;
	@Inject
	private ContestantService contestantService;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ContestantProvider() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		return;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String reply = "";
		
		switch(Integer.parseInt(request.getParameter("request"))) {
		case 0: // REQUESTED ONE CONTESTANT
			ContestantViewModel givenContestant = new Gson().fromJson(request.getParameter("obj"), ContestantViewModel.class);
			Contestant contestant = contestantService.get(givenContestant.getId());
			ContestantViewModel fullContestant = ViewModelConverter.toViewModel(contestant);
			reply = new Gson().toJson(fullContestant, ContestantViewModel.class);
			break;
		case 1: // REQUESTED ALL CONTESTANTS UNDER POLL
			PollViewModel givenPoll = new Gson().fromJson(request.getParameter("obj"), PollViewModel.class);
			List<ContestantViewModel> l = new ArrayList<>();
			List<Contestant> list = contestantService.getContestantsByPoll(pollService.get(givenPoll.getId()));
			l.addAll(ViewModelConverter.toContestantViewModels(list));
			reply = new Gson().toJson(l);
			break;
		default:
			throw new ServletException("Incorrect request type");
		}
		System.out.println(reply);

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.write(reply);
	}

}
