package com.moorkensam.xlra.service.util;

import junit.framework.Assert;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;

import java.io.IOException;

public class PoiUtilTest extends UnitilsJUnit4 {

  private Poiutil poiUtil;

  private XSSFWorkbook wb;

  private XSSFRow row;

  /**
   * Exec before a method.
   */
  @Before
  public void init() {
    wb = new XSSFWorkbook();
    XSSFSheet sheet = wb.createSheet("mySheet");
    row = sheet.createRow(0);
  }

  /**
   * Exec after.
   * 
   * @throws IOException htrown when in error closing the doc.
   */
  @After
  public void destroy() throws IOException {
    if (wb != null) {
      wb.close();
      wb = null;
    }
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
    cell.setCellValue(11.55d);
    cell.setCellType(Cell.CELL_TYPE_NUMERIC);
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
  public void testgetSafeCellEmpty() {
    Cell cell = row.createCell(0);
    cell.setCellType(Cell.CELL_TYPE_BLANK);
    String result = Poiutil.getSafeCellString(cell, false);
    Assert.assertNotNull(result);
    Assert.assertEquals("", result);
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

  @Test
  public void testgetSafeCellStringNumberAsInt() {
    Cell cell = row.createCell(0);
    cell.setCellValue(11.55d);
    cell.setCellType(Cell.CELL_TYPE_NUMERIC);
    String result = Poiutil.getSafeCellString(cell, true);
    Assert.assertNotNull(result);
    Assert.assertEquals("11", result);
  }
}
