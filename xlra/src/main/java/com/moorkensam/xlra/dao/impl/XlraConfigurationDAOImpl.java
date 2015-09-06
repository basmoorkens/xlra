package com.moorkensam.xlra.dao.impl;

import javax.persistence.Query;

import com.moorkensam.xlra.dao.BaseDAO;
import com.moorkensam.xlra.dao.XlraConfigurationDao;
import com.moorkensam.xlra.model.XlraConfiguration;

public class XlraConfigurationDAOImpl extends BaseDAO implements XlraConfigurationDao {

	@Override
	public void updateXlraConfiguration(XlraConfiguration config) {
		getEntityManager().merge(config);
	}

	@Override
	public XlraConfiguration getXlraConfiguration() {
		Query query = getEntityManager().createNamedQuery("XlraConfiguration.findMainConfig");
		query.setParameter("configName", XlraConfiguration.MAIN_CONFIG_NAME);
		return (XlraConfiguration) query.getSingleResult();
	}

}
