package com.AptiTekk.Poll.core.entityBeans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.sun.faces.ext.validator.CreditCardValidator;

@Entity
public class Credential {

	@Id
	@GeneratedValue
	private int id;

	private int studentNumber;

	@Column(length = 16)
	private String studentUsername;
	
	public Credential() {
		
	}

	public Credential(int studentNumber, String studentUsername) {
		setStudentNumber(studentNumber);
		setStudentUsername(studentUsername);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getStudentNumber() {
		return studentNumber;
	}

	public void setStudentNumber(int studentNumber) {
		this.studentNumber = studentNumber;
	}

	public String getStudentUsername() {
		return studentUsername;
	}

	public void setStudentUsername(String studentUsername) {
		this.studentUsername = studentUsername;
	}

}