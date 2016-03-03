package org.BinghamTSA.uCount.core.entityBeans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="Contestant")
public class Contestant {

	@Id
	@GeneratedValue
	private int id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "voteGroupId")
	private VoteGroup voteGroup;

	@Column(length = 64)
	private String name;

	@Column(length = 64)
	private String pictureFileName = "";

	public Contestant() {

	}

	public Contestant(VoteGroup voteGroup, String name) {
		setVoteGroup(voteGroup);
		setName(name);
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPictureFileName() {
		return pictureFileName;
	}

	public void setPictureFileName(String pictureFileName) {
		this.pictureFileName = pictureFileName;
	}

	public boolean hasPicture() {
		return this.pictureFileName != null && !this.pictureFileName.isEmpty();
	}

}
