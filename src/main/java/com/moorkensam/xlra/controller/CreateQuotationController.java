package com.moorkensam.xlra.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import com.moorkensam.xlra.controller.util.MessageUtil;
import com.moorkensam.xlra.model.configuration.Language;
import com.moorkensam.xlra.model.customer.Customer;
import com.moorkensam.xlra.model.error.RateFileException;
import com.moorkensam.xlra.model.offerte.OfferteOptionDTO;
import com.moorkensam.xlra.model.offerte.QuotationQuery;
import com.moorkensam.xlra.model.offerte.QuotationResult;
import com.moorkensam.xlra.model.rate.Country;
import com.moorkensam.xlra.model.rate.Kind;
import com.moorkensam.xlra.model.rate.Measurement;
import com.moorkensam.xlra.model.rate.TransportType;
import com.moorkensam.xlra.service.CountryService;
import com.moorkensam.xlra.service.CustomerService;
import com.moorkensam.xlra.service.FileService;
import com.moorkensam.xlra.service.QuotationService;
import com.moorkensam.xlra.service.impl.FileServiceImpl;
import com.moorkensam.xlra.service.util.TranslationUtil;

@ManagedBean
@ViewScoped
public class CreateQuotationController {

	@Inject
	private QuotationService quotationService;

	@Inject
	private CustomerService customerService;

	@Inject
	private CountryService countryService;

	private TranslationUtil translationUtil;

	private FileService fileService;

	private List<Customer> customers;

	private QuotationQuery quotationQuery;

	private QuotationResult quotationResult;

	private Customer customerToAdd;

	private boolean renderAddCustomerGrid;

	private List<Country> allCountries;

	private boolean collapseCustomerPanel, collapseFiltersPanel,
			collapseSummaryPanel, collapseOptionsPanel, collapseResultPanel;

	@PostConstruct
	public void init() {
		allCountries = countryService.getAllCountries();
		initializeNewQuotationQuery();
		refreshCustomers();
		initializeNewCustomer();
		collapseFiltersPanel = true;
		collapseSummaryPanel = true;
		collapseResultPanel = true;
		collapseOptionsPanel = true;
		fileService = new FileServiceImpl();
		translationUtil = new TranslationUtil();
	}

	private void initializeNewQuotationQuery() {
		QuotationQuery query = new QuotationQuery();
		query.setTransportType(TransportType.EXPORT);
		for (Country c : allCountries) {
			if (c.getShortName().toLowerCase().equals("be")) {
				query.setCountry(c);
			}
		}
		query.setMeasurement(Measurement.PALET);
		query.setKindOfRate(Kind.NORMAL);
		setQuotationQuery(query);
	}

	private void refreshCustomers() {
		setCustomers(customerService.getAllCustomers());
	}

	private void initializeNewCustomer() {
		customerToAdd = new Customer();
	}

	public void setupPageForNewCustomer() {
		renderAddCustomerGrid = true;
	}

	public void createCustomer() {
		getQuotationQuery().setCustomer(
				customerService.createCustomer(customerToAdd));
		MessageUtil.addMessage("Customer created", "Created customer "
				+ customerToAdd.getName());
		refreshCustomers();
		renderAddCustomerGrid = false;
		customerToAdd = new Customer();
	}

	public List<Language> getAllLanguages() {
		return Arrays.asList(Language.values());
	}

	public void procesCustomer() {
		if (getQuotationQuery().getCustomer().isHasOwnRateFile()) {
			setupFiltersFromExistingCustomer();
		}
		showRateFilterPanel();
	}

	public void showCustomerPanel() {
		collapseCustomerPanel = false;
		collapseFiltersPanel = true;
		collapseOptionsPanel = true;
		collapseSummaryPanel = true;
		collapseResultPanel = true;
	}

	public void showRateFilterPanel() {
		collapseCustomerPanel = true;
		collapseFiltersPanel = false;
		collapseOptionsPanel = true;
		collapseSummaryPanel = true;
		collapseResultPanel = true;
	}

	public void showOptionsPanel() {
		collapseCustomerPanel = true;
		collapseFiltersPanel = true;
		collapseOptionsPanel = false;
		collapseSummaryPanel = true;
		collapseResultPanel = true;
	}

	public void showSummaryPanel() {
		collapseCustomerPanel = true;
		collapseFiltersPanel = true;
		collapseOptionsPanel = true;
		collapseSummaryPanel = false;
		collapseResultPanel = true;
	}

	public void showResultPanel() {
		collapseCustomerPanel = true;
		collapseFiltersPanel = true;
		collapseOptionsPanel = true;
		collapseSummaryPanel = true;
		collapseResultPanel = false;
	}

	public void processRateFilters() {
		try {
			quotationResult = quotationService
					.generateQuotationResultForQuotationQuery(quotationQuery);
			fillInOptionTranslations();
			showOptionsPanel();
		} catch (RateFileException re2) {
			showRateFileError(re2);
		} catch (EJBException e) {
			if (e.getCausedByException() instanceof RateFileException) {
				RateFileException re = (RateFileException) e
						.getCausedByException();
				showRateFileError(re);
			} else {
				MessageUtil
						.addErrorMessage("Unknown exception",
								"An unexpected exception occurred, please contact the system admin.");
			}
		}
	}

	private void fillInOptionTranslations() {
		translationUtil.fillInTranslations(quotationResult
				.getSelectableOptions());
	}

