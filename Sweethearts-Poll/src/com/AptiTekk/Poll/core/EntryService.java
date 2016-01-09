package com.AptiTekk.Poll.core;

import javax.ejb.Stateless;

import com.AptiTekk.Poll.core.entityBeans.Entry;

@Stateless
public class EntryService extends Service<Entry> {
	
	public EntryService() {
		this.type = Entry.class;
	}

}
