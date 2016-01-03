package com.moorkensam.xlra.model.customer;

import com.moorkensam.xlra.model.BaseEntity;
import com.moorkensam.xlra.model.configuration.Language;

import org.hibernate.annotations.BatchSize;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

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

@Entity
@Cacheable
@Table(name = "customer")
@NamedQueries({
    @NamedQuery(name = "Customer.findAll",
        query = "SELECT b FROM Customer b where b.deleted = false"),
    @NamedQuery(name = "Customer.findAllFullCustomers",
        query = "SELECT c FROM Customer c where c.deleted = false AND c.hasOwnRateFile = true"),
    @NamedQuery(name = "Customer.countCustomers", query = "SELECT count(c.id) FROM Customer c")})
public class Customer extends BaseEntity {

  private static final long serialVersionUID = 1L;

  public Customer() {
    this.address = new Address();
  }

  @Length(max = 100)
  @NotNull
  @NotEmpty(message = "Name may not be empty")
  protected String name;

  @Length(max = 100)
  @NotNull
  @NotEmpty(message = "Email may not be empty")
  protected String email;

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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
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

}
