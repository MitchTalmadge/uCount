package com.AptiTekk.Poll.core.entityBeans;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Entry {

	@Id
	@GeneratedValue
	private int id;

	@ManyToOne
	@JoinColumn(name = "credentialsId")
	private Credential credential;

	@ManyToOne
	@JoinColumn(name = "voteGroupId")
	private VoteGroup voteGroup;

	@ManyToOne
	@JoinColumn(name = "pollId")
	private Poll poll;

	public Entry(Credential credential, VoteGroup voteGroup, Poll poll) {
		setCredential(credential);
		setVoteGroup(voteGroup);
		setPoll(poll);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Credential getCredential() {
		return credential;
	}

	public void setCredential(Credential credential) {
		this.credential = credential;
	}

	public VoteGroup getVoteGroup() {
		return voteGroup;
	}

	public void setVoteGroup(VoteGroup voteGroup) {
		this.voteGroup = voteGroup;
	}

	public Poll getPoll() {
		return poll;
	}

	public void setPoll(Poll poll) {
		this.poll = poll;
	}

}
