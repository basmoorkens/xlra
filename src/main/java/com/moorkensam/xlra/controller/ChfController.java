package com.moorkensam.xlra.controller;

import com.moorkensam.xlra.controller.util.MessageUtil;
import com.moorkensam.xlra.model.configuration.Configuration;
import com.moorkensam.xlra.model.configuration.CurrencyRate;
import com.moorkensam.xlra.model.configuration.Interval;
import com.moorkensam.xlra.model.configuration.XlraCurrency;
import com.moorkensam.xlra.model.customer.Customer;
import com.moorkensam.xlra.model.error.IntervalOverlapException;
import com.moorkensam.xlra.service.ApplicationConfigurationService;
import com.moorkensam.xlra.service.CurrencyService;
import com.moorkensam.xlra.service.util.CustomerUtil;

import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

@ManagedBean
@ViewScoped
public class ChfController {

  @Inject
  private ApplicationConfigurationService applicationConfigurationService;

  @Inject
  private CurrencyService currencyService;

  private List<CurrencyRate> chfRates;

  private CurrencyRate selectedChfRate;

  private BigDecimal currentChfValue;

  private Configuration configuration;

  private String detailTitle;

  private boolean editMode = false;

  @PostConstruct
  public void initPage() {
    refreshCurrencyRates();
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
    detailTitle = "Create new Chf rate";
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
    detailTitle = "Edit chf rate " + currencyRate.getInterval().toString();
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
      MessageUtil.addMessage("Swiss franc rate created",
          "Successfully created swiss franc rate for " + selectedChfRate.getInterval()
              + " with surcharge percentage " + selectedChfRate.getSurchargePercentage());
      refreshCurrencyRates();
      selectedChfRate = null;
      hideAddDialog();
    } catch (IntervalOverlapException e) {
      MessageUtil.addErrorMessage("Illegal interval", e.getBusinessException());
      showAddDialog();
    }
  }

  private void updateSelectedRate() {
    currencyService.updateCurrencyRate(selectedChfRate);
    MessageUtil.addMessage("Swiss franc rate updated", "Updated swiss franc rate for "
        + selectedChfRate.getInterval() + " to " + selectedChfRate.getSurchargePercentage());
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
    MessageUtil.addMessage("Successfully deleted rate", "Sucessfully deleted Swiss franc rate.");
  }

  public void cancelAddNewCurrencyRate() {
    selectedChfRate = null;
    hideAddDialog();
  }

  /**
   * Update the currenctly selected chf rate.
   */
  public void updateCurrentChfRate() {
    if (configuration.getCurrentChfValue() != getCurrentChfValue()) {
      currencyService.updateCurrentChfValue(getCurrentChfValue());
      MessageUtil.addMessage("Current swiss franc price", "Updated current swiss franc price to "
          + getCurrentChfValue());
      setupCurrentRates();
    } else {
      MessageUtil.addMessage("Current swiss franc price",
          "The new price is the same as the old, not updating.");
    }
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
}
