package com.moorkensam.xlra.service.util;

import com.moorkensam.xlra.model.ExcelUploadUtilData;
import com.moorkensam.xlra.model.RateLineExcelImportDto;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This clas does the magic of converting the excel to a relational model usable by the application.
 * 
 * @author bas
 *
 */
public class ExcelUploadParser {

  private static final String EXTRA_INFO = "Extra info";

  private ExcelUploadUtilData data;

  private FormulaEvaluator formulaEvaluator;

  /**
   * PArse the excel file into a data holding structure.
   * 
   * @param workBook the workbook to parse.
   * @return the result with the parsed data in it.
   */
  public ExcelUploadUtilData parseRateFileExcel(XSSFWorkbook workBook) {
    data = new ExcelUploadUtilData();

    formulaEvaluator = workBook.getCreationHelper().createFormulaEvaluator();

    ExcelReaderState readerState = ExcelReaderState.NOT_READING;

    Iterator<Row> rowIterator = workBook.getSheetAt(0).iterator();
    while (rowIterator.hasNext()) {
      Iterator<Cell> cellIterator = rowIterator.next().cellIterator();
      while (cellIterator.hasNext()) {
        readerState = processOneCell(readerState, cellIterator);
      }
      readerState = applyAfterProcessLogic(readerState);
    }
    return data;
  }

  private ExcelReaderState applyAfterProcessLogic(ExcelReaderState readerState) {
    if (readerState == ExcelReaderState.READ_ZONES) {
      readerState = ExcelReaderState.READ_ZONE_VALUES;
    }
    if (readerState == ExcelReaderState.READ_ZONE_EXTRA_INFO) {
      readerState = ExcelReaderState.WAIT_FOR_RATES;
    }
    return readerState;
  }

  private ExcelReaderState processOneCell(ExcelReaderState readerState, Iterator<Cell> cellIterator) {
    Cell cell = cellIterator.next();
    switch (readerState) {
      case NOT_READING:
        readerState = tryBeginReading(readerState, cell);
        break;
      case READ_ZONES:
        readZone(cell);
        break;
      case READ_ZONE_VALUES:
        readerState = readZoneValues(readerState, cell);
        break;
      case WAIT_FOR_RATES:
        readerState = readWaitForRates(readerState, cell);
        break;
      case READ_ZONE_EXTRA_INFO:
        readExtraInfo(readerState, cell);
        break;
      case READ_RATES:
        if (cell.getColumnIndex() == 0 && cell.getCellType() == Cell.CELL_TYPE_STRING) {
          readerState = ExcelReaderState.READ_TERMS;
          break;// dont need to read rest of row
        } else {
          readRateCells(cell);
        }
        break;
      case READ_TERMS:
        readerState = readTerms(readerState, cell);
        break;
      default:
        break;
    }
    return readerState;
  }

  private ExcelReaderState readWaitForRates(ExcelReaderState readerState, Cell cell) {
    if (cell.getColumnIndex() == 0 && cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
      data.setSelectedMeasurement(Poiutil.getSafeCellDouble(cell, formulaEvaluator));
      readerState = ExcelReaderState.READ_RATES;
    }
    return readerState;
  }

  private void readExtraInfo(ExcelReaderState readerState, Cell cell) {
    readExtraInfoCell(cell);
  }

  private ExcelReaderState readTerms(ExcelReaderState readerState, Cell cell) {
    if (cell.getColumnIndex() == 0 && cell.getCellType() == Cell.CELL_TYPE_BLANK) {
      readerState = ExcelReaderState.DONE;
    } else {
      readTermsCell(cell);
    }
    return readerState;
  }

