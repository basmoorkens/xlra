package com.moorkensam.xlra.service.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

import java.text.SimpleDateFormat;

public class Poiutil {

  private static SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

  /**
   * Utility method to get the contents of a cell as a double regarding of the cell type. If the
   * value can not be parsed to a valid double this will crash.
   * 
   * @param cell The cell to parse.
   * @return The parsed double value.
   */
  public static Double getSafeCellDouble(Cell cell, FormulaEvaluator evaluator) {
    if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
      return cell.getNumericCellValue();
    }
    if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
      return Double.parseDouble(cell.getStringCellValue());
    }
    if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
      evaluator.evaluateInCell(cell);
      return cell.getNumericCellValue();
    }
    return 0d;
  }

  /**
   * Utility method to get the contents of a cell as a string regarding of the cell type.
   * 
   * @param cell The cell to parse.
   * @param parseAsInt Parse the result as an int or not.
   * @return The string value of the cell.
   */
  public static String getSafeCellString(Cell cell, boolean parseAsInt) {
    if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
      if (DateUtil.isCellDateFormatted(cell)) {
        return format.format(cell.getDateCellValue());
      } else {
        if (parseAsInt) {
          return ((int) cell.getNumericCellValue()) + "";
        } else {
          return cell.getNumericCellValue() + "";
        }
      }
    }

    if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
      return cell.getStringCellValue();
    }
    if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
      return "";
    }
    return "";
  }

}
