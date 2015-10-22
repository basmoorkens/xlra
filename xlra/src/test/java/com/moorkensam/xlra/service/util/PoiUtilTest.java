package com.moorkensam.xlra.service.util;

import junit.framework.Assert;

import org.apache.poi.ss.format.CellTextFormatter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;

public class PoiUtilTest extends UnitilsJUnit4 {

	private Poiutil poiUtil;

	private XSSFRow row;

	@Before
	public void init() {
		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet("mySheet");
		row = sheet.createRow(0);
	}

	@Test
	public void testGetSafeCellDouble() {
		Cell cell = row.createCell(0);
		cell.setCellValue("10.05d");
		cell.setCellType(Cell.CELL_TYPE_STRING);
		Double result = Poiutil.getSafeCellDouble(cell, null);
		Assert.assertNotNull(result);
		Assert.assertEquals(10.05d, result);
	}

	@Test
	public void testGetSafeCellDoubleNumber() {
		Cell cell = row.createCell(0);
		cell.setCellValue("11.55d");
		cell.setCellType(Cell.CELL_TYPE_STRING);
		Double result = Poiutil.getSafeCellDouble(cell, null);
		Assert.assertNotNull(result);
		Assert.assertEquals(11.55d, result);
	}

	@Test
	public void testgetSafeCellString() {
		Cell cell = row.createCell(0);
		cell.setCellValue("test");
		cell.setCellType(Cell.CELL_TYPE_STRING);
		String result = Poiutil.getSafeCellString(cell, false);
		Assert.assertNotNull(result);
		Assert.assertEquals("test", result);
	}

	@Test
	public void testgetSafeCellStringNumber() {
		Cell cell = row.createCell(0);
		cell.setCellValue(11.55d);
		cell.setCellType(Cell.CELL_TYPE_NUMERIC);
		String result = Poiutil.getSafeCellString(cell, false);
		Assert.assertNotNull(result);
		Assert.assertEquals("11.55", result);
	}
}
