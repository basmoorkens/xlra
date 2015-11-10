package com.moorkensam.xlra.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import com.moorkensam.xlra.controller.util.MessageUtil;
import com.moorkensam.xlra.model.BaseCustomer;
import com.moorkensam.xlra.model.FullCustomer;
import com.moorkensam.xlra.model.Language;
import com.moorkensam.xlra.service.CustomerService;
import com.moorkensam.xlra.service.util.CustomerUtil;

@ManagedBean
@ViewScoped
public class CustomerController {

	@Inject
	private CustomerService customerService;

	private FullCustomer selectedCustomer;

	private String detailGridTitle;

	private List<BaseCustomer> allCustomers;

	/**
	 * Property for toggling the grid in the frontend.
	 */
	private boolean collapseDetailGrid;

	@PostConstruct
	public void initializeController() {
		reInitializePage();
	}

	public void createCustomerOrUpdate() {
		if (selectedCustomer.getId() == 0) {
			customerService.createCustomer(selectedCustomer);
		} else {
			customerService.updateCustomer(selectedCustomer);
		}

		reInitializePage();
	}

	public void confirmDeleteCustomer() {
		MessageUtil.addMessage("Confirm delete", "Confirm delete of customer "
				+ selectedCustomer.getName());
	}

	public void deleteCustomer() throws IOException {
		if (selectedCustomer.getId() != 0) {
			customerService.deleteCustomer(selectedCustomer);
		}
		reInitializePage();
	}

	private void reInitializePage() {
		setAllCustomers(customerService.getAllCustomers());
		collapseDetailGrid = true;
		detailGridTitle = "Details selected customer";
		selectedCustomer = new FullCustomer(true);
	}

	public List<Language> getAllLanguages() {
		return Arrays.asList(Language.values());
	}

	public void setupPageForNewCustomer() {
		detailGridTitle = "Details for new customer";
		selectedCustomer = new FullCustomer(true);
		openDetailGrid();
	}

	public void setupPageForEditCustomer() {
		if (!(selectedCustomer instanceof FullCustomer)) {
			selectedCustomer = CustomerUtil.getInstance()
					.promoteToFullCustomer(selectedCustomer);
		}
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

	public List<BaseCustomer> getAllCustomers() {
		return allCustomers;
	}

	public void setAllCustomers(List<BaseCustomer> allCustomers) {
		this.allCustomers = allCustomers;
	}

}
