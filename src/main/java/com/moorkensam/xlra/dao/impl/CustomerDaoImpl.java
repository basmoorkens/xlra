package com.moorkensam.xlra.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.primefaces.model.SortOrder;

import com.moorkensam.xlra.dao.BaseDao;
import com.moorkensam.xlra.dao.CustomerDao;
import com.moorkensam.xlra.model.customer.Customer;

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

  @Override
  public List<Customer> getLazyCustomers(int first, int pageSize, String sortField,
      SortOrder sortOrder, Map<String, Object> filters) {

    Query query = getEntityManager().createNamedQuery("Customer.findAll");
    if (first >= 0) {
      query.setFirstResult(first);
    }
    query.setMaxResults(pageSize);

    return (List<Customer>) query.getResultList();
  }

  @Override
  public int countCustomers(int first, int pageSize, String sortField, SortOrder sortOrder,
      Map<String, Object> filters) {
    StringBuilder builder = new StringBuilder();
    builder.append("SELECT count(c.id) FROM Customer c ");
    Query query = getEntityManager().createQuery(builder.toString());
    Long result = (Long) query.getSingleResult();
    return Integer.parseInt(result + "");
  }

}
