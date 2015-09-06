package com.moorkensam.xlra.dao;

import com.moorkensam.xlra.model.XlraConfiguration;

public interface XlraConfigurationDao {

	void updateXlraConfiguration(XlraConfiguration config); 
	
	XlraConfiguration getXlraConfiguration();
	
}
