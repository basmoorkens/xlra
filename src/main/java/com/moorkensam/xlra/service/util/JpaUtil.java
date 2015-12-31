package com.moorkensam.xlra.service.util;

import org.primefaces.model.SortOrder;

import java.util.Map;

import javax.persistence.Query;

public class JpaUtil {

  /**
   * Convert a primefaces sort order to the directive that can be used in a jpql query.
   * 
   * @param sortOrder The sortorder to convert.
   * @return The jpa sortorder.
   */
  public static String convertSortOrderToJpaSortOrder(SortOrder sortOrder) {
    switch (sortOrder) {
      case ASCENDING:
        return "ASC";
      case DESCENDING:
        return "DESC";
      default:
        return "ASC";
    }
  }

  /**
   * Apply pagination to a query.
   * 
   * @param first The first result to fetch.
   * @param pageSize The number of results to fetch.
   * @param query The query to apply to.
   */
  public static void applyPagination(int first, int pageSize, Query query) {
    if (first >= 0) {
      query.setFirstResult(first);
    }
    query.setMaxResults(pageSize);
  }

  /**
   * Fill in the parameter values for a map of filters.
   * 
   * @param filters The map that should be used to fill in values from.
   * @param query The query to set the parameters on.
   */
  public static void fillInParameters(Map<String, Object> filters, Query query) {
    if (filters != null) {
      for (String key : filters.keySet()) {
        query.setParameter(key.replace(".", ""), "%" + filters.get(key) + "%");
      }
    }
  }


}
