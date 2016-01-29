package com.AptiTekk.Poll.web.controllers;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class ContestantController {

	private int id;
	
	public String submit() {
	    return "modifyContestant?faces-redirect=true&includeViewParams=true";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
