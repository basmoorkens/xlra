package com.moorkensam.xlra.controller;

import com.moorkensam.xlra.model.customer.Customer;
import com.moorkensam.xlra.model.customer.CustomerContact;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.primefaces.model.DualListModel;
import org.unitils.UnitilsJUnit4;
import org.unitils.inject.annotation.TestedObject;

import java.util.Arrays;
import java.util.List;

public class CreateQuotationControllerTest extends UnitilsJUnit4 {

  @TestedObject
  private CreateQuotationController controller;

  @Before
  public void init() {
    controller = new CreateQuotationController();
    CustomerContact contact1 = new CustomerContact();
    contact1.setEmail("test1");
    CustomerContact contact2 = new CustomerContact();
    contact2.setEmail("test2");
    CustomerContact contact3 = new CustomerContact();
    contact3.setEmail("test3");
    DualListModel<CustomerContact> contacts = new DualListModel<CustomerContact>();
    contacts.setTarget(Arrays.asList(contact3));
    contacts.setSource(Arrays.asList(contact1, contact2));
    controller.setCustomerContacts(contacts);
  }

  @Test
  public void testGetSelectedCustomerContactsAsString() {
    String result = controller.getSelectedCustomerContactsAsString();
    Assert.assertEquals("test3", result);
  }

  @Test
  public void testCompleteCustomerNameWithNoCustomers() {
    controller.setCustomers(null);
    List<Customer> completeCustomerName = controller.completeCustomerName("test");
    Assert.assertNotNull(completeCustomerName);
    Assert.assertEquals(0, completeCustomerName.size());
  }

  @Test
  public void testCompleteValidCustomerName() {
    Customer customer = new Customer();
    customer.setName("test1");
    Customer customer2 = new Customer();
    customer2.setName("test2");
    Customer customer3 = new Customer();
    customer3.setName("oki");
    controller.setCustomers(Arrays.asList(customer, customer2, customer3));
    List<Customer> customers = controller.completeCustomerName("test");
    Assert.assertNotNull(customers);
    Assert.assertEquals(2, customers.size());
  }
}
