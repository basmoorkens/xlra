package com.moorkensam.xlra.controller;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import com.moorkensam.xlra.model.FullCustomer;
import com.moorkensam.xlra.model.Language;
import com.moorkensam.xlra.service.CustomerService;

@ManagedBean
@ViewScoped
public class CustomerController {

	@Inject
	private CustomerService customerService;

	private FullCustomer newCustomer;

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

	private void reInitializePage() {
		collapseDetailGrid = true;
		detailGridTitle = "Details selected customer";
		newCustomer = new FullCustomer(true);
		selectedCustomer = null;
	}

	public List<Language> getAllLanguages() {
		return Arrays.asList(Language.values());
	}

	public List<FullCustomer> getAllFullCustomers() {
		return customerService.getAllFullCustomers();
	}

	public void createCustomer() {
		customerService.createCustomer(newCustomer);
		reInitializePage();
	}

	public void setupPageForNewUser() {
		detailGridTitle = "Details for new customer";
		openDetailGrid();
	}

	public void setupPageForEditUser() {
		openDetailGrid();
	}

	public FullCustomer getNewCustomer() {
		return newCustomer;
	}

	public void setNewCustomer(FullCustomer newCustomer) {
		this.newCustomer = newCustomer;
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
