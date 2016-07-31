package com.moorkensam.xlra.dao;

import com.moorkensam.xlra.model.customer.Customer;
import com.moorkensam.xlra.model.customer.CustomerContact;

import org.primefaces.model.SortOrder;

import java.util.List;
import java.util.Map;

public interface CustomerDao {

  public Customer getCustomerById(long id);

  public void createCustomer(Customer customer);

  public Customer updateCustomer(Customer customer);

  public List<Customer> getAllFullCustomers();

  public void deleteCustomer(Customer customer);

  public List<Customer> getAllCustomers();

  public List<Customer> getLazyCustomers(int first, int pageSize, String sortField,
      SortOrder sortOrder, Map<String, Object> filters);

  public int countCustomers();

  public Customer getCustomerByName(String name);

  public CustomerContact getCustomerContactById(Long id);
}
