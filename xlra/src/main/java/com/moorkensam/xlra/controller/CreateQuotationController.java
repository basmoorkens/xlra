package com.moorkensam.xlra.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.event.FlowEvent;

import com.moorkensam.xlra.model.BaseCustomer;
import com.moorkensam.xlra.model.Language;
import com.moorkensam.xlra.model.Quotation;
import com.moorkensam.xlra.service.CustomerService;

@ManagedBean
@ViewScoped
public class CreateQuotationController {

	@Inject
	private CustomerService customerService;

	private List<BaseCustomer> customers;

	private Quotation quotation;

	private BaseCustomer customerToAdd;

	private boolean renderAddCustomerGrid;

	@PostConstruct
	public void init() {
		setCustomers(customerService.getAllCustomers());
		initializeNewCustomer();
	}

	private void initializeNewCustomer() {
		customerToAdd = new BaseCustomer();
	}

	public void setupPageForNewCustomer() {
		renderAddCustomerGrid = true;
	}

	public void createCustomer() {

	}

	public List<Language> getAllLanguages() {
		return Arrays.asList(Language.values());
	}

	public String onFlowProcess(FlowEvent event) {
		return event.getNewStep();
	}

	public List<BaseCustomer> completeCustomerName(String input) {
		List<BaseCustomer> filteredCustomers = new ArrayList<BaseCustomer>();
		if (customers != null) {
			for (BaseCustomer baseCustomer : customers) {
				if (baseCustomer.getName().toLowerCase()
						.contains(input.toLowerCase())) {
					filteredCustomers.add(baseCustomer);
				}
			}
		}
		return filteredCustomers;
	}

	public Quotation getQuotation() {
		return quotation;
	}

	public void setQuotation(Quotation quotation) {
		this.quotation = quotation;
	}

	public List<BaseCustomer> getCustomers() {
		return customers;
	}

	public void setCustomers(List<BaseCustomer> customers) {
		this.customers = customers;
	}

	public BaseCustomer getCustomerToAdd() {
		return customerToAdd;
	}

	public void setCustomerToAdd(BaseCustomer customerToAdd) {
		this.customerToAdd = customerToAdd;
	}

	public boolean isRenderAddCustomerGrid() {
		return renderAddCustomerGrid;
	}

	public void setRenderAddCustomerGrid(boolean renderAddCustomerGrid) {
		this.renderAddCustomerGrid = renderAddCustomerGrid;
	}

}
