package com.moorkensam.xlra.controller;

import com.moorkensam.xlra.controller.util.MessageUtil;
import com.moorkensam.xlra.model.security.Permission;
import com.moorkensam.xlra.service.RolePermissionService;

import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;

import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

@ManagedBean
@ViewScoped
public class PermissionController {

  @Inject
  private RolePermissionService rolePermissionService;

  @ManagedProperty("#{msg}")
  private ResourceBundle messageBundle;

  private MessageUtil messageUtil;

  private List<Permission> allPermissions;

  private Permission newPermission;

  @PostConstruct
  public void initialize() {
    messageUtil = MessageUtil.getInstance(messageBundle);
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
    messageUtil.addMessage("message.permission.updated.title", "message.permission.updated.detail",
        permission.getKey());
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
    messageUtil.addMessage("message.permission.created.title", "message.permission.created.detail",
        newPermission.getKey());
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

  public ResourceBundle getMessageBundle() {
    return messageBundle;
  }

  public void setMessageBundle(ResourceBundle messageBundle) {
    this.messageBundle = messageBundle;
  }

  public MessageUtil getMessageUtil() {
    return messageUtil;
  }

  public void setMessageUtil(MessageUtil messageUtil) {
    this.messageUtil = messageUtil;
  }

}
