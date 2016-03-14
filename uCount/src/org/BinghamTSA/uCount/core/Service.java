package org.BinghamTSA.uCount.core;

import java.util.List;

import javax.ejb.Remote;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Table;

/**
 * Service provides an interface for all Services to interact with JPA using CRUD operations.
 */
@Remote
public abstract class Service<T> {

  @PersistenceContext(unitName = "uCountRepo")
  protected EntityManager entityManager;

  protected Class<T> type;

  public Service() {

  }

  public void insert(T o) {
    this.entityManager.persist(o);
  }

  public T get(int id) {
    return this.entityManager.find(this.type, id);
  }

  public List<T> getAll() {
    Query query =
        entityManager.createQuery("SELECT e FROM " + ((type.getAnnotation(Table.class) != null)
            ? type.getAnnotation(Table.class).name() : type.getName()) + " e");
    return (List<T>) query.getResultList();
  }

  public void update(T newData, int id) {
    T old = entityManager.find(type, id);
    if (old != null) {
      old = newData;
    }
  }

  public void delete(int id) {
    T entity = entityManager.getReference(type, id);
    if (entity != null)
      entityManager.remove(entity);
  }

  public T merge(T entity) {
    return entityManager.merge(entity);
  }

}
