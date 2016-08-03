package com.moorkensam.xlra.service.util;

import com.moorkensam.xlra.model.customer.Customer;
import com.moorkensam.xlra.model.customer.Department;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.inject.annotation.TestedObject;

import java.util.List;

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

    Assert.assertNotNull(customer.getAddress());
  }

  @Test
  public void testNullAddres() {
    customer = new Customer();
    customer.setAddress(null);
    customerUtil.promoteToFullCustomer(customer);

    Assert.assertNotNull(customer.getAddress());
  }

  @Test
  public void testFetchDisplayDeps() {
    List<Department> deps = customerUtil.getDisplayDepartments();
    Assert.assertTrue(deps.size() > 0);
    for (Department dep : deps) {
      Assert.assertFalse(Department.STANDARD.equals(dep));
    }
  }
}
