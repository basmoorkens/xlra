package com.moorkensam.xlra.controller;

import com.moorkensam.xlra.controller.util.MessageUtil;
import com.moorkensam.xlra.model.configuration.Configuration;
import com.moorkensam.xlra.model.configuration.CurrencyRate;
import com.moorkensam.xlra.model.configuration.Interval;
import com.moorkensam.xlra.model.configuration.XlraCurrency;
import com.moorkensam.xlra.model.error.IntervalOverlapException;
import com.moorkensam.xlra.service.ApplicationConfigurationService;
import com.moorkensam.xlra.service.CurrencyService;

import org.primefaces.context.RequestContext;

import java.math.BigDecimal;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

@ManagedBean
@ViewScoped
public class ChfController {

  @Inject
  private ApplicationConfigurationService applicationConfigurationService;

  @Inject
  private CurrencyService currencyService;

  @ManagedProperty("#{msg}")
  private ResourceBundle messageBundle;

  private MessageUtil messageUtil;

  private List<CurrencyRate> chfRates;

  private CurrencyRate selectedChfRate;

  private BigDecimal currentChfValue;

  private Configuration configuration;

  private String detailTitle;

  private boolean editMode = false;

  @PostConstruct
  public void initPage() {
    refreshCurrencyRates();
    messageUtil = MessageUtil.getInstance(messageBundle);
  }

  private void refreshCurrencyRates() {
    chfRates = currencyService.getAllChfRates();
    configuration = applicationConfigurationService.getConfiguration();
    setupCurrentRates();
  }

  /**
   * Setup the page for a new currency rate.
   */
  public void setupPageForNewCurrencyRate() {
    selectedChfRate = new CurrencyRate();
    selectedChfRate.setCurrencyType(XlraCurrency.CHF);
    selectedChfRate.setInterval(new Interval());
    detailTitle = messageUtil.lookupI8nStringAndInjectParams("chfrates.create.popup.title", "");
    editMode = false;
    showAddDialog();
  }

  /**
   * Set up the page to edit a chf rate.
   * 
   * @param currencyRate The chf rate to edit.
   */
  public void setupPageForEditChfRate(CurrencyRate currencyRate) {
    selectedChfRate = currencyRate;
    detailTitle =
        messageUtil.lookupI8nStringAndInjectParams("chfrates.edit.popup.title", currencyRate
            .getInterval().toString());
    editMode = true;
    showAddDialog();
  }

  /**
   * Save a new currency rate.
   */
  public void saveNewCurrencyRate() {
    if (selectedChfRate.getId() > 0) {
      updateSelectedRate();
    } else {
      persistNewRate();
    }
  }

  private void persistNewRate() {
    try {
      currencyService.createCurrencyRate(selectedChfRate);
      messageUtil.addMessage("message.chf.rate.created.title", "message.chf.rate.created.detail",
          selectedChfRate.getInterval() + "", selectedChfRate.getSurchargePercentage() + "");
      refreshCurrencyRates();
      selectedChfRate = null;
      hideAddDialog();
    } catch (IntervalOverlapException e) {
      messageUtil.addErrorMessage("message.chf.illegal.interval.title", e.getBusinessException());
      showAddDialog();
    }
  }

  private void updateSelectedRate() {
    currencyService.updateCurrencyRate(selectedChfRate);
    messageUtil.addMessage("message.chf.rate.updated.title", "message.chf.rate.updated.detail"
        + selectedChfRate.getInterval() + "", selectedChfRate.getSurchargePercentage() + "");
    refreshCurrencyRates();
  }

  private void hideAddDialog() {
    RequestContext context = RequestContext.getCurrentInstance();
    context.execute("PF('addChfDialog').hide();");
  }

  private void showAddDialog() {
    RequestContext context = RequestContext.getCurrentInstance();
    context.execute("PF('addChfDialog').show();");
  }

  /**
   * Delete a currency rate.
   * 
   * @param rate The rate to delete.
   */
  public void deleteChfRate(CurrencyRate rate) {
    currencyService.deleteCurrencyRate(rate);
    refreshCurrencyRates();
    messageUtil.addMessage("message.chf.delete.title", "message.chf.delete.detail", rate
        .getInterval().toIntString());
  }

  public void cancelAddNewCurrencyRate() {
    selectedChfRate = null;
    hideAddDialog();
  }

  /**
   * Update the currenctly selected chf rate.
   */
  public void updateCurrentChfRate() {
    if (isValidCurrentChfValue()) {
      currencyService.updateCurrentChfValue(getCurrentChfValue());
      messageUtil.addMessage("message.current.chf.price", "message.chf.updated.to",
          getCurrentChfValue() + "");
      setupCurrentRates();
    }
  }

  private boolean isValidCurrentChfValue() {
    if (getCurrentChfValue() == null) {
      messageUtil.addErrorMessage("message.empty.value", "message.chf.currentvalue.null");
      return false;
    }
    if (configuration.getCurrentChfValue() == getCurrentChfValue()) {
      messageUtil.addMessage("message.current.chf.price", "message.price.same.as.old");
      return false;
    }
    return true;

  }

  private void setupCurrentRates() {
    currentChfValue = configuration.getCurrentChfValue();
  }

  public List<CurrencyRate> getChfRates() {
    return chfRates;
  }

  public CurrencyRate getSelectedChfRate() {
    return selectedChfRate;
  }

  public void setSelectedChfRate(CurrencyRate selectedChfRate) {
    this.selectedChfRate = selectedChfRate;
  }

  public Configuration getConfiguration() {
    return configuration;
  }

  public BigDecimal getCurrentChfValue() {
    return currentChfValue;
  }

  public void setCurrentChfValue(BigDecimal currentChfValue) {
    this.currentChfValue = currentChfValue;
  }

  public CurrencyService getCurrencyService() {
    return currencyService;
  }

  public void setCurrencyService(CurrencyService currencyService) {
    this.currencyService = currencyService;
  }

  public String getDetailTitle() {
    return detailTitle;
  }

  public void setDetailTitle(String detailTitle) {
    this.detailTitle = detailTitle;
  }

  public boolean isEditMode() {
    return editMode;
  }

  public void setEditMode(boolean editMode) {
    this.editMode = editMode;
  }

  public MessageUtil getMessageUtil() {
    return messageUtil;
  }

  public void setMessageUtil(MessageUtil messageUtil) {
    this.messageUtil = messageUtil;
  }

  public ResourceBundle getMessageBundle() {
    return messageBundle;
  }

  public void setMessageBundle(ResourceBundle messageBundle) {
    this.messageBundle = messageBundle;
  }
}
