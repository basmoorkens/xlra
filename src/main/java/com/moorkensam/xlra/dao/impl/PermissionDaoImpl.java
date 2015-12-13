package com.moorkensam.xlra.dao.impl;

import java.util.List;

import javax.persistence.Query;

import com.moorkensam.xlra.dao.BaseDao;
import com.moorkensam.xlra.dao.PermissionDao;
import com.moorkensam.xlra.model.security.Permission;

public class PermissionDaoImpl extends BaseDao implements PermissionDao {

  @SuppressWarnings("unchecked")
  @Override
  public List<Permission> getAllPermissions() {
    Query query = getEntityManager().createNamedQuery("Permission.findAll");
    return (List<Permission>) query.getResultList();
  }

  @Override
  public void createPermission(Permission permission) {
    getEntityManager().persist(permission);
  }

  @Override
  public Permission updatePermission(Permission permission) {
    return getEntityManager().merge(permission);
  }

  @Override
  public Permission getPermissionById(long id) {
    Query query = getEntityManager().createNamedQuery("Permission.findById");
    query.setParameter("id", id);
    return (Permission) query.getSingleResult();
  }
}
