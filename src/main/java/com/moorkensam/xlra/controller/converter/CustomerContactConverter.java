package com.moorkensam.xlra.controller.converter;

import com.moorkensam.xlra.model.customer.CustomerContact;
import com.moorkensam.xlra.service.CustomerService;

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
public class CustomerContactConverter implements Converter {

  @Inject
  private CustomerService customerService;

  @Override
  public Object getAsObject(FacesContext context, UIComponent component, String value) {
    if (value != null && value.length() > 0) {
      try {
        return customerService.getCustomerContactById(Long.parseLong(value));
      } catch (NumberFormatException e) {
        throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
            "Conversion Error", "Not a valid  customercontat."));
      }
    }
    return null;
  }

  @Override
  public String getAsString(FacesContext context, UIComponent component, Object value) {
    CustomerContact contact = (CustomerContact) value;
    return contact.getId() + "";
  }

}
