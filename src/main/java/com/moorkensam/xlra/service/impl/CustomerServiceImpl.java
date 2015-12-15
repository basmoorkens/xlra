package com.moorkensam.xlra.service.impl;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.model.SortOrder;

import com.moorkensam.xlra.dao.CustomerDao;
import com.moorkensam.xlra.model.customer.Customer;
import com.moorkensam.xlra.service.CustomerService;

/**
 * Service to fetch and update customers.
 * 
 * @author bas
 *
 */
@Stateless
public class CustomerServiceImpl implements CustomerService {

  @Inject
  private CustomerDao customerDao;

  private final static Logger logger = LogManager.getLogger();

  @Override
  public Customer createCustomer(Customer customer) {
    logger.info("Creating new customer with name " + customer.getName());
    return getCustomerDao().createCustomer(customer);
  }

  @Override
  public void updateCustomer(Customer customer) {
    getCustomerDao().updateCustomer(customer);
  }

  @Override
  public List<Customer> getAllFullCustomers() {
    return getCustomerDao().getAllFullCustomers();
  }

  @Override
  public void deleteCustomer(Customer customer) {
    logger.info("Deleting customer with id " + customer.getId());
    getCustomerDao().deleteCustomer(customer);
  }

  @Override
  public Customer getCustomerById(long id) {
    if (logger.isDebugEnabled()) {
      logger.debug("Fetching customer for id " + id);
    }
    return getCustomerDao().getCustomerById(id);
  }

  @Override
  public List<Customer> getAllCustomers() {
    return getCustomerDao().getAllCustomers();
  }

  public CustomerDao getCustomerDao() {
    return customerDao;
  }

  public void setCustomerDao(CustomerDao customerDao) {
    this.customerDao = customerDao;
  }

  @Override
  public List<Customer> getLazyCustomers(int first, int pageSize, String sortField,
      SortOrder sortOrder, Map<String, Object> filters) {
    return customerDao.getLazyCustomers(first, pageSize, sortField, sortOrder, filters);
  }

  @Override
  public int countCustomers(int first, int pageSize, String sortField, SortOrder sortOrder,
      Map<String, Object> filters) {
    return customerDao.countCustomers(first, pageSize, sortField, sortOrder, filters);
  }

}
