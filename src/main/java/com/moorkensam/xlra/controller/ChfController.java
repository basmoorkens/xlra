package com.moorkensam.xlra.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.event.RowEditEvent;

import com.moorkensam.xlra.controller.util.MessageUtil;
import com.moorkensam.xlra.model.configuration.Configuration;
import com.moorkensam.xlra.model.configuration.CurrencyRate;
import com.moorkensam.xlra.model.configuration.Interval;
import com.moorkensam.xlra.model.configuration.XlraCurrency;
import com.moorkensam.xlra.service.ApplicationConfigurationService;
import com.moorkensam.xlra.service.CurrencyService;

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

  private CurrencyRate newCurrencyRate;

  private boolean showAddCurrencyPanel;

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
    newCurrencyRate = new CurrencyRate();
    newCurrencyRate.setCurrencyType(XlraCurrency.CHF);
    newCurrencyRate.setInterval(new Interval());
    showAddCurrencyPanel = true;
  }

  /**
   * Save a new currency rate.
   */
  public void saveNewCurrencyRate() {
    currencyService.createCurrencyRate(newCurrencyRate);
    showAddCurrencyPanel = false;
    MessageUtil.addMessage("Swiss franc rate created",
        "Successfully created swiss franc rate for " + newCurrencyRate.getInterval()
            + " with surcharge percentage " + newCurrencyRate.getSurchargePercentage());
    refreshCurrencyRates();
    newCurrencyRate = null;
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
    showAddCurrencyPanel = false;
    newCurrencyRate = null;
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

  /**
   * Executed when the onedit event is triggered.
   * 
   * @param event The event that was triggered.
   */
  public void onChfCurrencyRateRowEdit(RowEditEvent event) {
    CurrencyRate newRate = (CurrencyRate) event.getObject();

    updateChfRate(newRate);
    MessageUtil.addMessage(
        "Swiss franc rate updated",
        "Updated swiss franc rate for " + newRate.getInterval() + " to "
            + newRate.getSurchargePercentage());
    refreshCurrencyRates();
  }

  private void updateChfRate(CurrencyRate rate) {
    currencyService.updateCurrencyRate(rate);
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

  public CurrencyRate getNewCurrencyRate() {
    return newCurrencyRate;
  }

  public void setNewCurrencyRate(CurrencyRate newCurrencyRate) {
    this.newCurrencyRate = newCurrencyRate;
  }

  public boolean isShowAddCurrencyPanel() {
    return showAddCurrencyPanel;
  }

  public void setShowAddCurrencyPanel(boolean showAddCurrencyPanel) {
    this.showAddCurrencyPanel = showAddCurrencyPanel;
  }
}
