package com.moorkensam.xlra.dao;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public abstract class BaseDao {

  @PersistenceContext(unitName = "xlraPU")
  protected EntityManager em;

  protected EntityManager getEntityManager() {
    return em;
  }

  /**
   * Fill a query from the parameter map.
   * 
   * @param query The query to fill in.
   * @param parametersMap The parameters to use.
   */
  public void fillQueryFromParameterMap(Query query, Map<String, Object> parametersMap) {
    for (String key : parametersMap.keySet()) {
      query.setParameter(key, parametersMap.get(key));
    }
  }

}
