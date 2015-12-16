package com.moorkensam.xlra.dao.impl;

import com.moorkensam.xlra.dao.BaseDao;
import com.moorkensam.xlra.dao.CustomerDao;
import com.moorkensam.xlra.model.customer.Customer;
import com.moorkensam.xlra.service.util.JpaUtil;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.SortOrder;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

public class CustomerDaoImpl extends BaseDao implements CustomerDao {

  public Customer createCustomer(Customer customer) {
    return getEntityManager().merge(customer);
  }

  public Customer updateCustomer(Customer customer) {
    return getEntityManager().merge(customer);
  }

  @SuppressWarnings("unchecked")
  public List<Customer> getAllFullCustomers() {
    Query query = getEntityManager().createNamedQuery("Customer.findAllFullCustomers");
    return (List<Customer>) query.getResultList();
  }

  @Override
  public void deleteCustomer(Customer customer) {
    customer.setDeleted(true);
    customer.setDeletedDateTime(new Date());
    getEntityManager().merge(customer);
  }

  @Override
  public Customer getCustomerById(long id) {
    return getEntityManager().find(Customer.class, id);

  }

  @SuppressWarnings("unchecked")
  @Override
  public List<Customer> getAllCustomers() {
    Query query = getEntityManager().createNamedQuery("Customer.findAll");
    return (List<Customer>) query.getResultList();
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<Customer> getLazyCustomers(int first, int pageSize, String sortField,
      SortOrder sortOrder, Map<String, Object> filters) {
    String queryString = buildLazyLoadCustomersQuery(sortField, sortOrder, filters);
    Query query = getEntityManager().createQuery(queryString);
    JpaUtil.applyPagination(first, pageSize, query);
    JpaUtil.fillInParameters(filters, query);
    return (List<Customer>) query.getResultList();
  }

  /*
   * Build the lazy loading datatable query for customers. PAgination, sorting and filtering are
   * included.
   */
  private String buildLazyLoadCustomersQuery(String sortField, SortOrder sortOrder,
      Map<String, Object> filters) {
    StringBuilder builder = new StringBuilder();
    builder.append("SELECT b FROM Customer b where 1 = 1 ");
    if (filters != null) {
      for (String key : filters.keySet()) {
        builder.append("AND b." + key + " LIKE :" + key + " ");
      }
    }
    if (StringUtils.isNotBlank(sortField)) {
      builder.append("ORDER BY b." + sortField + " "
          + JpaUtil.convertSortOrderToJpaSortOrder(sortOrder));
    }
    return builder.toString();
  }

  @Override
  public int countCustomers() {
    Query query = getEntityManager().createNamedQuery("Customer.countCustomers");
    Long result = (Long) query.getSingleResult();
    return Integer.parseInt(result + "");
  }

}
