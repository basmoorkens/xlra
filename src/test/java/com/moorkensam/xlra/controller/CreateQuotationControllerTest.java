package com.moorkensam.xlra.controller;

import com.moorkensam.xlra.model.customer.Customer;
import com.moorkensam.xlra.model.customer.CustomerContact;
import com.moorkensam.xlra.service.CustomerService;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.primefaces.model.DualListModel;
import org.unitils.UnitilsJUnit4;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.TestedObject;

import java.util.Arrays;
import java.util.List;

public class CreateQuotationControllerTest extends UnitilsJUnit4 {

  @TestedObject
  private CreateQuotationController controller;

  @Mock
  private CustomerService customerService;

  /**
   * init method.
   */
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
    controller.setCustomerService(customerService);
  }

  @Test
  public void testGetSelectedCustomerContactsAsString() {
    String result = controller.getSelectedCustomerContactsAsString();
    Assert.assertEquals("test3", result);
  }


  @Test
  public void testCompleteValidCustomerName() {
    String name = "test";
    Customer customer = new Customer();
    customer.setName("test1");
    Customer customer2 = new Customer();
    customer2.setName("test2");
    EasyMock.expect(customerService.findCustomersLikeName(name)).andReturn(
        Arrays.asList(customer, customer2));
    EasyMockUnitils.replay();
    List<Customer> customers = controller.completeCustomerName("test");
    Assert.assertNotNull(customers);
    Assert.assertEquals(2, customers.size());
  }
}
