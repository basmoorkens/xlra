package com.moorkensam.xlra.service;

import java.util.List;

import com.moorkensam.xlra.model.Customer;

public interface CustomerService {

	public Customer getCustomerById(long id);

	public Customer createCustomer(Customer customer);

	public void updateCustomer(Customer customer);

	public List<Customer> getAllFullCustomers();

	public void deleteCustomer(Customer customer);

	public List<Customer> getAllCustomers();

}
