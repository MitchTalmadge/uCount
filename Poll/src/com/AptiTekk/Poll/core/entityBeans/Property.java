package com.AptiTekk.Poll.core.entityBeans;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Property {

	@Column(unique = true, nullable = false)
	private
	String key;
	
	private String value;
	
	public Property() {
		
	}
	
	public Property(String key, String value) {
		this.setKey(key);
		this.setValue(value);
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
