package com.moorkensam.xlra.dao;

import java.util.List;

import com.moorkensam.xlra.model.rate.RateFile;

public interface RateFileDAO {

	List<RateFile> getAllRateFiles(); 
	
	void createRateFile(RateFile rateFile); 
	
	void updateRateFile(RateFile rateFile); 
	
	
	
}
