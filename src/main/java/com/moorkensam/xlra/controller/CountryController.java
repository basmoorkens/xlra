package com.moorkensam.xlra.controller;

import com.moorkensam.xlra.controller.util.MessageUtil;
import com.moorkensam.xlra.model.rate.Country;
import com.moorkensam.xlra.service.CountryService;

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

  @PostConstruct
  public void init() {
    refreshCountries();
  }

  private void refreshCountries() {
    countries = countryService.getAllCountriesFullLoad();
  }

  /**
   * Executed when a country is edited.
   * 
   * @param event The event that triggered the update.
   */
  public void onCountryRowEdit(RowEditEvent event) {
    Country country = (Country) event.getObject();
    MessageUtil.addMessage("Country updated", "Country " + country.getShortName()
        + " was successfully");
    country = countryService.updateCountry(country);
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
}
