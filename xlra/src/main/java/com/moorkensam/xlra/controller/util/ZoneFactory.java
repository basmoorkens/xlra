package com.moorkensam.xlra.controller.util;

import com.moorkensam.xlra.model.rate.Country;
import com.moorkensam.xlra.model.rate.Zone;

public class ZoneFactory {

	public static Zone createZone(Country country) {
		Zone zone = new Zone();
		zone.setCountry(country);
		zone.setZoneType(country.getZoneType());
		zone.setAlphaNumericPostalCodesAsString("");
		return zone;
	}

}
