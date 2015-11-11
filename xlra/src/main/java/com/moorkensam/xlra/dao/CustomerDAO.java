package com.moorkensam.xlra.dao;

import java.util.List;

import com.moorkensam.xlra.model.Customer;

public interface CustomerDAO {

	public Customer getCustomerById(long id);

	public Customer createCustomer(Customer customer);

	public Customer updateCustomer(Customer customer);

	public List<Customer> getAllFullCustomers();

	public void deleteCustomer(Customer customer);

	public List<Customer> getAllCustomers();
}
