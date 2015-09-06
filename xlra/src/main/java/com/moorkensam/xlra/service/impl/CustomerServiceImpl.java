package com.moorkensam.xlra.service.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.moorkensam.xlra.dao.CustomerDAO;
import com.moorkensam.xlra.model.Customer;
import com.moorkensam.xlra.service.CustomerService;

@Stateless
public class CustomerServiceImpl implements CustomerService {

	@Inject
	private CustomerDAO customerDAO; 
	
	@Override
	public void createCustomer(Customer customer) {
		customerDAO.createCustomer(customer);
	}

	@Override
	public void updateCustomer(Customer customer) {
		customerDAO.updateCustomer(customer);
	}

	@Override
	public List<Customer> getAllCustomers() {
		return customerDAO.getAllCustomers();
	}

}
