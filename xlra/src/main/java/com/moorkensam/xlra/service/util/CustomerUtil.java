package com.moorkensam.xlra.service.util;

import com.moorkensam.xlra.model.BaseCustomer;
import com.moorkensam.xlra.model.FullCustomer;

public class CustomerUtil {

	private static CustomerUtil instance;

	public static CustomerUtil getInstance() {
		if (instance == null) {
			instance = new CustomerUtil();
		}
		return instance;
	}

	public FullCustomer promoteToFullCustomer(BaseCustomer base) {
		FullCustomer fullCustomer = new FullCustomer(base);
		return fullCustomer;
	}

	private CustomerUtil() {

	}

}
