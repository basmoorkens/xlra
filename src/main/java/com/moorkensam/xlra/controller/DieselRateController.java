package com.moorkensam.xlra.controller;

import com.moorkensam.xlra.controller.util.MessageUtil;
import com.moorkensam.xlra.model.configuration.Configuration;
import com.moorkensam.xlra.model.configuration.DieselRate;
import com.moorkensam.xlra.model.configuration.Interval;
import com.moorkensam.xlra.model.error.IntervalOverlapException;
import com.moorkensam.xlra.service.ApplicationConfigurationService;
import com.moorkensam.xlra.service.DieselService;

import org.primefaces.context.RequestContext;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

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

  private String detailTitle;

  private boolean editMode;

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
    selectedDieselRate = new DieselRate();
    selectedDieselRate.setInterval(new Interval());
    detailTitle = "Create new diesel rate";
    editMode = false;
    showAddDialog();
  }

  /**
   * Setup the page to display the add dieselrate dialog. Load in the clicked rate and set the
   * dialog in edit mode.
   * 
   * @param dieselRate The rate to load.
   */
  public void setupPageForEditRate(DieselRate dieselRate) {
    selectedDieselRate = dieselRate;
    detailTitle = "Edit rate " + dieselRate.getInterval().toString();
    setEditMode(true);
    showAddDialog();
  }

  public void cancelAddNewRate() {
    selectedDieselRate = null;
    hideAddDialog();
  }

  private void hideAddDialog() {
    RequestContext context = RequestContext.getCurrentInstance();
    context.execute("PF('addDieselDialog').hide();");
  }

  private void showAddDialog() {
    RequestContext context = RequestContext.getCurrentInstance();
    context.execute("PF('addDieselDialog').show();");
  }

  /**
   * Save a new diesel rate.
   */
  public void saveNewDieselRate() {
    if (selectedDieselRate.getId() > 0) {
      updateExistingDieselRate();
    } else {
      persistNewDieselRate();
    }
  }

  private void updateExistingDieselRate() {
    updateDieselRate(selectedDieselRate);
    MessageUtil.addMessage(
        "Diesel rate updated",
        "Updated diesel rate for " + selectedDieselRate.getInterval() + " to "
            + selectedDieselRate.getSurchargePercentage());
    refreshDieselRates();
  }

  private void persistNewDieselRate() {
    try {
      dieselService.createDieselRate(selectedDieselRate);
      refreshDieselRates();
      MessageUtil.addMessage("New diesel rate created",
          "successfully created diesel rate for " + selectedDieselRate.getInterval()
              + " with surcharge percentage " + selectedDieselRate.getSurchargePercentage());
      selectedDieselRate = null;
    } catch (IntervalOverlapException exc) {
      MessageUtil.addErrorMessage("Illegal interval", exc.getBusinessException());
      showAddDialog();
    }
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
