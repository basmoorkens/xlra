package com.moorkensam.xlra.service;

import com.moorkensam.xlra.model.customer.Customer;
import com.moorkensam.xlra.model.customer.CustomerContact;
import com.moorkensam.xlra.model.error.XlraValidationException;

import org.primefaces.model.SortOrder;

import java.util.List;
import java.util.Map;

public interface CustomerService {

  public Customer getCustomerById(long id);

  public void createCustomer(Customer customer) throws XlraValidationException;

  public Customer updateCustomer(Customer customer);

  public void deleteCustomer(Customer customer);

  public List<Customer> getAllCustomers();

  public int countCustomers();

  public Customer createCustomerAndReturnManaged(Customer customer) throws XlraValidationException;

  public List<Customer> getLazyCustomers(int first, int pageSize, String sortField,
      SortOrder sortOrder, Map<String, Object> filters);

  public CustomerContact getCustomerContactById(Long id);

  public List<Customer> findCustomersLikeName(String name);
}
