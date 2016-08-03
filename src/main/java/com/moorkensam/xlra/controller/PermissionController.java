package com.moorkensam.xlra.controller;

import com.moorkensam.xlra.controller.util.MessageUtil;
import com.moorkensam.xlra.model.security.Permission;
import com.moorkensam.xlra.service.RolePermissionService;

import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

@ManagedBean
@ViewScoped
public class PermissionController {

  @Inject
  private RolePermissionService rolePermissionService;

  private List<Permission> allPermissions;

  private Permission newPermission;

  @PostConstruct
  public void initialize() {
    newPermission = new Permission();
    refreshPermissions();
  }

  /**
   * Triggered when a permission is selected for edit.
   * 
   * @param event The triggering event.
   */
  public void onPermissionRowEdit(RowEditEvent event) {
    Permission permission = (Permission) event.getObject();
    MessageUtil.addMessage("Permission updated", "Updated " + permission.getKey() + " to ");
    rolePermissionService.updatePermission(permission);
    refreshPermissions();
  }

  private void refreshPermissions() {
    allPermissions = rolePermissionService.getAllPermissions();
  }

  /**
   * Save the new permission.
   */
  public void saveNewPermission() {
    rolePermissionService.createPermission(newPermission);
    MessageUtil.addMessage("Permission created", "Created permission" + newPermission.getKey());
    newPermission = new Permission();
    refreshPermissions();
  }

  public void cancelAddNewPermission() {
    newPermission = null;
    hideAddDialog();
  }

  private void hideAddDialog() {
    RequestContext context = RequestContext.getCurrentInstance();
    context.execute("PF('addPermissionDialog').hide();");
  }

  public List<Permission> getAllPermissions() {
    return allPermissions;
  }

  public void setAllPermissions(List<Permission> allPermissions) {
    this.allPermissions = allPermissions;
  }

  public Permission getNewPermission() {
    return newPermission;
  }

  public void setNewPermission(Permission newPermission) {
    this.newPermission = newPermission;
  }

}
