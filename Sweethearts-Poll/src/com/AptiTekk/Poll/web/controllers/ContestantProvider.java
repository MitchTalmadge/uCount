package com.AptiTekk.Poll.web.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.AptiTekk.Poll.core.Service;
import com.AptiTekk.Poll.core.entityBeans.Contestant;
import com.AptiTekk.Poll.core.entityBeans.ViewModelConverter;
import com.AptiTekk.Poll.web.ViewModels.ContestantViewModel;
import com.google.gson.Gson;

/**
 * Servlet implementation class ContestantProvider
 */
@WebServlet("/ContestantProvider")
public class ContestantProvider extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@EJB(mappedName="ContestantService")
	private Service<Contestant> service;
       
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
		List<ContestantViewModel> l = new ArrayList<>();
		
		List<Contestant> list = service.getAll();
		
		l.addAll(ViewModelConverter.toContestantViewModels(list));

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.write(new Gson().toJson(l));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
