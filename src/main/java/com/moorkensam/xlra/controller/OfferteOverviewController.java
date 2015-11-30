package com.moorkensam.xlra.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;

import com.moorkensam.xlra.controller.util.MessageUtil;
import com.moorkensam.xlra.model.offerte.OfferteSearchFilter;
import com.moorkensam.xlra.model.offerte.QuotationResult;
import com.moorkensam.xlra.model.rate.Country;
import com.moorkensam.xlra.service.CountryService;
import com.moorkensam.xlra.service.EmailService;
import com.moorkensam.xlra.service.FileService;
import com.moorkensam.xlra.service.QuotationService;
import com.moorkensam.xlra.service.impl.FileServiceImpl;

@ManagedBean
@ViewScoped
public class OfferteOverviewController {

	@Inject
	private QuotationService quotationService;

	@Inject
	private CountryService countryService;

	@Inject
	private EmailService emailService;

	private FileService fileService;

	private List<QuotationResult> quotationResults;

	private QuotationResult selectedOfferte;

	private List<Country> allCountrys;

	private OfferteSearchFilter searchFilter;

	private boolean detail;

	@PostConstruct
	public void initialize() {
		fileService = new FileServiceImpl();
		quotationResults = quotationService.getAllQuotationResults();
		allCountrys = countryService.getAllCountriesFullLoad();
		searchFilter = new OfferteSearchFilter();
	}

	public void setupOfferteDetail(QuotationResult quotationResult) {
		selectedOfferte = quotationResult;
		detail = true;
	}

	public void downloadPdf() throws IOException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) facesContext
				.getExternalContext().getResponse();
		response.reset();
		response.setHeader("Content-Type", "application/pdf");
		OutputStream responseOutputStream = response.getOutputStream();
		File generatedPdfFromDisk = getFileService()
				.getOffertePdfFromFileSystem(selectedOfferte.getPdfFileName());
		InputStream pdfInputStream = new FileInputStream(generatedPdfFromDisk);
		byte[] bytesBuffer = new byte[2048];
		int bytesRead;
		while ((bytesRead = pdfInputStream.read(bytesBuffer)) > 0) {
			responseOutputStream.write(bytesBuffer, 0, bytesRead);
		}
		responseOutputStream.flush();
		pdfInputStream.close();
		responseOutputStream.close();
		facesContext.responseComplete();
	}

	public void resendEmail() {
		try {
			emailService.sendOfferteMail(selectedOfferte);
			MessageUtil.addMessage("Email resend", "Successfully sent email");
		} catch (MessagingException e) {
			MessageUtil
					.addErrorMessage("Could not send email",
							"Error sending email! Contact the system admin if this error persists.");
		}
	}

	public void search() {
		quotationResults = quotationService
				.getQuotationResultsForFilters(searchFilter);
	}

	public QuotationService getQuotationService() {
		return quotationService;
	}

	public void setQuotationService(QuotationService quotationService) {
		this.quotationService = quotationService;
	}

	public List<QuotationResult> getQuotationResults() {
		return quotationResults;
	}

	public void setQuotationResults(List<QuotationResult> quotationResults) {
		this.quotationResults = quotationResults;
	}

	public OfferteSearchFilter getSearchFilter() {
		return searchFilter;
	}

	public void setSearchFilter(OfferteSearchFilter searchFilter) {
		this.searchFilter = searchFilter;
	}

	public List<Country> getAllCountrys() {
		return allCountrys;
	}

	public void setAllCountrys(List<Country> allCountrys) {
		this.allCountrys = allCountrys;
	}

	public boolean isDetail() {
		return detail;
	}

	public void setDetail(boolean detail) {
		this.detail = detail;
	}

	public QuotationResult getSelectedOfferte() {
		return selectedOfferte;
	}

	public void setSelectedOfferte(QuotationResult selectedOfferte) {
		this.selectedOfferte = selectedOfferte;
	}

	public FileService getFileService() {
		return fileService;
	}

	public void setFileService(FileService fileService) {
		this.fileService = fileService;
	}
}
