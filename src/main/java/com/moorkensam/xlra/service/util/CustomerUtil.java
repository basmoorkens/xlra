package com.moorkensam.xlra.service.util;

import com.moorkensam.xlra.model.customer.Address;
import com.moorkensam.xlra.model.customer.Customer;
import com.moorkensam.xlra.model.customer.CustomerContact;
import com.moorkensam.xlra.model.customer.Department;

import java.util.ArrayList;
import java.util.List;

public class CustomerUtil {

  private static CustomerUtil instance;

  /**
   * Gets the instance.
   * 
   * @return the instance.
   */
  public static CustomerUtil getInstance() {
    if (instance == null) {
      instance = new CustomerUtil();
    }
    return instance;
  }

  /**
   * promote a 'base' customer to a full customer.
   * 
   * @param customer the customer to promote.
   */
  public void promoteToFullCustomer(Customer customer) {
    if (customer.getAddress() == null) {
      customer.setAddress(new Address());
    }
  }

  /**
   * Returns a filtered list with all the customercontacts for a customer minus the standard
   * contact.
   * 
   * @param customer The customer to list the contacts for
   * @return The filtered contacts list.
   */
  public List<CustomerContact> getCustomerContactsForCustomerWithoutStandardContact(
      Customer customer) {
    List<CustomerContact> filteredContacts = new ArrayList<CustomerContact>();
    for (CustomerContact contact : customer.getContacts()) {
      if (!Department.STANDARD.equals(contact.getDepartment())) {
        filteredContacts.add(contact);
      }
    }
    return filteredContacts;
  }

  /**
   * Returns all departments that should be shown on the frontend.
   * 
   * @return The list of display departments.
   */
  public List<Department> getDisplayDepartments() {
    List<Department> displayDepartments = new ArrayList<Department>();
    for (Department dep : Department.values()) {
      if (!Department.STANDARD.equals(dep)) {
        displayDepartments.add(dep);
      }
    }
    return displayDepartments;
  }

  private CustomerUtil() {

  }

}
