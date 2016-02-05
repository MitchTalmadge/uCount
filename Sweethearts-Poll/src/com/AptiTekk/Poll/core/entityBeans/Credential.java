package com.AptiTekk.Poll.core.entityBeans;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Credential {

  @Id
  @GeneratedValue
  private int id;

  private int studentNumber;

  public Credential() {

  }

  public Credential(int studentNumber) {
    setStudentNumber(studentNumber);
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

}
