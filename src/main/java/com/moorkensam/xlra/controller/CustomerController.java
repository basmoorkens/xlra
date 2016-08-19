package com.moorkensam.xlra.controller;

import com.moorkensam.xlra.controller.util.MessageUtil;
import com.moorkensam.xlra.model.configuration.Language;
import com.moorkensam.xlra.model.customer.Customer;
import com.moorkensam.xlra.model.customer.CustomerContact;
import com.moorkensam.xlra.model.customer.Department;
import com.moorkensam.xlra.model.error.XlraValidationException;
import com.moorkensam.xlra.service.CustomerService;
import com.moorkensam.xlra.service.util.CustomerUtil;

import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

@ManagedBean
@ViewScoped
public class CustomerController {

  @Inject
  private CustomerService customerService;

  @ManagedProperty("#{msg}")
  private ResourceBundle messageBundle;

  private MessageUtil messageUtil;

  private Customer selectedCustomer;

  private CustomerContact selectedContact;

  private String detailGridTitle;

  private LazyDataModel<Customer> model;

  private String contactHeader;

  private String saveContactButtonTitle;

  @PostConstruct
  public void initializeController() {
    messageUtil = MessageUtil.getInstance(messageBundle);
    reInitializePage();
    initModel();
  }

  private void initModel() {
    model = new LazyDataModel<Customer>() {

      private static final long serialVersionUID = -7346727256149263406L;

      @Override
      public List<Customer> load(int first, int pageSize, String sortField, SortOrder sortOrder,
          Map<String, Object> filters) {
        List<Customer> lazyCustomers =
            customerService.getLazyCustomers(first, pageSize, sortField, sortOrder, filters);
        model.setRowCount(customerService.countCustomers());
        return lazyCustomers;
      }

    };
  }

  /**
   * Create or update the selected customer in the backend.
   */
  public void createCustomerOrUpdate() {
    doCreateOrUpdate();
    reInitializePage();
  }

  private void doCreateOrUpdate() {
    if (selectedCustomer.getId() == 0) {
      try {
        customerService.createCustomer(selectedCustomer);
        messageUtil.addMessage("Customer added", "Customer " + selectedCustomer.getName()
            + " was successfully added.");
      } catch (XlraValidationException e) {
        messageUtil.addErrorMessage("Invalid customer data", e.getBusinessException());
      }
    } else {
      selectedCustomer = customerService.updateCustomer(selectedCustomer);
      messageUtil.addMessage("Customer updated", "Customer " + selectedCustomer.getName()
          + " was successfully updated.");
    }
  }

  /**
   * Deletes the selected customer.
   * 
   */
  public void deleteCustomer(Customer customer) {
    customerService.deleteCustomer(customer);
    messageUtil.addMessage("Customer removed", "Successfully removed customer.");
  }

  private void hideAddDialog() {
    RequestContext context = RequestContext.getCurrentInstance();
    context.execute("PF('addCustomerDialog').hide();");
  }

  private void showAddDialog() {
    RequestContext context = RequestContext.getCurrentInstance();
    context.execute("PF('addCustomerDialog').show();");
  }

  private void hideAddContactDialog() {
    RequestContext context = RequestContext.getCurrentInstance();
    context.execute("PF('addContactDialog').hide();");
  }

  private void showAddContactDialog() {
    RequestContext context = RequestContext.getCurrentInstance();
    context.execute("PF('addContactDialog').show();");
  }

  private void reInitializePage() {
    hideAddDialog();
    detailGridTitle = "Details selected customer";
    selectedCustomer = new Customer();
  }

  public List<Language> getAllLanguages() {
    return Arrays.asList(Language.values());
  }

  /**
   * Set up the page for a new customer.
   */
  public void setupPageForNewCustomer() {
    detailGridTitle = "New customer";
    selectedCustomer = new Customer();
    selectedCustomer.setHasOwnRateFile(false);
    showAddDialog();
  }

  public void cancelDetail() {
    selectedCustomer = null;
    hideAddDialog();
  }

