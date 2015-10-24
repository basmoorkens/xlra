package com.moorkensam.xlra.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import com.moorkensam.xlra.model.security.Role;
import com.moorkensam.xlra.service.RolePermissionService;

@ManagedBean
@ViewScoped
public class RolesController {

	@Inject
	private RolePermissionService roleService;

	private List<Role> allRoles;

	@PostConstruct
	public void initialize() {
		allRoles = roleService.getAllRoles();
	}

}
