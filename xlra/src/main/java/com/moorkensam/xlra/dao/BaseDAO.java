package com.moorkensam.xlra.dao;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public abstract class BaseDAO {

	@PersistenceContext(unitName = "xlraPU")
	protected EntityManager em;

	protected EntityManager getEntityManager() {
		return em;
	}

	public void fillQueryFromParameterMap(Query query,
			Map<String, Object> parametersMap) {
		for (String key : parametersMap.keySet()) {
			query.setParameter(key, parametersMap.get(key));
		}
	}

}
