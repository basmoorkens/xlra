package com.moorkensam.xlra.controller;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import com.moorkensam.xlra.controller.util.MessageUtil;
import com.moorkensam.xlra.model.configuration.Language;
import com.moorkensam.xlra.model.customer.Customer;
import com.moorkensam.xlra.service.CustomerService;
import com.moorkensam.xlra.service.util.CustomerUtil;

@ManagedBean
@ViewScoped
public class CustomerController {

  @Inject
  private CustomerService customerService;

  private Customer selectedCustomer;

  private String detailGridTitle;

  private List<Customer> allCustomers;

  /**
   * Property for toggling the grid in the frontend.
   */
  private boolean collapseDetailGrid;

  @PostConstruct
  public void initializeController() {
    reInitializePage();
  }

  /**
   * Create or update the selected customer in the backend.
   */
  public void createCustomerOrUpdate() {
    if (selectedCustomer.getId() == 0) {
      customerService.createCustomer(selectedCustomer);
    } else {
      customerService.updateCustomer(selectedCustomer);
    }

    reInitializePage();
  }

  public void confirmDeleteCustomer() {
    MessageUtil.addMessage("Confirm delete",
        "Confirm delete of customer " + selectedCustomer.getName());
  }

  /**
   * Deletes the selected customer.
   * 
   */
  public void deleteCustomer() {
    if (selectedCustomer.getId() != 0) {
      customerService.deleteCustomer(selectedCustomer);
    }
    reInitializePage();
  }

  private void reInitializePage() {
    setAllCustomers(customerService.getAllCustomers());
    collapseDetailGrid = true;
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
    detailGridTitle = "Details for new customer";
    selectedCustomer = new Customer();
    selectedCustomer.setHasOwnRateFile(true);
    openDetailGrid();
  }

  /**
   * Set up the page to edit a customer.
   * 
   * @param customer The customer to edit.
   */
  public void setupPageForEditCustomer(Customer customer) {
    if (!customer.isHasOwnRateFile()) {
      customer = CustomerUtil.getInstance().promoteToFullCustomer(customer);
    }
    selectedCustomer = customer;
    openDetailGrid();
    detailGridTitle = "Details for customer " + selectedCustomer.getName();
  }

  public void openDetailGrid() {
    collapseDetailGrid = false;
  }

  public void closeDetailGrid() {
    collapseDetailGrid = true;
  }

  public boolean isRenderDetailGrid() {
    return collapseDetailGrid;
  }

  public void setRenderDetailGrid(boolean renderDetailGrid) {
    this.collapseDetailGrid = renderDetailGrid;
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

  public List<Customer> getAllCustomers() {
    return allCustomers;
  }

  public void setAllCustomers(List<Customer> allCustomers) {
    this.allCustomers = allCustomers;
  }

}
