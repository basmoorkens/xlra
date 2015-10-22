package com.moorkensam.xlra.dto;

import junit.framework.Assert;

import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.inject.annotation.TestedObject;

public class PriceResultDTOTest extends UnitilsJUnit4 {

	@TestedObject
	private PriceResultDTO dto;

	@Test
	public void testGetDetailedCalculation() {
		dto = new PriceResultDTO();
		dto.setBasePrice("100");
		dto.setDieselSurcharge("50");
		dto.setChfSurcharge("25");
		dto.setExportForm("10");
		dto.setImportForm("15");
		dto.setTotalPrice("250");
		String fullDetail = dto.getDetailedCalculation();
		Assert.assertNotNull(fullDetail);
		Assert.assertEquals("10050251510250", fullDetail);
	}
}
