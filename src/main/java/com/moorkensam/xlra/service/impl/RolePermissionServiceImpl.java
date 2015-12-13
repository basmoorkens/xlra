package com.moorkensam.xlra.service.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.moorkensam.xlra.dao.PermissionDao;
import com.moorkensam.xlra.dao.RoleDao;
import com.moorkensam.xlra.model.security.Permission;
import com.moorkensam.xlra.model.security.Role;
import com.moorkensam.xlra.service.RolePermissionService;

@Stateless
public class RolePermissionServiceImpl implements RolePermissionService {

  @Inject
  private RoleDao roleDao;

  @Inject
  private PermissionDao permissionDao;

  @Override
  public void createRole(Role role) {
    roleDao.createRole(role);
  }

  @Override
  public Role updateRole(Role role) {
    return roleDao.updateRole(role);
  }

  @Override
  public List<Role> getAllRoles() {
    return roleDao.getAllRoles();
  }

  @Override
  public List<Permission> getAllPermissions() {
    return permissionDao.getAllPermissions();
  }

  @Override
  public void createPermission(Permission perm) {
    permissionDao.createPermission(perm);
  }

  @Override
  public Permission updatePermission(Permission perm) {
    return permissionDao.updatePermission(perm);
  }

  @Override
  public Permission getPermissionById(long id) {
    return permissionDao.getPermissionById(id);
  }

  @Override
  public Role getRoleById(long id) {
    return roleDao.getRoleById(id);
  }

}
