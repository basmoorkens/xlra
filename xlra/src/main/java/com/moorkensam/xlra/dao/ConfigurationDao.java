package com.moorkensam.xlra.dao;

import java.util.List;

import com.moorkensam.xlra.model.configuration.Configuration;
import com.moorkensam.xlra.model.configuration.TranslationKey;

public interface ConfigurationDao {

	void updateXlraConfiguration(Configuration config); 
	
	Configuration getXlraConfiguration();
}
