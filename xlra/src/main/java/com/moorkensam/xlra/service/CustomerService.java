package com.moorkensam.xlra.service;

import java.util.List;

import com.moorkensam.xlra.model.FullCustomer;

public interface CustomerService {

	public void createCustomer(FullCustomer customer);

	public void updateCustomer(FullCustomer customer);

	public List<FullCustomer> getAllFullCustomers();
}
