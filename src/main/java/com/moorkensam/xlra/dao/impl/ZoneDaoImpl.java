package com.moorkensam.xlra.dao.impl;

import com.moorkensam.xlra.dao.BaseDao;
import com.moorkensam.xlra.dao.ZoneDao;
import com.moorkensam.xlra.model.rate.Zone;

public class ZoneDaoImpl extends BaseDao implements ZoneDao {

  @Override
  public void createZone(Zone zone) {
    getEntityManager().persist(zone);
  }

  @Override
  public void updateZone(Zone zone) {
    getEntityManager().merge(zone);
  }

}
