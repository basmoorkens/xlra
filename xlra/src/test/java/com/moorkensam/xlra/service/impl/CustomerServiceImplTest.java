package com.moorkensam.xlra.service.impl;

import java.util.Arrays;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.TestedObject;

import com.moorkensam.xlra.dao.CustomerDAO;
import com.moorkensam.xlra.model.BaseCustomer;

public class CustomerServiceImplTest extends UnitilsJUnit4 {

	@TestedObject
	private CustomerServiceImpl service;

	@Mock
	private CustomerDAO dao;

	@Before
	public void init() {
		service = new CustomerServiceImpl();
		service.setCustomerDAO(dao);
	}

	@Test
	public void testGetCustomerById() {
		BaseCustomer cust = new BaseCustomer();
		cust.setId(1l);

		EasyMock.expect(dao.getCustomerById(cust.getId())).andReturn(cust);
		EasyMockUnitils.replay();

		service.getCustomerById(1l);
	}

	@Test
	public void testGetAllCustomers() {
		BaseCustomer cust1 = new BaseCustomer();
		BaseCustomer cust2 = new BaseCustomer();
		EasyMock.expect(dao.getAllCustomers()).andReturn(
				Arrays.asList(cust1, cust2));

		EasyMockUnitils.replay();
		service.getAllCustomers();
	}

	@Test
	public void testCreateCustomer() {
		BaseCustomer cust = new BaseCustomer();
		EasyMock.expect(dao.createCustomer(cust)).andReturn(cust);
		EasyMockUnitils.replay();

		service.createCustomer(cust);
	}

	@Test
	public void testDeleteCustomer() {
		BaseCustomer cust = new BaseCustomer();
		dao.deleteCustomer(cust);
		EasyMock.expectLastCall();

		EasyMockUnitils.replay();
		service.deleteCustomer(cust);

	}

}
