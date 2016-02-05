package com.AptiTekk.Poll.core.entityBeans;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class VoteGroup {

	@Id
	@GeneratedValue
	private int id;

	@ManyToOne
	@JoinColumn(name = "pollId")
	private Poll poll;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy = "voteGroup", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Contestant> contestants;

	@OneToMany(fetch=FetchType.LAZY, mappedBy = "voteGroup", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Entry> entries;
	
	@Column(length = 64)
	private String talentName;

	public VoteGroup() {

	}

	public VoteGroup(Poll poll) {
		this.talentName = null;
		setPoll(poll);
	}

	public VoteGroup(Poll poll, String talentName) {
		this.talentName = talentName;
		setPoll(poll);

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Contestant> getContestants() {
		return contestants;
	}

	public void setContestants(List<Contestant> contestants) {
		this.contestants = contestants;
	}

	public Poll getPoll() {
		return poll;
	}

	public void setPoll(Poll poll) {
		this.poll = poll;
	}

	public String getTalentName() {
		return talentName;
	}

	public void setTalentName(String talentName) {
		this.talentName = talentName;
	}

	public List<Entry> getEntries() {
		return entries;
	}

	public void setEntries(List<Entry> entries) {
		this.entries = entries;
	}

}
