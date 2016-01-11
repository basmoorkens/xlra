package com.moorkensam.xlra.service.impl;

import com.moorkensam.xlra.dao.CustomerDao;
import com.moorkensam.xlra.model.customer.Customer;
import com.moorkensam.xlra.model.error.XlraValidationException;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.TestedObject;

import java.util.Arrays;

import javax.persistence.NoResultException;

public class CustomerServiceImplTest extends UnitilsJUnit4 {

  @TestedObject
  private CustomerServiceImpl service;

  @Mock
  private CustomerDao dao;

  @Before
  public void init() {
    service = new CustomerServiceImpl();
    service.setCustomerDao(dao);
  }

  @Test
  public void testGetCustomerById() {
    Customer cust = new Customer();
    cust.setId(1L);

    EasyMock.expect(dao.getCustomerById(cust.getId())).andReturn(cust);
    EasyMockUnitils.replay();

    service.getCustomerById(1L);
  }

  @Test
  public void testGetAllCustomers() {
    Customer cust1 = new Customer();
    Customer cust2 = new Customer();
    EasyMock.expect(dao.getAllCustomers()).andReturn(Arrays.asList(cust1, cust2));

    EasyMockUnitils.replay();
    service.getAllCustomers();
  }

  @Test
  public void testCreateCustomer() throws XlraValidationException {
    Customer cust = new Customer();
    cust.setName("test");
    EasyMock.expect(dao.getCustomerByName(cust.getName())).andThrow(new NoResultException());
    dao.createCustomer(cust);
    EasyMock.expectLastCall();
    EasyMockUnitils.replay();

    service.createCustomer(cust);
  }

  @Test(expected = XlraValidationException.class)
  public void testCreateCustomerNameExists() throws XlraValidationException {
    Customer cust = new Customer();
    cust.setName("test");
    EasyMock.expect(dao.getCustomerByName(cust.getName())).andReturn(new Customer());
    EasyMockUnitils.replay();

    service.createCustomer(cust);
  }

  @Test
  public void testDeleteCustomer() {
    Customer cust = new Customer();
    dao.deleteCustomer(cust);
    EasyMock.expectLastCall();

    EasyMockUnitils.replay();
    service.deleteCustomer(cust);

  }

}
