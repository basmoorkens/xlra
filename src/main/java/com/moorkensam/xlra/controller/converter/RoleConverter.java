package com.moorkensam.xlra.controller.converter;

import com.moorkensam.xlra.model.security.Role;
import com.moorkensam.xlra.service.RolePermissionService;

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
public class RoleConverter implements Converter {

  @Inject
  private RolePermissionService roleService;

  @Override
  public Object getAsObject(FacesContext fc, UIComponent arg1, String value) {
    if (value != null && value.length() > 0) {
      try {
        return getRoleService().getRoleById(Long.parseLong(value));
      } catch (NumberFormatException e) {
        throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
            "Conversion Error", "Not a valid role."));
      }
    }
    return null;
  }

  @Override
  public String getAsString(FacesContext arg0, UIComponent arg1, Object object) {
    if (object != null) {
      return String.valueOf(((Role) object).getId());
    }
    return null;
  }

  public RolePermissionService getRoleService() {
    return roleService;
  }

  public void setRoleService(RolePermissionService roleService) {
    this.roleService = roleService;
  }
}
