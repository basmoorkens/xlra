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
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

@ManagedBean
@ViewScoped
public class DieselRateController {

  @Inject
  private ApplicationConfigurationService applicationConfigurationService;

  @ManagedProperty("#{msg}")
  private ResourceBundle messageBundle;

  private MessageUtil messageUtil;

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
    messageUtil = MessageUtil.getInstance(messageBundle);
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
    detailTitle = messageUtil.lookupI8nStringAndInjectParams("diesel.create.popup.title", "");
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
    detailTitle =
        messageUtil.lookupI8nStringAndInjectParams("diesel.edit.popup.title", dieselRate
            .getInterval().toString());
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
    messageUtil.addMessage("message.diesel.rate.updated.title",
        "message.diesel.rate.updated.detail" + selectedDieselRate.getInterval() + "",
        selectedDieselRate.getSurchargePercentage() + "");
    refreshDieselRates();
  }

  private void persistNewDieselRate() {
    try {
      dieselService.createDieselRate(selectedDieselRate);
      refreshDieselRates();
      messageUtil.addMessage("message.diesel.rate.create.title",
          "message.diesel.rate.create.detail", selectedDieselRate.getInterval() + "",
          selectedDieselRate.getSurchargePercentage() + "");
      selectedDieselRate = null;
    } catch (IntervalOverlapException exc) {
      messageUtil.addErrorMessage("message.chf.illegal.interval.title", exc.getBusinessException());
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
    messageUtil.addMessage("message.diesel.delete.title", "message.diesel.delete.detail", rate
        .getInterval().toIntString());
  }

  /**
   * Updates the current selected diesel rate.
   */
  public void updateCurrentDieselRate() {
    if (configuration.getCurrentDieselPrice() != getCurrentDieselValue()) {
      dieselService.updateCurrentDieselValue(getCurrentDieselValue());
      messageUtil.addMessage("message.current.diesel.price.title",
          "message.current.diesel.price.detail", getCurrentDieselValue() + "");
      setupCurrentRates();
    } else {
      messageUtil.addMessage("message.current.diesel.price.title", "message.price.same.as.old");
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
