package com.moorkensam.xlra.controller.converter;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import com.moorkensam.xlra.controller.ManageRatesController;
import com.moorkensam.xlra.model.rate.RateFile;

@ManagedBean
@RequestScoped
public class RateFileConverter implements Converter {

	@ManagedProperty(value = "#{manageRatesController}")
	private ManageRatesController controller;

	@Override
	public Object getAsObject(FacesContext fc, UIComponent arg1, String value) {
		if (value != null && value.length() > 0) {
			try {
				return getController().getRateFileById(Long.parseLong(value));
			} catch (NumberFormatException e) {
				throw new ConverterException(new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Conversion Error",
						"Not a valid rate file."));
			}
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object object) {
		if (object != null) {
			return String.valueOf(((RateFile) object).getId());
		}
		return null;
	}

	public ManageRatesController getController() {
		return controller;
	}

	public void setController(ManageRatesController controller) {
		this.controller = controller;
	}

}
