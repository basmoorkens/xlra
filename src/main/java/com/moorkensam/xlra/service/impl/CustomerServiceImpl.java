package com.moorkensam.xlra.service.impl;

import com.moorkensam.xlra.dao.CustomerDao;
import com.moorkensam.xlra.model.customer.Customer;
import com.moorkensam.xlra.model.customer.CustomerContact;
import com.moorkensam.xlra.model.error.XlraValidationException;
import com.moorkensam.xlra.service.CustomerService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.model.SortOrder;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;

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

  private static final Logger logger = LogManager.getLogger();

  @Override
  public void createCustomer(Customer customer) throws XlraValidationException {
    logger.info("Creating new customer with name " + customer.getName());
    validateCustomer(customer);
    getCustomerDao().createCustomer(customer);
  }

  private void validateCustomer(Customer customer) throws XlraValidationException {
    try {
      customerDao.getCustomerByName(customer.getName());
      logger.info("Validate customer failed, customer with name " + customer.getName()
          + " already exists.");
      XlraValidationException exc =
          new XlraValidationException("message.customer.create.name.already.exists");
      exc.setExtraArguments(Arrays.asList(customer.getName()));
      throw exc;
    } catch (NoResultException nre) {
      // nothing needed here
    }
  }

  @Override
  public Customer updateCustomer(Customer customer) {
    return getCustomerDao().updateCustomer(customer);
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
  public int countCustomers() {
    return customerDao.countCustomers();
  }

  @Override
  public Customer createCustomerAndReturnManaged(Customer customer) throws XlraValidationException {
    createCustomer(customer);
    return customerDao.getCustomerByName(customer.getName());
  }

  @Override
  public CustomerContact getCustomerContactById(Long id) {
    return customerDao.getCustomerContactById(id);
  }

  @Override
  public List<Customer> findCustomersLikeName(String name) {
    return customerDao.findCustomersLikeName(name);
  }

}
