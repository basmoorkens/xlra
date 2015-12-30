package com.moorkensam.xlra.controller;

import com.moorkensam.xlra.controller.util.MessageUtil;
import com.moorkensam.xlra.model.rate.Country;
import com.moorkensam.xlra.service.CountryService;
import com.moorkensam.xlra.service.util.CustomerUtil;

import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

@ManagedBean
@ViewScoped
public class CountryController {

  @Inject
  private CountryService countryService;

  private List<Country> countries;

  private Country selectedCountry;

  private String detailGridTitle;

  @PostConstruct
  public void init() {
    refreshCountries();
  }

  private void refreshCountries() {
    countries = countryService.getAllCountriesFullLoad();
  }

  /**
   * Saves the country that was edited.
   */
  public void saveEditedCountry() {
    selectedCountry = countryService.updateCountry(selectedCountry);
    MessageUtil.addMessage("Country updated", "Country " + selectedCountry.getEnglishName()
        + " was successfully");
    refreshCountries();
    hideAddDialog();
  }

  /**
   * Delete a country
   * 
   * @param country the country to delete.
   */
  public void deleteCountry(Country country) {
    countryService.deleteCountry(country);
    refreshCountries();
    MessageUtil.addMessage("Country deleted",
        "Successfully deleted the country " + country.getEnglishName());
  }

  public void cancel() {
    selectedCountry = null;
    hideAddDialog();
  }

  /**
   * Setup the page to edit a country.
   * 
   * @param country The country to edit.
   */
  public void setupPageForEdit(Country country) {
    selectedCountry = country;
    detailGridTitle = "Edit country " + country.getEnglishName();
    showAddDialog();
  }

  private void hideAddDialog() {
    RequestContext context = RequestContext.getCurrentInstance();
    context.execute("PF('editCountryDialog').hide();");
  }

  private void showAddDialog() {
    RequestContext context = RequestContext.getCurrentInstance();
    context.execute("PF('editCountryDialog').show();");
  }


  public Country getSelectedCountry() {
    return selectedCountry;
  }

  public void setSelectedCountry(Country selectedCountry) {
    this.selectedCountry = selectedCountry;
  }

  public List<Country> getCountries() {
    return countries;
  }

  public void setCountries(List<Country> countries) {
    this.countries = countries;
  }

  public CountryService getCountryService() {
    return countryService;
  }

  public void setCountryService(CountryService countryService) {
    this.countryService = countryService;
  }

  public String getDetailGridTitle() {
    return detailGridTitle;
  }

  public void setDetailGridTitle(String detailGridTitle) {
    this.detailGridTitle = detailGridTitle;
  }
}
