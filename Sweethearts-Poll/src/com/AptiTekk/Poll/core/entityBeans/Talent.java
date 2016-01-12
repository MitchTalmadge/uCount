package com.AptiTekk.Poll.core.entityBeans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Talent {

	@Id
	@GeneratedValue
	private int id;

	@OneToOne
	@JoinColumn(name = "voteGroupId")
	private VoteGroup voteGroup;

	@Column(length = 64)
	private String talentName;

	public Talent(VoteGroup voteGroup, String talentName) {
		setVoteGroup(voteGroup);
		setTalentName(talentName);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public VoteGroup getVoteGroup() {
		return voteGroup;
	}

	public void setVoteGroup(VoteGroup voteGroup) {
		this.voteGroup = voteGroup;
	}

	public String getTalentName() {
		return talentName;
	}

	public void setTalentName(String talentName) {
		this.talentName = talentName;
	}

}
