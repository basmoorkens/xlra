package com.moorkensam.xlra.controller.converter;

import com.moorkensam.xlra.model.rate.Country;
import com.moorkensam.xlra.service.CountryService;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Inject;

@ManagedBean
@RequestScoped
public class CountryConverter implements Converter {

  @Inject
  private CountryService countryService;

  @Override
  public Object getAsObject(FacesContext fc, UIComponent arg1, String value) {
    if (value != null && value.length() > 0) {
      try {
        return countryService.getCountryById(Long.parseLong(value));
      } catch (NumberFormatException e) {
        throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
            "Conversion Error", "Not a valid country."));
      }
    }
    return null;
  }

  @Override
  public String getAsString(FacesContext arg0, UIComponent arg1, Object object) {
    if (object != null) {
      Country country = (Country) object;
      return country.getId() + "";
    }
    return null;
  }

  public CountryService getCountryService() {
    return countryService;
  }

  public void setCountryService(CountryService countryService) {
    this.countryService = countryService;
  }
}
