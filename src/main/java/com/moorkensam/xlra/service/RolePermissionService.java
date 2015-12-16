package com.moorkensam.xlra.service;

import com.moorkensam.xlra.model.security.Permission;
import com.moorkensam.xlra.model.security.Role;

import java.util.List;

public interface RolePermissionService {

  public void createRole(Role role);

  public Role updateRole(Role role);

  public List<Role> getAllRoles();

  public Role getRoleById(long id);

  public List<Permission> getAllPermissions();

  public void createPermission(Permission perm);

  public Permission updatePermission(Permission perm);

  public Permission getPermissionById(long id);

}
