package com.moorkensam.xlra.dao;

import java.util.List;

import com.moorkensam.xlra.model.BaseCustomer;
import com.moorkensam.xlra.model.FullCustomer;

public interface CustomerDAO {

	public FullCustomer getCustomerById(long id);

	public void createCustomer(FullCustomer customer);

	public void updateCustomer(FullCustomer customer);

	public List<FullCustomer> getAllFullCustomers();

	public void deleteCustomer(BaseCustomer customer);

	public List<BaseCustomer> getAllCustomers();
}
