package com.moorkensam.xlra.service.util;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.inject.annotation.TestedObject;

import com.moorkensam.xlra.model.customer.Customer;

public class CustomerUtilTest extends UnitilsJUnit4 {

	@TestedObject
	private CustomerUtil customerUtil;

	private Customer customer;

	@Before
	public void init() {
		customerUtil = CustomerUtil.getInstance();
	}

	@Test
	public void test() {
		customer = new Customer();
		customerUtil.promoteToFullCustomer(customer);

		Assert.assertTrue(customer.isHasOwnRateFile());
		Assert.assertNotNull(customer.getAddress());
	}

	@Test
	public void testNullAddres() {
		customer = new Customer();
		customer.setAddress(null);
		customerUtil.promoteToFullCustomer(customer);

		Assert.assertTrue(customer.isHasOwnRateFile());
		Assert.assertNotNull(customer.getAddress());
	}
}
