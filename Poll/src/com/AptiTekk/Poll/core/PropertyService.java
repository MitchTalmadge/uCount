package com.AptiTekk.Poll.core;

import java.util.List;

import javax.ejb.Stateless;

import com.AptiTekk.Poll.core.entityBeans.Property;
import com.AptiTekk.Poll.core.entityBeans.QProperty;

@Stateless
public class PropertyService extends Service<Property> {
  QProperty propertiesTable = QProperty.property;

  public PropertyService() {
    this.type = Property.class;
  }

  public String getString(String key) {
    List<Property> properties = getAll();
    for (Property property : properties) {
      if (property.getKey().equals(key)) {
        return property.getValue();
      }
    }
    return null;
  }
  
  public int getInt(String key) {
    List<Property> properties = getAll();
    for (Property property : properties) {
      if (property.getKey().equals(key)) {
        return Integer.parseInt(property.getValue());
      }
    }
    return -1;
  }
  
  public void set(String key, String value) {
    List<Property> properties = getAll();
    for (Property property : properties) {
      if (property.getKey().equals(key)) {
        property.setValue(value);
        merge(property);
        return;
      }
    }
    Property newProperty = new Property(key, value);
    insert(newProperty);
  }

}
