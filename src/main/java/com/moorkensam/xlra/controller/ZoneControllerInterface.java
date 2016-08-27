package com.moorkensam.xlra.controller;

import com.moorkensam.xlra.model.rate.Zone;

public interface ZoneControllerInterface {

  boolean isCollapseZonesDetailGrid();

  boolean isNumericRateFileZone();

  boolean isAlphaNumericRateFileZone();

  void setupEditZone(Zone zone);

  void deleteZone(Zone zone);

  void setupAddZone();

  String getZoneDialogTitle();

  Zone getSelectedZone();

  void cancelEditZone();
  
  void saveZone();
}
