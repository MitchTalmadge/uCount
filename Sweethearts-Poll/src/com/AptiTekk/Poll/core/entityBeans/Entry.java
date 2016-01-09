package com.AptiTekk.Poll.core.entityBeans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Entry {
	
	@Id @GeneratedValue
	private int id;
	
	@Column(length=32)
	private String studentId;
	private int contestantId;
	private int pollId;

	public Entry(String studentId, int contestantId, int pollId) {
		this.setStudentId(studentId);
		this.setContestantId(contestantId);
		this.setPollId(pollId);
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public int getContestantId() {
		return contestantId;
	}

	public void setContestantId(int contestantId) {
		this.contestantId = contestantId;
	}

	public int getPollId() {
		return pollId;
	}

	public void setPollId(int pollId) {
		this.pollId = pollId;
	}

}
