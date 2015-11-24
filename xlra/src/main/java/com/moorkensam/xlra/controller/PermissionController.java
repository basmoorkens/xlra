package com.moorkensam.xlra.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.event.RowEditEvent;

import com.moorkensam.xlra.controller.util.MessageUtil;
import com.moorkensam.xlra.model.security.Permission;
import com.moorkensam.xlra.service.RolePermissionService;

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

	public void onPermissionRowEdit(RowEditEvent event) {
		Permission permission = (Permission) event.getObject();
		MessageUtil.addMessage("Permission updated",
				"Updated " + permission.getKey() + " to ");
		rolePermissionService.updatePermission(permission);
		refreshPermissions();
	}

	private void refreshPermissions() {
		allPermissions = rolePermissionService.getAllPermissions();
	}

	public void saveNewPermission() {
		rolePermissionService.createPermission(newPermission);
		MessageUtil.addMessage("Permission created", "Created permission"
				+ newPermission.getKey());
		newPermission = new Permission();
		refreshPermissions();
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
