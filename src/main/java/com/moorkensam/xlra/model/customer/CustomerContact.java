package com.moorkensam.xlra.model.customer;

import com.moorkensam.xlra.model.BaseEntity;
import com.moorkensam.xlra.model.Person;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "customer_contacts")
public class CustomerContact extends BaseEntity implements Person {

  private static final long serialVersionUID = -1943038849182892475L;

  /**
   * Standard constructor.
   */
  public CustomerContact() {
    display = true;
  }

  @ManyToOne
  @JoinColumn(name = "customer_id")
  private Customer customer;

  @Enumerated(EnumType.STRING)
  private Department department;

  private String firstName;

  private String name;

  private String email;

  private boolean display;

  @Override
  public String getFirstName() {
    return firstName;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getEmail() {
    return email;
  }

  @Override
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public void setEmail(String email) {
    this.email = email;
  }

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  public Department getDepartment() {
    return department;
  }

  public void setDepartment(Department department) {
    this.department = department;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof CustomerContact)) {
      return false;
    }
    CustomerContact other = (CustomerContact) obj;
    if (other.getId() > 0 && id > 0 && other.getId() != id) {
      return false;
    }
    if (other.getEmail() != null && email != null) {
      if (!other.getEmail().equals(email)) {
        return false;
      }
    } else if (other.getEmail() == null && email == null) {
      // ok
    } else {
      return false;
    }
    return true;
  }

  public boolean isDisplay() {
    return display;
  }

  public void setDisplay(boolean display) {
    this.display = display;
  }
}
