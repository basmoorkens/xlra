package com.moorkensam.xlra.controller.converter;

import java.util.Arrays;

import com.moorkensam.xlra.controller.LocaleController;
import com.moorkensam.xlra.model.rate.Country;
import com.moorkensam.xlra.service.CountryService;
import com.moorkensam.xlra.service.util.LocaleUtil;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
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

	@ManagedProperty("#{localeController}")
	private LocaleController localeController;

	private LocaleUtil localeUtil;

	@Override
	public Object getAsObject(FacesContext fc, UIComponent arg1, String value) {
		if (value != null && value.length() > 0) {
			try {
				localeUtil = new LocaleUtil();
				Country country = countryService.getCountryById(Long
						.parseLong(value));
				localeUtil.fillInCountryi8nNameByLanguage(
						Arrays.asList(country), localeController.getLanguage());
				return country;
			} catch (NumberFormatException e) {
				throw new ConverterException(new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Conversion Error",
						"Not a valid country."));
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

	public LocaleUtil getLocaleUtil() {
		return localeUtil;
	}

	public void setLocaleUtil(LocaleUtil localeUtil) {
		this.localeUtil = localeUtil;
	}

	public LocaleController getLocaleController() {
		return localeController;
	}

	public void setLocaleController(LocaleController localeController) {
		this.localeController = localeController;
	}
}
