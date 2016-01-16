package com.AptiTekk.Poll.core;

import java.util.List;

import javax.ejb.Remote;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Remote
public abstract class Service<T>{
	
	@PersistenceContext(unitName="PollRepo")
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
		Query query = entityManager.createQuery("SELECT e FROM "+type.getName()+" e");
	    return (List<T>) query.getResultList();
	}
	
	public void update(T newData, int id) {
		T old = entityManager.find(type, id);
		if(old != null) {
			old = newData;
		}
	}
	
	public void delete(int id) {
		T old = entityManager.find(type, id);
		if(old != null) {
			entityManager.remove(old);;
		}
	}

}