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

/**
 * Service to fetch and update customers.
 * 
 * @author bas
 *
 */
@Stateless
public class CustomerServiceImpl implements CustomerService {

	@Inject
	private CustomerDAO customerDAO;

	private final static Logger logger = LogManager.getLogger();

	@Override
	public BaseCustomer createCustomer(BaseCustomer customer) {
		logger.info("Creating new customer with name " + customer.getName());
		return getCustomerDAO().createCustomer(customer);
	}

	@Override
	public void updateCustomer(BaseCustomer customer) {
		getCustomerDAO().updateCustomer(customer);
	}

	@Override
	public List<FullCustomer> getAllFullCustomers() {
		return getCustomerDAO().getAllFullCustomers();
	}

	@Override
	public void deleteCustomer(BaseCustomer customer) {
		logger.info("Deleting customer with id " + customer.getId());
		getCustomerDAO().deleteCustomer(customer);
	}

	@Override
	public BaseCustomer getCustomerById(long id) {
		if (logger.isDebugEnabled()) {
			logger.debug("Fetching customer for id " + id);
		}
		return getCustomerDAO().getCustomerById(id);
	}

	@Override
	public List<BaseCustomer> getAllCustomers() {
		return getCustomerDAO().getAllCustomers();
	}

	public CustomerDAO getCustomerDAO() {
		return customerDAO;
	}

	public void setCustomerDAO(CustomerDAO customerDAO) {
		this.customerDAO = customerDAO;
	}

}