  /**
   * Sets up the page to add a new customer contact.
   */
  public void setupPageForNewContact() {
    selectedContact = new CustomerContact();
    contactHeader = "Add contact for " + selectedCustomer.getName();
    saveContactButtonTitle = "Add contact to customer";
    showAddContactDialog();
  }

  /**
   * Sets up the page to edit a customer contact.
   * 
   * @param customerContact The customer contact to load for edit.
   */
  public void setupPageForEditContact(CustomerContact customerContact) {
    selectedContact = customerContact;
    contactHeader =
        "Edit contact " + selectedContact.getEmail() + " from customer "
            + selectedCustomer.getName();
    saveContactButtonTitle = "Update contact";
    showAddContactDialog();
  }

  /**
   * Delete the contact. Just cuts the coupling and saves the customer and cascades the delete.
   * 
   * @param contact the contact to delete.
   */
  public void deleteCustomerContact(CustomerContact contact) {
    selectedCustomer.deleteContact(contact);
    contact.setCustomer(null);
    if (selectedCustomer.getId() > 0) {
      doCreateOrUpdate();
    }
    showAddDialog();
  }

  /**
   * Cancel the create/update of a contact.
   */
  public void cancelAddContact() {
    selectedContact = null;
    hideAddContactDialog();
    showAddDialog();
  }

  /**
   * Adds the selected contact to the customer contacts of the selected customer.
   */
  public void addContactToCustomer() {
    if (selectedContact.getId() <= 0) {
      linkContactAndCustomer();
    }
    if (selectedCustomer.getId() > 0) {
      doCreateOrUpdate();
    }
    hideAddContactDialog();
    selectedContact = null;
    showAddDialog();
  }

  private void linkContactAndCustomer() {
    selectedContact.setCustomer(selectedCustomer);
    selectedCustomer.addContact(selectedContact);
  }

  /**
   * Set up the page to edit a customer.
   * 
   * @param customer The customer to edit.
   */
  public void setupPageForEditCustomer(Customer customer) {
    loadInCustomer(customer);
    detailGridTitle = "Edit customer " + selectedCustomer.getName();
    showAddDialog();
  }

  private void loadInCustomer(Customer customer) {
    customer = customerService.getCustomerById(customer.getId());
    CustomerUtil.getInstance().promoteToFullCustomer(customer);
    selectedCustomer = customer;
  }

  public String getDetailGridTitle() {
    return detailGridTitle;
  }

  public void setDetailGridTitle(String detailGridTitle) {
    this.detailGridTitle = detailGridTitle;
  }

  public Customer getSelectedCustomer() {
    return selectedCustomer;
  }

  public void setSelectedCustomer(Customer selectedCustomer) {
    this.selectedCustomer = selectedCustomer;
  }

  public LazyDataModel<Customer> getModel() {
    return model;
  }

  public void setModel(LazyDataModel<Customer> model) {
    this.model = model;
  }

  public CustomerContact getSelectedContact() {
    return selectedContact;
  }

  public void setSelectedContact(CustomerContact selectedContact) {
    this.selectedContact = selectedContact;
  }

  public List<Department> getAllDepartments() {
    return CustomerUtil.getInstance().getDisplayDepartments();
  }

  public String getContactHeader() {
    return contactHeader;
  }

  public void setContactHeader(String contactHeader) {
    this.contactHeader = contactHeader;
  }

  public String getSaveContactButtonTitle() {
    return saveContactButtonTitle;
  }

  public void setSaveContactButtonTitle(String saveContactButtonTitle) {
    this.saveContactButtonTitle = saveContactButtonTitle;
  }

  public ResourceBundle getMessageBundle() {
    return messageBundle;
  }

  public void setMessageBundle(ResourceBundle messageBundle) {
    this.messageBundle = messageBundle;
  }

  public MessageUtil getMessageUtil() {
    return messageUtil;
  }

  public void setMessageUtil(MessageUtil messageUtil) {
    this.messageUtil = messageUtil;
  }

}
