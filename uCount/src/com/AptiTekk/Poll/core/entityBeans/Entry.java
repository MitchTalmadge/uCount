package com.AptiTekk.Poll.core.entityBeans;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.AptiTekk.Poll.core.utilities.Sha256Helper;

@Entity
@Table(name="Entry")
public class Entry {

	@Id
	@GeneratedValue
	private int id;

	private byte[] hashedStudentId;

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

	public Entry(int studentId, VoteGroup voteGroup, Poll poll) {
		setHashedStudentId(Sha256Helper.rawToSha(studentId + ""));
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

	public Calendar getSubmitDate() {
		return submitDate;
	}

	public String getFriendlySubmitDate() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		return simpleDateFormat.format(getSubmitDate().getTime());
	}

	public void setSubmitDate(Calendar submitDate) {
		this.submitDate = submitDate;
	}

	public byte[] getHashedStudentId() {
		return hashedStudentId;
	}

	public void setHashedStudentId(byte[] hashedStudentId) {
		this.hashedStudentId = hashedStudentId;
	}

}
