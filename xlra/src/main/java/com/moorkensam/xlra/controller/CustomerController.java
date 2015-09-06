package com.moorkensam.xlra.controller;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import com.moorkensam.xlra.model.Customer;
import com.moorkensam.xlra.service.CustomerService;
import com.moorkensam.xlra.service.TestEJB;

@Named(value = "customerController")
public class CustomerController {

	@Inject
	private CustomerService customerService; 
	
	
	public List<Customer> getCustomers() {
		return customerService.getAllCustomers();
	}
	
	public void insertCustomer() {
		Customer c = new Customer();
		c.setName("bas");
		customerService.createCustomer(c);
	}
}
