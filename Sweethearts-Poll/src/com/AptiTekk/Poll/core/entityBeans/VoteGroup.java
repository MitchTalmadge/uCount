package com.AptiTekk.Poll.core.entityBeans;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class VoteGroup {

	@Id
	@GeneratedValue
	private int id;

	@OneToMany(mappedBy = "voteGroup", cascade = CascadeType.ALL)
	private Set<Contestant> contestants;
	
	@OneToOne(mappedBy = "voteGroup", cascade = CascadeType.ALL)
	private Talent talent;

	@ManyToOne
	@JoinColumn(name = "pollId")
	private Poll poll;

	public VoteGroup(Talent talent, Poll poll)
	{
		setTalent(talent);
		setPoll(poll);
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Set<Contestant> getContestants() {
		return contestants;
	}

	public void setContestants(Set<Contestant> contestants) {
		this.contestants = contestants;
	}

	public Talent getTalent() {
		return talent;
	}

	public void setTalent(Talent talent) {
		this.talent = talent;
	}

	public Poll getPoll() {
		return poll;
	}

	public void setPoll(Poll poll) {
		this.poll = poll;
	}

}
