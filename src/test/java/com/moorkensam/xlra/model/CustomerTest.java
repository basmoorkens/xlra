package com.moorkensam.xlra.model;

import com.moorkensam.xlra.model.customer.Customer;
import com.moorkensam.xlra.model.customer.CustomerContact;
import com.moorkensam.xlra.model.customer.Department;

import junit.framework.Assert;

import org.junit.Test;
import org.unitils.UnitilsJUnit4;

public class CustomerTest extends UnitilsJUnit4 {

  @Test
  public void testDeleteContact() {
    CustomerContact contact1 = new CustomerContact();
    contact1.setDepartment(Department.MANAGEMENT);
    contact1.setEmail("bas@test.com");
    CustomerContact contact2 = new CustomerContact();
    contact2.setEmail("bas2@test.com");
    contact2.setDepartment(Department.FINANCIAL);
    Customer customer = new Customer();
    customer.addContact(contact1);
    customer.addContact(contact2);

    CustomerContact contactDelete = new CustomerContact();
    contactDelete.setEmail("bas@test.com");
    customer.deleteContact(contactDelete);

    Assert.assertEquals(1, customer.getContacts().size());// standard contact is saved in a
                                                          // different way
    Assert.assertEquals(contact2.getEmail(), customer.getContacts().get(0).getEmail());
  }

}
