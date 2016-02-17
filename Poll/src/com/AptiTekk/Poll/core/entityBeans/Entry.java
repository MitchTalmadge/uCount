package com.AptiTekk.Poll.core.entityBeans;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Entry {

	@Id
	@GeneratedValue
	private int id;

	private int studentNumber;

	@Temporal(TemporalType.TIMESTAMP)
	private Calendar submitDate;

	@ManyToOne
	@JoinColumn(name = "voteGroupId")
	private VoteGroup voteGroup;

	@ManyToOne
	@JoinColumn(name = "pollId")
	private Poll poll;

	public Entry() {

	}

	public Entry(int studentNumber, VoteGroup voteGroup, Poll poll) {
		setStudentNumber(studentNumber);
		setVoteGroup(voteGroup);
		setPoll(poll);
		setSubmitDate(Calendar.getInstance());
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

	public Poll getPoll() {
		return poll;
	}

	public void setPoll(Poll poll) {
		this.poll = poll;
	}

	public int getStudentNumber() {
		return studentNumber;
	}

	public void setStudentNumber(int studentNumber) {
		this.studentNumber = studentNumber;
	}

	public Calendar getSubmitDate() {
		return submitDate;
	}

	public void setSubmitDate(Calendar submitDate) {
		this.submitDate = submitDate;
	}

}
