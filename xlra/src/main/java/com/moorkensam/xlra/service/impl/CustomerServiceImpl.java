package com.moorkensam.xlra.service.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.moorkensam.xlra.dao.CustomerDAO;
import com.moorkensam.xlra.model.BaseCustomer;
import com.moorkensam.xlra.model.FullCustomer;
import com.moorkensam.xlra.service.CustomerService;

@Stateless
public class CustomerServiceImpl implements CustomerService {

	@Inject
	private CustomerDAO customerDAO; 
	
	private final static Logger logger = LogManager.getLogger();
	
	@Override
	public void createCustomer(FullCustomer customer) {
		logger.info("Creating new customer with name " + customer.getName());
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
		logger.info("Deleting customer with id " + customer.getId());
		customerDAO.deleteCustomer(customer);
	}

}
