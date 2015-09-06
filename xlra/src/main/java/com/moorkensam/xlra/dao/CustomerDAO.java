package com.moorkensam.xlra.dao;

import java.util.List;

import com.moorkensam.xlra.model.Customer;

public interface CustomerDAO {

	public void createCustomer(Customer customer);

	public void updateCustomer(Customer customer);

	public List<Customer> getAllCustomers();

}
