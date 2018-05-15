package org.BinghamTSA.uCount.core.entityBeans;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * A database Entity bean that stores data about a Credential
 */
@Entity
@Table(name = "Credential")
public class Credential {

  @Id
  @GeneratedValue
  private int id;

  private int studentId;

  public Credential() {

  }

  public Credential(int studentId) {
    setStudentId(studentId);
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getStudentId() {
    return studentId;
  }

  public void setStudentId(int studentId) {
    this.studentId = studentId;
  }

  @Override
  public boolean equals(Object other) {
    return (other != null && other instanceof Credential && ((Credential) other).getId() == id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode() + super.hashCode() + id;
  }
}
