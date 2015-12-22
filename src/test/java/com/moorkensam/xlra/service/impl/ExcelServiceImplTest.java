package com.moorkensam.xlra.service.impl;

import com.moorkensam.xlra.model.ExcelUploadUtilData;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.service.RateFileService;
import com.moorkensam.xlra.service.util.excel.ExcelToModelMapper;
import com.moorkensam.xlra.service.util.excel.ExcelUploadParser;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;

public class ExcelServiceImplTest extends UnitilsJUnit4 {

  private ExcelServiceImpl excelServiceImpl;

  private RateFile rf;

  private XSSFWorkbook workBook;

  @Mock
  private ExcelToModelMapper mapper;

  @Mock
  private RateFileService rateFileService;

  @Mock
  private ExcelUploadParser parser;

  private ExcelUploadUtilData excelUploadUtilData;

  /**
   * exec befor test.
   */
  @Before
  public void init() {
    rf = new RateFile();
    workBook = new XSSFWorkbook();
    excelServiceImpl = new ExcelServiceImpl();
    excelServiceImpl.setMapper(mapper);
    excelServiceImpl.setParser(parser);
    excelServiceImpl.setRateFileService(rateFileService);
  }

  @Test
  public void testuploadRateFileExcel() {
    excelUploadUtilData = new ExcelUploadUtilData();
    EasyMock.expect(parser.parseRateFileExcel(workBook)).andReturn(excelUploadUtilData);
    mapper.mapExcelToModel(rf, excelUploadUtilData);
    EasyMock.expectLastCall();
    rateFileService.createRateFile(rf);
    EasyMock.expectLastCall();

    EasyMockUnitils.replay();

    excelServiceImpl.uploadRateFileExcel(rf, workBook);
  }
}
