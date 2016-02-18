package com.AptiTekk.Poll.core;

import java.util.List;

import javax.ejb.Stateless;

import com.AptiTekk.Poll.core.entityBeans.Property;
import com.AptiTekk.Poll.core.entityBeans.QProperty;
import com.mysema.query.jpa.impl.JPAQuery;

@Stateless
public class PropertyService extends Service<Property> {
  QProperty propertiesTable = QProperty.property;

  public PropertyService() {
    this.type = Property.class;
  }

  public Property findPropertyIfExists(String key) {
    try {
      return new JPAQuery(entityManager).from(propertiesTable).where(propertiesTable.key.eq(key))
          .singleResult(propertiesTable);
    } catch (Exception e) {
      return null;
    }
  }

  public String getString(String key) {
    Property property = findPropertyIfExists(key);
    if (property != null)
      return property.getValue();
    return null;
  }

  public int getInt(String key) {
    Property property = findPropertyIfExists(key);
    if (property != null)
      return Integer.parseInt(property.getValue());
    return -1;
  }

  public void set(String key, String value) {
    Property property = findPropertyIfExists(key);
    if (property != null) {
      property.setValue(value);
      merge(property);
      return;
    }
    Property newProperty = new Property(key, value);
    insert(newProperty);
  }

}
