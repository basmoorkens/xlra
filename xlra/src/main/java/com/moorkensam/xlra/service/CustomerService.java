package com.moorkensam.xlra.service;

import java.util.List;

import com.moorkensam.xlra.model.BaseCustomer;
import com.moorkensam.xlra.model.FullCustomer;

public interface CustomerService {

	public BaseCustomer getCustomerById(long id);

	public BaseCustomer createCustomer(BaseCustomer customer);

	public void updateCustomer(BaseCustomer customer);

	public List<FullCustomer> getAllFullCustomers();

	public void deleteCustomer(BaseCustomer customer);

	public List<BaseCustomer> getAllCustomers();

}
