package com.moorkensam.xlra.service.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.moorkensam.xlra.dao.CustomerDAO;
import com.moorkensam.xlra.model.BaseCustomer;
import com.moorkensam.xlra.model.FullCustomer;
import com.moorkensam.xlra.service.CustomerService;

@Stateless
public class CustomerServiceImpl implements CustomerService {

	@Inject
	private CustomerDAO customerDAO; 
	
	@Override
	public void createCustomer(FullCustomer customer) {
		customerDAO.createCustomer(customer);
	}

	@Override
	public void updateCustomer(FullCustomer customer) {
		customerDAO.updateCustomer(customer);
	}

	@Override
	public List<FullCustomer> getAllFullCustomers() {
		return customerDAO.getAllFullCustomers();
	}

	@Override
	public void deleteCustomer(BaseCustomer customer) {
		customerDAO.deleteCustomer(customer);
	}

}
