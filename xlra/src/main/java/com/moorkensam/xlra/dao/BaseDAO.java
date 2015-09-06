package com.moorkensam.xlra.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class BaseDAO {

	@PersistenceContext(unitName="xlraPU")
	protected EntityManager em;
	
	protected EntityManager getEntityManager() {
		return em; 
	}
	
}
