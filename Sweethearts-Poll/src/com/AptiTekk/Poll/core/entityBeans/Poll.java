package com.AptiTekk.Poll.core.entityBeans;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Poll {

	@Id
	@GeneratedValue
	private int id;

	@OneToMany(mappedBy = "poll", cascade = CascadeType.ALL)
	private Set<VoteGroup> voteGroups;

	@OneToMany(mappedBy = "poll", cascade = CascadeType.ALL)
	private Set<Entry> entries;

	@Column(length = 128)
	private String name;

	@Column(length = 512)
	private String description;

	public Poll(String name, String description) {
		setName(name);
		setDescription(description);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Set<VoteGroup> getVoteGroups() {
		return voteGroups;
	}

	public void setVoteGroups(Set<VoteGroup> voteGroups) {
		this.voteGroups = voteGroups;
	}

	public Set<Entry> getEntries() {
		return entries;
	}

	public void setEntries(Set<Entry> entries) {
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

}
