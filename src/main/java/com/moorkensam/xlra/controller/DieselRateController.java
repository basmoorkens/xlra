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
import com.moorkensam.xlra.model.configuration.DieselRate;
import com.moorkensam.xlra.model.configuration.Interval;
import com.moorkensam.xlra.service.ApplicationConfigurationService;
import com.moorkensam.xlra.service.DieselService;

@ManagedBean
@ViewScoped
public class DieselRateController {

  @Inject
  private ApplicationConfigurationService applicationConfigurationService;

  @Inject
  private DieselService dieselService;

  private List<DieselRate> dieselRates;

  private DieselRate selectedDieselRate;

  private Configuration configuration;

  private BigDecimal currentDieselValue;

  private boolean showAddDieselRate;

  private DieselRate newDieselRate;

  /**
   * Initialize logic for the controller.
   */
  @PostConstruct
  public void initPage() {
    refreshDieselRates();
    configuration = getApplicationConfigurationService().getConfiguration();
    setupCurrentRates();
  }

  private void setupCurrentRates() {
    currentDieselValue = configuration.getCurrentDieselPrice();
  }

  private void refreshDieselRates() {
    dieselRates = dieselService.getAllDieselRates();
  }

  private void updateDieselRate(DieselRate rate) {
    dieselService.updateDieselRate(rate);
  }

  /**
   * Sets up the page for a new rate.
   */
  public void setupPageForNewRate() {
    newDieselRate = new DieselRate();
    newDieselRate.setInterval(new Interval());
    showAddDieselRate = true;
  }

  public void cancelAddNewRate() {
    showAddDieselRate = false;
    newDieselRate = null;
  }

  /**
   * Save a new diesel rate.
   */
  public void saveNewDieselRate() {
    dieselService.createDieselRate(newDieselRate);
    refreshDieselRates();
    showAddDieselRate = false;
    MessageUtil.addMessage("New diesel rate created",
        "successfully created diesel rate for " + newDieselRate.getInterval()
            + " with surcharge percentage " + newDieselRate.getSurchargePercentage());
    newDieselRate = null;
  }

  /**
   * Deletes the given dieselrate.
   * 
   * @param rate The rate to delete.
   */
  public void deleteDieselRate(DieselRate rate) {
    dieselService.deleteDieselRate(rate);
    refreshDieselRates();
    MessageUtil.addMessage("Successfully deleted rate", "Sucessfully deleted diesel rate.");
  }

  /**
   * Updates the current selected diesel rate.
   */
  public void updateCurrentDieselRate() {
    if (configuration.getCurrentDieselPrice() != getCurrentDieselValue()) {
      dieselService.updateCurrentDieselValue(getCurrentDieselValue());
      MessageUtil.addMessage("Current diesel price", "Updated current diesel price to "
          + getCurrentDieselValue());
      setupCurrentRates();
    } else {
      MessageUtil.addMessage("Current diesel price",
          "The new price is the same as the old, not updating.");
    }
  }

  /**
   * Triggered when a dieselrate is edited from the datatable.
   * 
   * @param event The event that triggered this.
   */
  public void onDieselChargeRowEdit(RowEditEvent event) {
    DieselRate newRate = (DieselRate) event.getObject();

    updateDieselRate(newRate);
    MessageUtil.addMessage(
        "Diesel rate updated",
        "Updated diesel rate for " + newRate.getInterval() + " to "
            + newRate.getSurchargePercentage());
    refreshDieselRates();
  }

  public List<DieselRate> getDieselRates() {
    return dieselRates;
  }

  public void setDieselRates(List<DieselRate> dieselRates) {
    this.dieselRates = dieselRates;
  }

  public DieselRate getSelectedDieselRate() {
    return selectedDieselRate;
  }

  public void setSelectedDieselRate(DieselRate selectedDieselRate) {
    this.selectedDieselRate = selectedDieselRate;
  }

  public BigDecimal getCurrentDieselValue() {
    return currentDieselValue;
  }

  public void setCurrentDieselValue(BigDecimal currentDieselValue) {
    this.currentDieselValue = currentDieselValue;
  }

  public ApplicationConfigurationService getApplicationConfigurationService() {
    return applicationConfigurationService;
  }

  public void setApplicationConfigurationService(
      ApplicationConfigurationService applicationConfigurationService) {
    this.applicationConfigurationService = applicationConfigurationService;
  }

  public DieselService getDieselService() {
    return dieselService;
  }

  public void setDieselService(DieselService dieselService) {
    this.dieselService = dieselService;
  }

  public boolean isShowAddDieselRate() {
    return showAddDieselRate;
  }

  public void setShowAddDieselRate(boolean showAddDieselRate) {
    this.showAddDieselRate = showAddDieselRate;
  }

  public DieselRate getNewDieselRate() {
    return newDieselRate;
  }

  public void setNewDieselRate(DieselRate newDieselRate) {
    this.newDieselRate = newDieselRate;
  }
}
