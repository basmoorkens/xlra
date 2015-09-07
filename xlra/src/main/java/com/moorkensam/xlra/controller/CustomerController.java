package com.moorkensam.xlra.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import com.moorkensam.xlra.controller.util.MessageUtil;
import com.moorkensam.xlra.model.FullCustomer;
import com.moorkensam.xlra.model.Language;
import com.moorkensam.xlra.service.CustomerService;

@ManagedBean
@ViewScoped
public class CustomerController {

	@Inject
	private CustomerService customerService;

	private FullCustomer selectedCustomer;

	private String detailGridTitle;

	
	/**
	 * Property for toggling the grid in the frontend.
	 */
	private boolean collapseDetailGrid;

	@PostConstruct
	public void initializeController() {
		reInitializePage();
	}

	public void createCustomerOrUpdate() {
		if(selectedCustomer.getId()==0) { 
			customerService.createCustomer(selectedCustomer);			
		} else {
			customerService.updateCustomer(selectedCustomer);
		} 
		
		reInitializePage();
	}

	public void confirmDeleteCustomer() {
		MessageUtil.addMessage("Confirm delete", "Confirm delete of customer " + selectedCustomer.getName());
	}
	
	public void deleteCustomer() throws IOException {
		if (selectedCustomer.getId() != 0) {
			customerService.deleteCustomer(selectedCustomer);
		}
		reInitializePage();
	}

	private void reInitializePage() {
		collapseDetailGrid = true;
		detailGridTitle = "Details selected customer";
		selectedCustomer = new FullCustomer(true);
	}

	public List<Language> getAllLanguages() {
		return Arrays.asList(Language.values());
	}

	public List<FullCustomer> getAllFullCustomers() {
		return customerService.getAllFullCustomers();
	}

	public void setupPageForNewUser() {
		detailGridTitle = "Details for new customer";
		selectedCustomer = new FullCustomer(true);
		openDetailGrid();
	}

	public void setupPageForEditUser() {
		openDetailGrid();
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

	public FullCustomer getSelectedCustomer() {
		return selectedCustomer;
	}

	public void setSelectedCustomer(FullCustomer selectedCustomer) {
		this.selectedCustomer = selectedCustomer;
		detailGridTitle = "Details for customer " + selectedCustomer.getName();
	}
}
