package com.moorkensam.xlra.service.util;

import com.moorkensam.xlra.model.customer.Address;
import com.moorkensam.xlra.model.customer.Customer;

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
   * @return the promoted customer.
   */
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
