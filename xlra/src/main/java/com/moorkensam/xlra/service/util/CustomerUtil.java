package com.moorkensam.xlra.service.util;

import com.moorkensam.xlra.model.Address;
import com.moorkensam.xlra.model.Customer;

public class CustomerUtil {

	private static CustomerUtil instance;

	public static CustomerUtil getInstance() {
		if (instance == null) {
			instance = new CustomerUtil();
		}
		return instance;
	}

	public Customer promoteToFullCustomer(Customer customer) {
		customer.setHasOwnRateFile(true);
		if (customer.getAddress() == null) {
			customer.setAddress(new Address());
		}
		return customer;
	}

	private CustomerUtil() {

	}

}
