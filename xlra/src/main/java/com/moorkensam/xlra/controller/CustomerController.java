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

	/**
	 * Property for toggling the grid in the frontend.
	 */
	private boolean collapseDetailGrid;

	@PostConstruct
	public void initializeController() {
		setupNewCustomer();
		collapseDetailGrid = true;
	}

	private void setupNewCustomer() {
		newCustomer = new FullCustomer(true);
	}

	public List<Language> getAllLanguages() {
		return Arrays.asList(Language.values());
	}

	public List<FullCustomer> getAllFullCustomers() {
		return customerService.getAllFullCustomers();
	}

	public void createCustomer() {
		customerService.createCustomer(newCustomer);
		setupNewCustomer();
		collapseDetailGrid=true;
	}


	public FullCustomer getNewCustomer() {
		return newCustomer;
	}

	public void setNewCustomer(FullCustomer newCustomer) {
		this.newCustomer = newCustomer;
	}

	public void openDetailGrid() {
		collapseDetailGrid=false;
	}

	public void closeDetailGrid() {
		collapseDetailGrid=true;
	}
	
	public boolean isRenderDetailGrid() {
		return collapseDetailGrid;
	}

	public void setRenderDetailGrid(boolean renderDetailGrid) {
		this.collapseDetailGrid = renderDetailGrid;
	}

}
