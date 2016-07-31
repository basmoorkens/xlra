package com.moorkensam.xlra.controller.delegate;

import com.moorkensam.xlra.model.offerte.QuotationResult;

import org.primefaces.context.RequestContext;

import java.io.IOException;

import javax.faces.context.FacesContext;

public class QuotationControllerDelegate {

  private boolean collapseCustomerPanel;

  private boolean collapseFiltersPanel;

  private boolean collapseSummaryPanel;

  private boolean collapseOptionsPanel;

  private boolean collapseResultPanel;

  public boolean isCollapseResultPanel() {
    return collapseResultPanel;
  }

  public void setCollapseResultPanel(boolean collapseResultPanel) {
    this.collapseResultPanel = collapseResultPanel;
  }

  public boolean isCollapseOptionsPanel() {
    return collapseOptionsPanel;
  }

  public void setCollapseOptionsPanel(boolean collapseOptionsPanel) {
    this.collapseOptionsPanel = collapseOptionsPanel;
  }

  public boolean isCollapseSummaryPanel() {
    return collapseSummaryPanel;
  }

  public void setCollapseSummaryPanel(boolean collapseSummaryPanel) {
    this.collapseSummaryPanel = collapseSummaryPanel;
  }

  public boolean isCollapseFiltersPanel() {
    return collapseFiltersPanel;
  }

  public void setCollapseFiltersPanel(boolean collapseFiltersPanel) {
    this.collapseFiltersPanel = collapseFiltersPanel;
  }

  public boolean isCollapseCustomerPanel() {
    return collapseCustomerPanel;
  }

  public void setCollapseCustomerPanel(boolean collapseCustomerPanel) {
    this.collapseCustomerPanel = collapseCustomerPanel;
  }

  /**
   * Show the customer section.
   */
  public void showCustomerPanel() {
    collapseCustomerPanel = false;
    collapseFiltersPanel = true;
    collapseOptionsPanel = true;
    collapseSummaryPanel = true;
    collapseResultPanel = true;
  }

  /**
   * Show the ratefilter section.
   */
  public void showRateFilterPanel() {
    collapseCustomerPanel = true;
    collapseFiltersPanel = false;
    collapseOptionsPanel = true;
    collapseSummaryPanel = true;
    collapseResultPanel = true;
  }

  /**
   * Show the options section.
   */
  public void showOptionsPanel() {
    collapseCustomerPanel = true;
    collapseFiltersPanel = true;
    collapseOptionsPanel = false;
    collapseSummaryPanel = true;
    collapseResultPanel = true;
  }

  /**
   * Show the summary section.
   */
  public void showSummaryPanel() {
    collapseCustomerPanel = true;
    collapseFiltersPanel = true;
    collapseOptionsPanel = true;
    collapseSummaryPanel = false;
    collapseResultPanel = true;
  }

  /**
   * Show the result section.
   */
  public void showResultPanel() {
    collapseCustomerPanel = true;
    collapseFiltersPanel = true;
    collapseOptionsPanel = true;
    collapseSummaryPanel = true;
    collapseResultPanel = false;
  }

  /**
   * Show the edit recipients dialog.
   */
  public void showEditRecipientsDialog() {
    RequestContext context = RequestContext.getCurrentInstance();
    context.execute("PF('editRecipientsDialog').show();");
  }

  /**
   * Hide the edit recipients dialog.
   */
  public void hideEditRecipientsDialog() {
    RequestContext context = RequestContext.getCurrentInstance();
    context.execute("PF('editRecipientsDialog').hide();");
  }

  /**
   * Go to the offerte detail page in the application.
   * 
   * @throws IOException Thrown when there was a problem redirecting.
   */
  public void goToOfferteDetail(QuotationResult quotationResult) throws IOException {
    FacesContext
        .getCurrentInstance()
        .getExternalContext()
        .redirect(
            FacesContext.getCurrentInstance().getExternalContext().getApplicationContextPath()
                + "/views/user/offerteOverview.xhtml?offerteKey="
                + quotationResult.getOfferteUniqueIdentifier());
  }
}
