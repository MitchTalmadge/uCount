package com.AptiTekk.Poll.core.entityBeans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.AptiTekk.Poll.core.Service;

@Entity
public class Contestant {

	@Id
	@GeneratedValue
	private int id;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "voteGroupId")
	private VoteGroup voteGroup;

	@Column(length = 32)
	private String firstName;

	@Column(length = 32)
	private String lastName;

	@Column(length = 64)
	private String pictureFileName = Service.NOTFOUND_IMAGE_FILENAME;
	
	public Contestant() {
		
	}

	public Contestant(VoteGroup voteGroup, String firstName, String lastName, String pictureFileName) {
		setVoteGroup(voteGroup);
		setFirstName(firstName);
		setLastName(lastName);
		setPictureFileName(pictureFileName);
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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPictureFileName() {
		return pictureFileName;
	}

	public void setPictureFileName(String pictureFileName) {
		this.pictureFileName = pictureFileName;
	}

}
