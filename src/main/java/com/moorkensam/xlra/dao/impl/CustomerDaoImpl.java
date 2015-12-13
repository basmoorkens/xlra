package com.moorkensam.xlra.dao.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

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

}
