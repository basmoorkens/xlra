package com.moorkensam.xlra.dao.impl;

import java.util.List;

import javax.persistence.Query;

import com.moorkensam.xlra.dao.BaseDAO;
import com.moorkensam.xlra.dao.PermissionDAO;
import com.moorkensam.xlra.model.security.Permission;

public class PermissionDAOImpl extends BaseDAO implements PermissionDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<Permission> getAllPermissions() {
		Query query = getEntityManager().createNamedQuery("Permission.findAll");
		return (List<Permission>) query.getResultList();
	}

	@Override
	public void createPermission(Permission p) {
		getEntityManager().persist(p);
	}

	@Override
	public Permission updatePermission(Permission p) {
		return getEntityManager().merge(p);
	}

	@Override
	public Permission getPermissionById(long id) {
		Query query = getEntityManager()
				.createNamedQuery("Permission.findById");
		query.setParameter("id", id);
		return (Permission) query.getSingleResult();
	}
}
