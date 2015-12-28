package com.moorkensam.xlra.service.util.excel;

import com.moorkensam.xlra.model.configuration.TranslationForLanguage;
import com.moorkensam.xlra.model.rate.Condition;
import com.moorkensam.xlra.model.rate.ConditionType;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.rate.RateLine;
import com.moorkensam.xlra.model.rate.Zone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;
import java.util.List;

public class RateFileToExcelMapper {

  private static final Logger logger = LogManager.getLogger();

  /**
   * Transform a ratefile back to excel.
   * 
   * @param rateFile The ratefile to transform.
   * @return The generated workbook.
   */
  public XSSFWorkbook map(RateFile rateFile) {
    List<Integer> headerRows = new ArrayList<Integer>();
    logger.info("Parsing XSSF workbook from ratefile " + rateFile.getName());
    XSSFWorkbook workBook = new XSSFWorkbook();
    generateSheetName(rateFile);
    Sheet sheet = workBook.createSheet(generateSheetName(rateFile));
    int lastUsedRow = 0;

    headerRows.add(lastUsedRow);
    lastUsedRow = createHeaders(rateFile, sheet, lastUsedRow);

    headerRows.add(lastUsedRow);
    lastUsedRow = createZoneInfo(rateFile, sheet, lastUsedRow);
    lastUsedRow++;// 1 empty row

    lastUsedRow = createRatesTitle(headerRows, sheet, lastUsedRow);
    lastUsedRow = createRateLineInfo(rateFile, sheet, lastUsedRow);
    lastUsedRow++;// 1 empty row

    headerRows.add(lastUsedRow);
    lastUsedRow = createConditionsInfo(rateFile, sheet, lastUsedRow);

    int maxColumns = rateFile.getColumns().size() + 1;
    mergeHeaderRowCells(maxColumns, headerRows, sheet);
    autoSizeColumns(sheet, maxColumns);
    return workBook;
  }

  private void autoSizeColumns(Sheet sheet, int maxColumns) {
    for (int x = 0; x < maxColumns; x++) {
      sheet.autoSizeColumn(x);
    }
  }

  private void mergeHeaderRowCells(int maxColumns, List<Integer> headerRows, Sheet sheet) {
    for (int row : headerRows) {
      sheet.addMergedRegion(new CellRangeAddress(row, row, 0, maxColumns));
    }
  }

  private int createRatesTitle(List<Integer> headerRows, Sheet sheet, int lastUsedRow) {
    headerRows.add(lastUsedRow);
    Row ratesHeader = sheet.createRow(lastUsedRow++);
    Cell ratesTitleCell = ratesHeader.createCell(0);
    ratesTitleCell.setCellValue("Rates");
    return lastUsedRow;
  }

  private int createConditionsInfo(RateFile rateFile, Sheet sheet, int lastUsedRow) {
    Row conditionHeader = sheet.createRow(lastUsedRow++);
    Cell conditionHeaderCell = conditionHeader.createCell(0);
    conditionHeaderCell.setCellValue("Conditions");
    for (Condition condition : rateFile.getConditions()) {
      Row row = sheet.createRow(lastUsedRow++);
      Cell cell = row.createCell(0);
      cell.setCellValue(condition.getConditionKey() + "");
      Cell cell2 = row.createCell(1);
      cell2.setCellValue(condition.getConditionType().toString());
      Cell cell3 = row.createCell(2);
      if (ConditionType.CALCULATION == condition.getConditionType()) {
        cell3.setCellValue(condition.getValue());
      }
      if (ConditionType.TRANSLATION == condition.getConditionType()) {
        String value = "";
        for (TranslationForLanguage tl : condition.getTranslations()) {
          value += tl.getTranslation() + " ";
        }
        cell3.setCellValue(value);
      }
    }
    return lastUsedRow;
  }

  private int createRateLineInfo(RateFile rateFile, Sheet sheet, int lastUsedRow) {
    Row ratesHeader = sheet.createRow(lastUsedRow++);
    Cell measurementTitleCell = ratesHeader.createCell(0);
    measurementTitleCell.setCellValue(rateFile.getMeasurement().getDescription());
    for (int i = 0; i < rateFile.getColumns().size(); i++) {
      Cell cell = ratesHeader.createCell(i + 1);
      cell.setCellValue(rateFile.getColumns().get(i));
    }
    for (List<RateLine> ratelineForZones : rateFile.getRelationalRateLines()) {
      Row row = sheet.createRow(lastUsedRow++);
      Cell mesCell = row.createCell(0);
      int iterator = 1;
      for (RateLine rl : ratelineForZones) {
        mesCell.setCellValue(rl.getMeasurement());
        Cell valueCell = row.createCell(iterator++);
        valueCell.setCellValue(rl.getValue() + "");
      }
    }
    return lastUsedRow;
  }

  private int createZoneInfo(RateFile rateFile, Sheet sheet, int lastUsedRow) {
    Row zoneHeader = sheet.createRow(lastUsedRow++);
    Cell firstCell = zoneHeader.createCell(0);
    firstCell.setCellValue("Zones");
    for (Zone zone : rateFile.getZones()) {
      Row zoneRow = sheet.createRow(lastUsedRow++);
      Cell nameCell = zoneRow.createCell(0);
      nameCell.setCellValue(zone.getName());
      Cell valueCell = zoneRow.createCell(1);
      if (rateFile.isAlphaNumericalZoneRateFile()) {
        valueCell.setCellValue(zone.getAlphaNumericPostalCodesAsString());
      }
      if (rateFile.isNumericalZoneRateFile()) {
        valueCell.setCellValue(zone.getNumericalPostalCodesAsString());
      }
      Cell extraInfoCell = zoneRow.createCell(2);
      extraInfoCell.setCellValue(zone.getExtraInfo());
    }
    return lastUsedRow;
  }

  private int createHeaders(RateFile rateFile, Sheet sheet, int lastUsedRow) {
    Row headerRow = sheet.createRow(lastUsedRow++);
    Cell headerCell = headerRow.createCell(0);
    headerCell.setCellValue(rateFile.getName());
    return lastUsedRow;
  }

  private String generateSheetName(final RateFile rateFile) {
    if (rateFile.getName().length() > 30) {
      return rateFile.getName().substring(0, 29);
    } else {
      return rateFile.getName();
    }
  }

}
