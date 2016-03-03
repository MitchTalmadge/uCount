package org.BinghamTSA.uCount.core.entityBeans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Properties")
public class Property {

  @Id
  @GeneratedValue
  private int id;

  @Column(unique = true, nullable = false)
  private String name;

  private String value;

  public Property() {

  }

  public Property(String key, String value) {
    this.setName(name);
    this.setValue(value);
  }
  
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

}
