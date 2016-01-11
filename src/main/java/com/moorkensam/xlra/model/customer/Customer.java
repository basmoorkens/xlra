package com.moorkensam.xlra.model.customer;

import com.moorkensam.xlra.model.BaseEntity;
import com.moorkensam.xlra.model.configuration.Language;

import org.hibernate.annotations.BatchSize;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * When a customer is created an empty address and a base customercontact are made.
 * 
 * @author bas
 *
 */
@Entity
@Cacheable
@Table(name = "customer")
@NamedQueries({
    @NamedQuery(name = "Customer.findAll",
        query = "SELECT b FROM Customer b where b.deleted = false"),
    @NamedQuery(name = "Customer.findAllFullCustomers",
        query = "SELECT c FROM Customer c where c.deleted = false AND c.hasOwnRateFile = true"),
    @NamedQuery(name = "Customer.countCustomers", query = "SELECT count(c.id) FROM Customer c"),
    @NamedQuery(name = "Customer.findByName",
        query = "SELECT c FROM Customer c WHERE c.name = :name")})
public class Customer extends BaseEntity {

  private static final long serialVersionUID = 1L;

  /**
   * Inits the customer with an empty address and a standard contact.
   */
  public Customer() {
    this.address = new Address();
    CustomerContact standardContact = createStandardCustomerContact();
    addContact(standardContact);
  }

  private CustomerContact createStandardCustomerContact() {
    if (getStandardContact() == null) {
      CustomerContact standardContact = new CustomerContact();
      standardContact.setDepartment(Department.STANDARD);
      standardContact.setDisplay(false);
      standardContact.setCustomer(this);
      return standardContact;
    }
    return null;
  }

  @Length(max = 100)
  @NotNull
  @NotEmpty(message = "Name may not be empty")
  protected String name;

  @Length(max = 100)
  @NotNull
  @NotEmpty(message = "Phone may not be empty")
  protected String phone;

  @Enumerated(EnumType.STRING)
  @NotNull(message = "A language has to be selected")
  protected Language language;

  @Embedded
  private Address address;

  @Length(max = 100)
  private String btwNumber;

  private boolean hasOwnRateFile;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer", cascade = CascadeType.ALL,
      orphanRemoval = true)
  @BatchSize(size = 5)
  private List<CustomerContact> contacts;

  public Language getLanguage() {
    return language;
  }

  public void setLanguage(Language language) {
    this.language = language;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public String getBtwNumber() {
    return btwNumber;
  }

  public void setBtwNumber(String btwNumber) {
    this.btwNumber = btwNumber;
  }

  public boolean isHasOwnRateFile() {
    return hasOwnRateFile;
  }

  public void setHasOwnRateFile(boolean hasOwnRateFile) {
    this.hasOwnRateFile = hasOwnRateFile;
  }

  public List<CustomerContact> getContacts() {
    return contacts;
  }

  public void setContacts(List<CustomerContact> contacts) {
    this.contacts = contacts;
  }

  /**
   * Gets the customer contacts to display.
   * 
   * @return the list to display.
   */
  @Transient
  public List<CustomerContact> getDisplayContacts() {
    List<CustomerContact> displayContacts = new ArrayList<CustomerContact>();
    for (CustomerContact customerContact : contacts) {
      if (customerContact.isDisplay()) {
        displayContacts.add(customerContact);
      }
    }
    return displayContacts;
  }

  /**
   * Add a contact to the customer.
   * 
   * @param contact the contact to add.
   */
  public void addContact(CustomerContact contact) {
    if (contacts == null) {
      contacts = new ArrayList<CustomerContact>();
    }
    contacts.add(contact);
  }

  /**
   * Delete the contact from the contacts list.
   * 
   * @param contact The contact to delete.
   */
  public void deleteContact(CustomerContact contact) {
    if (contacts != null && !contacts.isEmpty()) {
      Iterator<CustomerContact> iterator = contacts.iterator();
      while (iterator.hasNext()) {
        CustomerContact cc = iterator.next();
        if (cc.equals(contact)) {
          iterator.remove();
        }
      }
    }
  }

  /**
   * Fetches the standard contact for the customer. There should only be one contact with the
   * department STANDARD.
   * 
   * @return The contact.
   */
  public CustomerContact getStandardContact() {
    if (contacts != null) {
      for (CustomerContact cc : contacts) {
        if (Department.STANDARD.equals(cc.getDepartment())) {
          return cc;
        }
      }
    }
    return null;
  }

}
