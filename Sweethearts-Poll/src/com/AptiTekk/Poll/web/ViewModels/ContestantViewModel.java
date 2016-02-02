package com.AptiTekk.Poll.web.ViewModels;

import javax.servlet.http.Part;

import com.AptiTekk.Poll.core.entityBeans.Poll;
import com.AptiTekk.Poll.core.entityBeans.VoteGroup;

public class ContestantViewModel {
	
	private int id;
	private VoteGroup voteGroup;
	private Poll poll;
	private String firstName;
	private String lastName;
	private String pictureFileName;
	
	private Part pictureUpload;

	public ContestantViewModel() {
		pictureFileName = "notFound.png";
	}
	
	public ContestantViewModel(int id, VoteGroup voteGroup, Poll poll, String firstName, String lastName, String pictureFileName) {
		this.setVoteGroup(voteGroup);
		this.setPollName(poll);
		this.setFirstName(firstName);
		this.setLastName(lastName);
		this.setPictureFileName(pictureFileName);
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

	public Poll getPollName() {
		return poll;
	}

	public void setPollName(Poll poll) {
		this.poll = poll;
	}

	public Part getPictureUpload() {
		return pictureUpload;
	}

	public void setPictureUpload(Part pictureUpload) {
		this.pictureUpload = pictureUpload;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