	public void processOptions() {
		try {
			quotationResult = quotationService
					.generateEmailAndPdfForOfferte(quotationResult);
			showSummaryPanel();
		} catch (RateFileException e) {
			showRateFileError(e);
		}
	}

	public void downloadPdf() throws IOException {
		// Get the FacesContext
		FacesContext facesContext = FacesContext.getCurrentInstance();

		// Get HTTP response
		HttpServletResponse response = (HttpServletResponse) facesContext
				.getExternalContext().getResponse();

		// Set response headers
		response.reset(); // Reset the response in the first place
		response.setHeader("Content-Type", "application/pdf"); // Set only the
																// content type

		// Open response output stream
		OutputStream responseOutputStream = response.getOutputStream();

		// Read PDF contents
		File generatedPdfFromDisk = fileService
				.getOffertePdfFromFileSystem(quotationResult.getPdfFileName());
		InputStream pdfInputStream = new FileInputStream(generatedPdfFromDisk);

		// Read PDF contents and write them to the output
		byte[] bytesBuffer = new byte[2048];
		int bytesRead;
		while ((bytesRead = pdfInputStream.read(bytesBuffer)) > 0) {
			responseOutputStream.write(bytesBuffer, 0, bytesRead);
		}

		// Make sure that everything is out
		responseOutputStream.flush();

		// Close both streams
		pdfInputStream.close();
		responseOutputStream.close();

		facesContext.responseComplete();

	}

	public void submitOfferte() {
		try {
			quotationService.submitQuotationResult(quotationResult);
			MessageUtil.addMessage("Offerte successfully send",
					"The offerte was successfully send to "
							+ quotationResult.getEmailResult().getToAddress());
			showResultPanel();
		} catch (RateFileException re2) {
			showRateFileError(re2);
		} catch (EJBException e) {
			if (e.getCausedByException() instanceof RateFileException) {
				RateFileException re = (RateFileException) e
						.getCausedByException();
				showRateFileError(re);
			} else {
				MessageUtil
						.addErrorMessage("Unknown exception",
								"An unexpected exception occurred, please contact the system admin.");
			}
		}
	}

	private void showRateFileError(RateFileException re) {
		MessageUtil.addErrorMessage(
				"Unexpected error whilst processing quotation request.",
				re.getBusinessException());
	}

	private void setupFiltersFromExistingCustomer() {
		// TODO implements loading of filters based on customer ratefile present
	}

	public List<Customer> completeCustomerName(String input) {
		List<Customer> filteredCustomers = new ArrayList<Customer>();
		if (customers != null) {
			for (Customer baseCustomer : customers) {
				if (baseCustomer.getName().toLowerCase()
						.contains(input.toLowerCase())) {
					filteredCustomers.add(baseCustomer);
				}
			}
		}
		return filteredCustomers;
	}

	public List<Measurement> getAllMeasurements() {
		return Arrays.asList(Measurement.values());
	}

	public List<Kind> getAllKinds() {
		return Arrays.asList(Kind.values());
	}

	public List<Customer> getCustomers() {
		return customers;
	}

	public void setCustomers(List<Customer> customers) {
		this.customers = customers;
	}

	public Customer getCustomerToAdd() {
		return customerToAdd;
	}

	public void setCustomerToAdd(Customer customerToAdd) {
		this.customerToAdd = customerToAdd;
	}

	public boolean isRenderAddCustomerGrid() {
		return renderAddCustomerGrid;
	}

	public void setRenderAddCustomerGrid(boolean renderAddCustomerGrid) {
		this.renderAddCustomerGrid = renderAddCustomerGrid;
	}

	public List<Country> getAllCountries() {
		return allCountries;
	}

	public void setAllCountries(List<Country> allCountries) {
		this.allCountries = allCountries;
	}

	public QuotationQuery getQuotationQuery() {
		return quotationQuery;
	}

	public void setQuotationQuery(QuotationQuery quotationQuery) {
		this.quotationQuery = quotationQuery;
	}

	public QuotationResult getQuotationResult() {
		return quotationResult;
	}

	public void setQuotationResult(QuotationResult quotationResult) {
		this.quotationResult = quotationResult;
	}

	public boolean isCollapseSummaryPanel() {
		return collapseSummaryPanel;
	}

	public void setCollapseSummaryPanel(boolean collapseSummaryPanel) {
		this.collapseSummaryPanel = collapseSummaryPanel;
	}

	public boolean isCollapseCustomerPanel() {
		return collapseCustomerPanel;
	}

	public void setCollapseCustomerPanel(boolean collapseCustomerPanel) {
		this.collapseCustomerPanel = collapseCustomerPanel;
	}

	public boolean isCollapseFiltersPanel() {
		return collapseFiltersPanel;
	}

	public void setCollapseFiltersPanel(boolean collapseFiltersPanel) {
		this.collapseFiltersPanel = collapseFiltersPanel;
	}

	public List<TransportType> getAllTransportTypes() {
		return Arrays.asList(TransportType.values());
	}

	public boolean isCollapseResultPanel() {
		return collapseResultPanel;
	}

	public void setCollapseResultPanel(boolean collapseResultPanel) {
		this.collapseResultPanel = collapseResultPanel;
	}

	public FileService getFileService() {
		return fileService;
	}

	public void setFileService(FileService fileService) {
		this.fileService = fileService;
	}

	public boolean isCollapseOptionsPanel() {
		return collapseOptionsPanel;
	}

	public void setCollapseOptionsPanel(boolean collapseOptionsPanel) {
		this.collapseOptionsPanel = collapseOptionsPanel;
	}

}
