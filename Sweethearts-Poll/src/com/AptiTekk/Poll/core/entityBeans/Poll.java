package com.AptiTekk.Poll.core.entityBeans;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Poll {

	@Id
	@GeneratedValue
	private int id;

	@OneToMany(fetch=FetchType.EAGER, mappedBy = "poll", orphanRemoval = true)
	private List<VoteGroup> voteGroups;

	@OneToMany(mappedBy = "poll", cascade = CascadeType.ALL)
	private List<Entry> entries;

	@Column(length = 128)
	private String name;

	@Column(length = 512)
	private String description;
	
	private Boolean enabled;
	
	public Poll() {
		
	}

	public Poll(String name, String description) {
		this(name, description, false);
	}
	public Poll(String name, String description, boolean enabled) {
		setName(name);
		setDescription(description);
		setEnabled(enabled);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<VoteGroup> getVoteGroups() {
		return voteGroups;
	}

	public void setVoteGroups(List<VoteGroup> voteGroups) {
		this.voteGroups = voteGroups;
	}

	public List<Entry> getEntries() {
		return entries;
	}

	public void setEntries(List<Entry> entries) {
		this.entries = entries;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public boolean equals(Object other)
	{
		return (other != null && other instanceof Poll && ((Poll)other).getId() == id);
	}
	
	@Override
	public int hashCode() {
		return getClass().hashCode() + id;
	}
}
