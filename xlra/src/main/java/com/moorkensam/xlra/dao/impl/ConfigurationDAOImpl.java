package com.moorkensam.xlra.dao.impl;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.moorkensam.xlra.dao.BaseDAO;
import com.moorkensam.xlra.dao.ConfigurationDao;
import com.moorkensam.xlra.model.configuration.Configuration;

public class ConfigurationDAOImpl extends BaseDAO implements ConfigurationDao {

	@Override
	public void updateXlraConfiguration(Configuration config) {
		getEntityManager().merge(config);
	}

	@Override
	public Configuration getXlraConfiguration() {
		Query query = getEntityManager().createNamedQuery("Configuration.findMainConfig");
		query.setParameter("configName", Configuration.MAIN_CONFIG_NAME);
		return (Configuration) query.getSingleResult();
	}

}
