package com.moorkensam.xlra.controller.converter;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Inject;

import com.moorkensam.xlra.model.customer.Customer;
import com.moorkensam.xlra.service.CustomerService;

@ManagedBean
@RequestScoped
public class CustomerConverter implements Converter {

	@Inject
	private CustomerService customerService;

	@Override
	public Object getAsObject(FacesContext fc, UIComponent arg1, String value) {
		if (value != null && value.length() > 0) {
			try {
				return customerService.getCustomerById(Long.parseLong(value));
			} catch (NumberFormatException e) {
				throw new ConverterException(new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Conversion Error",
						"Not a valid  customer."));
			}
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object object) {
		if (object != null) {
			Customer fc = (Customer) object;
			return fc.getId() + "";
		}
		return null;
	}
}
