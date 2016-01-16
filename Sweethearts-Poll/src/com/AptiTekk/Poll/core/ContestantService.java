package com.AptiTekk.Poll.core;

import javax.ejb.Stateless;

import com.AptiTekk.Poll.core.entityBeans.Contestant;

@Stateless
public class ContestantService extends Service<Contestant> {
	
	public ContestantService() {
		this.type = Contestant.class;
	}

}
