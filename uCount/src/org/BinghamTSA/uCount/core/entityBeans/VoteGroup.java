package org.BinghamTSA.uCount.core.entityBeans;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * A database Entity bean that stores data about a VoteGroup
 */
@Entity
@Table(name = "VoteGroup")
public class VoteGroup {

  @Id
  @GeneratedValue
  private int id;

  @ManyToOne
  @JoinColumn(name = "pollId")
  private Poll poll;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "voteGroup", cascade = CascadeType.ALL,
      orphanRemoval = true)
  private List<Contestant> contestants;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "voteGroup", cascade = CascadeType.ALL,
      orphanRemoval = true)
  private List<Entry> entries;

  @Column(length = 128)
  private String name;

  @Column(length = 64)
  private String pictureFileName = "";

  public VoteGroup() {

  }

  public VoteGroup(Poll poll, String name) {
    setPoll(poll);
    setName(name);
  }

  public VoteGroup(Poll poll, String name, String imageFileName) {
    setPoll(poll);
    setName(name);
    setPictureFileName(imageFileName);
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public List<Contestant> getContestants() {
    return contestants;
  }

  public void setContestants(List<Contestant> contestants) {
    this.contestants = contestants;
  }

  public Poll getPoll() {
    return poll;
  }

  public void setPoll(Poll poll) {
    this.poll = poll;
  }

  public List<Entry> getEntries() {
    return entries;
  }

  public void setEntries(List<Entry> entries) {
    this.entries = entries;
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

  /**
   * Determines whether or not this VoteGroup has a picture.
   * @return True if it has a picture, false if it doesn't.
   */
  public boolean hasPicture() {
    return this.pictureFileName != null && !this.pictureFileName.isEmpty();
  }

  @Override
  public boolean equals(Object other) {
    return (other != null && other instanceof VoteGroup && ((VoteGroup) other).getId() == id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode() + super.hashCode() + id;
  }
}
