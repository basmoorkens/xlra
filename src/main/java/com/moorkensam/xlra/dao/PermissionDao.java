package com.moorkensam.xlra.dao;

import com.moorkensam.xlra.model.security.Permission;

import java.util.List;

public interface PermissionDao {

  public Permission getPermissionById(long id);

  public List<Permission> getAllPermissions();

  public void createPermission(Permission permission);

  public Permission updatePermission(Permission permission);

}
