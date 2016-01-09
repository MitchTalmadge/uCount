package com.AptiTekk.Poll.web;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.AptiTekk.Poll.core.EntryService;
import com.AptiTekk.Poll.core.entityBeans.Entry;

@ManagedBean
@RequestScoped
public class EntryController {
	
	private EntryService service;

	@PostConstruct
	public void init() {
		try {
			service = ((EntryService) new InitialContext().lookup("java:global/Sweethearts-Poll/EntryService"));
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	public String addDummyEntry() {
		service.insert(new Entry("student", 0, 0));
		return "index";
	}

}
