package com.AptiTekk.Poll.web.ViewModels;

public class ContestantViewModel {
	
	private int id;
	private int voteGroupId;
	private String pollName;
	private String firstName;
	private String lastName;
	private String pictureFileName;

	public ContestantViewModel() {
	}
	
	public ContestantViewModel(int id, int voteGroupId, String pollName, String firstName, String lastName, String pictureFileName) {
		this.setId(id);
		this.setVoteGroupId(voteGroupId);
		this.setPollName(pollName);
		this.setFirstName(firstName);
		this.setLastName(lastName);
		this.setPictureFileName(pictureFileName);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getVoteGroupId() {
		return voteGroupId;
	}

	public void setVoteGroupId(int voteGroupId) {
		this.voteGroupId = voteGroupId;
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

	public String getPollName() {
		return pollName;
	}

	public void setPollName(String pollName) {
		this.pollName = pollName;
	}

}
