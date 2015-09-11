package com.moorkensam.xlra.dao;

import com.moorkensam.xlra.model.configuration.Configuration;

public interface ConfigurationDao {

	void updateXlraConfiguration(Configuration config); 
	
	Configuration getXlraConfiguration();
	
}
