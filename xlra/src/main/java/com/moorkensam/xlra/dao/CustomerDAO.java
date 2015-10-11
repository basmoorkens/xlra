package com.moorkensam.xlra.dao;

import java.util.List;

import com.moorkensam.xlra.model.BaseCustomer;
import com.moorkensam.xlra.model.FullCustomer;

public interface CustomerDAO {

	public BaseCustomer getCustomerById(long id);

	public BaseCustomer createCustomer(BaseCustomer customer);

	public BaseCustomer updateCustomer(BaseCustomer customer);

	public List<FullCustomer> getAllFullCustomers();

	public void deleteCustomer(BaseCustomer customer);

	public List<BaseCustomer> getAllCustomers();
}
