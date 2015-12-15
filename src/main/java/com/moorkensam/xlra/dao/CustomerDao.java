package com.moorkensam.xlra.dao;

import java.util.List;
import java.util.Map;

import org.primefaces.model.SortOrder;

import com.moorkensam.xlra.model.customer.Customer;

public interface CustomerDao {

  public Customer getCustomerById(long id);

  public Customer createCustomer(Customer customer);

  public Customer updateCustomer(Customer customer);

  public List<Customer> getAllFullCustomers();

  public void deleteCustomer(Customer customer);

  public List<Customer> getAllCustomers();

  public List<Customer> getLazyCustomers(int first, int pageSize, String sortField,
      SortOrder sortOrder, Map<String, Object> filters);

  public int countCustomers(int first, int pageSize, String sortField, SortOrder sortOrder,
      Map<String, Object> filters);
}