  private ExcelReaderState readZoneValues(ExcelReaderState readerState, Cell cell) {
    if (cell.getCellType() != Cell.CELL_TYPE_BLANK) {
      readZoneValues(cell);
    }

    if (cell.getColumnIndex() == 0) {
      if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
        String cellValue = Poiutil.getSafeCellString(cell, false);
        if (cellValue.toLowerCase().equals(EXTRA_INFO.toLowerCase())) {
          readerState = ExcelReaderState.READ_ZONE_EXTRA_INFO;
        }
      }

      if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
        data.setSelectedMeasurement(Poiutil.getSafeCellDouble(cell, formulaEvaluator));
        readerState = ExcelReaderState.READ_RATES;
      }
    }
    return readerState;
  }

  private void readZoneValues(Cell cell) {
    if (cell.getColumnIndex() == 0) {
      if (Poiutil.getSafeCellString(cell, false) != "") {
        // empty cell do nothing
      }
    } else {
      readZonesValueDataCell(cell);
    }
  }

  private ExcelReaderState tryBeginReading(ExcelReaderState readerState, Cell cell) {
    boolean startReadingLines;
    startReadingLines = checkStartReadingFile(cell);
    if (startReadingLines) {
      readerState = ExcelReaderState.READ_ZONES;
    }
    return readerState;
  }

  protected void readTermsCell(Cell cell) {
    if (cell.getColumnIndex() == 0) {
      data.setSelectedTerm(Poiutil.getSafeCellString(cell, false));
    } else {
      readTermsDataCell(cell);
    }
  }

  private void readTermsDataCell(Cell cell) {
    if (data.getTermsMap().containsKey(data.getSelectedTerm())) {
      List<String> list = data.getTermsMap().get(data.getSelectedTerm());
      list.add(Poiutil.getSafeCellString(cell, false));
    } else {
      if (data.getSelectedTerm() != null) {
        List<String> list = new ArrayList<String>();
        list.add(Poiutil.getSafeCellString(cell, false));
        data.getTermsMap().put(data.getSelectedTerm(), list);
      }
    }
  }

  private void readExtraInfoCell(Cell cell) {
    if (cell.getColumnIndex() == 0) {
      return;
    } else {
      data.getZoneExtraInfoMap().put(cell.getColumnIndex(), Poiutil.getSafeCellString(cell, false));
    }
  }

  private void readRateCells(Cell cell) {
    if (cell.getColumnIndex() == 0) {
      data.setSelectedMeasurement(Poiutil.getSafeCellDouble(cell, formulaEvaluator));
    } else {
      readRateCellData(cell);
    }
  }

  private void readRateCellData(Cell cell) {
    if (cell.getCellType() != Cell.CELL_TYPE_BLANK) {
      if (data.getRatesMap().containsKey(data.getSelectedMeasurement())) {
        List<RateLineExcelImportDto> list = data.getRatesMap().get(data.getSelectedMeasurement());
        RateLineExcelImportDto dto = addRateLine(cell);
        list.add(dto);
      } else {
        List<RateLineExcelImportDto> list = new ArrayList<RateLineExcelImportDto>();
        RateLineExcelImportDto dto = addRateLine(cell);
        list.add(dto);
        data.getRatesMap().put(data.getSelectedMeasurement(), list);
      }
    } else {
      // not filled in column, just skip
    }

  }

  private RateLineExcelImportDto addRateLine(Cell cell) {
    RateLineExcelImportDto dto = new RateLineExcelImportDto();
    dto.setValue(Poiutil.getSafeCellDouble(cell, formulaEvaluator));
    dto.setZone(data.getZoneMap().get(cell.getColumnIndex()));
    return dto;
  }

  private void readZonesValueDataCell(Cell cell) {
    if (data.getZoneValuesMap().containsKey(cell.getColumnIndex())) {
      List<String> list = data.getZoneValuesMap().get(cell.getColumnIndex());
      list.add(Poiutil.getSafeCellString(cell, false));
    } else {
      List<String> list = new ArrayList<String>();
      list.add(Poiutil.getSafeCellString(cell, false));
      data.getZoneValuesMap().put(cell.getColumnIndex(), list);
    }
  }

  private void readZone(Cell cell) {
    if (cell.getColumnIndex() == 0) {
      return;
    } else {
      data.getZoneMap().put(cell.getColumnIndex(), Poiutil.getSafeCellString(cell, true));
    }

  }

  private boolean checkStartReadingFile(Cell cell) {
    if (cell.getColumnIndex() == 0) {
      if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
        if ("zone".equals(cell.getStringCellValue().toLowerCase())) {
          return true;
        }
      }
    }
    return false;
  }

}
