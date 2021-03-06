package com.moorkensam.xlra.controller;

import com.moorkensam.xlra.controller.util.MessageUtil;
import com.moorkensam.xlra.model.error.UnAuthorizedAccessException;
import com.moorkensam.xlra.model.offerte.QuotationResult;
import com.moorkensam.xlra.model.rate.Country;
import com.moorkensam.xlra.service.CountryService;
import com.moorkensam.xlra.service.EmailService;
import com.moorkensam.xlra.service.FileService;
import com.moorkensam.xlra.service.QuotationService;
import com.moorkensam.xlra.service.impl.FileServiceImpl;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;

@ManagedBean
@ViewScoped
public class OfferteOverviewController {

  @Inject
  private QuotationService quotationService;

  @Inject
  private CountryService countryService;

  @Inject
  private EmailService emailService;

  @ManagedProperty("#{msg}")
  private ResourceBundle messageBundle;

  private MessageUtil messageUtil;

  private FileService fileService;

  private LazyDataModel<QuotationResult> model;

  private QuotationResult selectedOfferte;

  private List<Country> allCountrys;

  /**
   * Initializes the controller.
   */
  @PostConstruct
  public void initialize() {
    messageUtil = MessageUtil.getInstance(messageBundle);
    fileService = new FileServiceImpl();
    allCountrys = countryService.getAllCountriesFullLoad();
    checkAndLoadOfferteIfPresent();
    initModel();
  }

  private void initModel() {
    model = new LazyDataModel<QuotationResult>() {

      private static final long serialVersionUID = 1130768020033685725L;

      @Override
      public List<QuotationResult> load(int first, int pageSize, String sortField,
          SortOrder sortOrder, Map<String, Object> filters) {
        List<QuotationResult> lazyloadedOffertes =
            quotationService.getLazyloadedOffertes(first, pageSize, sortField, sortOrder, filters);
        model.setRowCount(quotationService.countOffertes());
        return lazyloadedOffertes;
      }

    };
  }

  private void checkAndLoadOfferteIfPresent() {
    String offerteKey =
        FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
            .get("offerteKey");
    if (StringUtils.isNotBlank(offerteKey)) {
      try {
        selectedOfferte = quotationService.getOfferteByOfferteKey(offerteKey);
        showDetailDialog();
      } catch (UnAuthorizedAccessException e) {
        messageUtil.addErrorMessage("message.offerte.unauthorized.access.title",
            e.getBusinessException(), e.getExtraArguments().get(0));
      }
    }
  }

  /**
   * Setup the page to view the offerte details.
   * 
   * @param quotationResult The offerte to view the details of.
   */
  public void setupOfferteDetail(QuotationResult quotationResult) {
    try {
      selectedOfferte = quotationService.getFullOfferteById(quotationResult.getId());
      showDetailDialog();
    } catch (UnAuthorizedAccessException e) {
      messageUtil.addErrorMessage("message.offerte.unauthorized.access.title",
          e.getBusinessException(), e.getExtraArguments().get(0));
    }
  }

  public void hideDetailDialog() {
    RequestContext context = RequestContext.getCurrentInstance();
    context.execute("PF('detailOfferteDialog').hide();");
  }

  public void showDetailDialog() {
    RequestContext context = RequestContext.getCurrentInstance();
    context.execute("PF('detailOfferteDialog').show();");
  }

  /**
   * Serve the pdf from the filesystem.
   * 
   * @throws IOException Thrown when the pdf could not be read in.
   */
  public void downloadPdf() throws IOException {
    FacesContext facesContext = FacesContext.getCurrentInstance();
    HttpServletResponse response =
        (HttpServletResponse) facesContext.getExternalContext().getResponse();
    response.reset();
    response.setHeader("Content-Type", "application/pdf");
    OutputStream responseOutputStream = response.getOutputStream();
    File generatedPdfFromDisk =
        getFileService().getOffertePdfFromFileSystem(selectedOfferte.getPdfFileName());
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

  /**
   * Resend an email for the offerte.
   */
  public void resendEmail() {
    try {
      emailService.sendOfferteMail(selectedOfferte);
      selectedOfferte = quotationService.getFullOfferteById(selectedOfferte.getId());
      messageUtil.addMessage("message.email.resend.title", "message.email.resend.detail");
    } catch (MessagingException e) {
      messageUtil.addErrorMessage("message.email.resend.failed.title",
          "message.email.resend.failed.detail");
    } catch (UnAuthorizedAccessException e) {
      messageUtil.addErrorMessage("message.offerte.unauthorized.access.title",
          e.getBusinessException(), e.getExtraArguments().get(0));
    }
  }

  public QuotationService getQuotationService() {
    return quotationService;
  }

  public void setQuotationService(QuotationService quotationService) {
    this.quotationService = quotationService;
  }

  public List<Country> getAllCountrys() {
    return allCountrys;
  }

  public void setAllCountrys(List<Country> allCountrys) {
    this.allCountrys = allCountrys;
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

  public LazyDataModel<QuotationResult> getModel() {
    return model;
  }

  public void setModel(LazyDataModel<QuotationResult> model) {
    this.model = model;
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
