package com.moorkensam.xlra.dao.impl;

import com.moorkensam.xlra.dao.BaseDAO;
import com.moorkensam.xlra.dao.ZoneDAO;
import com.moorkensam.xlra.model.rate.Zone;

public class ZoneDAOImpl extends BaseDAO implements ZoneDAO {

	@Override
	public void createZone(Zone zone) {
		getEntityManager().persist(zone);
	}

	@Override
	public void updateZone(Zone zone) {
		getEntityManager().merge(zone);
	}

}
