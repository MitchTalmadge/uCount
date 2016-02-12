package com.AptiTekk.Poll.core;

import java.util.List;

import javax.ejb.Remote;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.AptiTekk.Poll.core.utilities.PollLogger;

@Remote
public abstract class Service<T> {

	@PersistenceContext(unitName = "PollRepo")
	protected EntityManager entityManager;

	protected Class<T> type;

	public static final String NOTFOUND_IMAGE_FILENAME = "notFound.png";
	
	public Service() {

	}

	public void insert(T o) {
		this.entityManager.persist(o);
	}

	public T get(int id) {
		return this.entityManager.find(this.type, id);
	}

	public List<T> getAll() {
		Query query = entityManager.createQuery("SELECT e FROM " + type.getName() + " e");
		return (List<T>) query.getResultList();
	}

	public void update(T newData, int id) {
		T old = entityManager.find(type, id);
		if (old != null) {
			old = newData;
		}
	}

	public void delete(int id) {
		T old = entityManager.find(type, id);
		if (old != null) {
			PollLogger.logVerbose("Deleting " + old.getClass().getSimpleName() + " entity from database with ID: " + id);
			entityManager.remove(merge(old));
		} else
			PollLogger.logError("Tried to delete entity from database with ID: " + id + ", but it was not found!");
	}

	public T merge(T entity) {
		return entityManager.merge(entity);
	}

}