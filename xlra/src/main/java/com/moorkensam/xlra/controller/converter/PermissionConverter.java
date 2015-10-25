package com.moorkensam.xlra.controller.converter;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Inject;

import com.moorkensam.xlra.model.security.Permission;
import com.moorkensam.xlra.service.RolePermissionService;

@ManagedBean
@RequestScoped
public class PermissionConverter implements Converter {

	@Inject
	private RolePermissionService permissionService;

	@Override
	public Object getAsObject(FacesContext fc, UIComponent arg1, String value) {
		if (value != null && value.length() > 0) {
			try {
				return getPermissionService().getPermissionById(
						Long.parseLong(value));
			} catch (NumberFormatException e) {
				throw new ConverterException(new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Conversion Error",
						"Not a valid permission."));
			}
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object object) {
		if (object != null) {
			return String.valueOf(((Permission) object).getId());
		}
		return null;
	}

	public RolePermissionService getPermissionService() {
		return permissionService;
	}

	public void setPermissionService(RolePermissionService permissionService) {
		this.permissionService = permissionService;
	}
}
