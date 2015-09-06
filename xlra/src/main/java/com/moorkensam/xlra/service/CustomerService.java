package com.moorkensam.xlra.service;

import java.util.List;

import com.moorkensam.xlra.model.Customer;

public interface CustomerService {

	public void createCustomer(Customer customer);

	public void updateCustomer(Customer customer);

	public List<Customer> getAllCustomers();
}
