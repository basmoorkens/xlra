package com.moorkensam.xlra.service.util;

import org.apache.poi.ss.usermodel.Cell;

public class Poiutil {

	/**
	 * Utility method to get the contents of a cell as a double regarding of the cell type.
	 * If the value can not be parsed to a valid double this will crash.
	 * @param cell
	 * @return
	 */
	public static Double getSafeCellDouble(Cell cell) {
		if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			return cell.getNumericCellValue();
		}
		if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
			return Double.parseDouble(cell.getStringCellValue());
		}
		return 0d;
	}

	/**
	 * Utility method to get the contents of a cell as a string regarding of the cell type.
	 * @param cell
	 * @param parseAsInt
	 * @return
	 */
	public static String getSafeCellString(Cell cell, boolean parseAsInt) {
		if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			if (parseAsInt) {
				return ((int) cell.getNumericCellValue()) + "";
			} else {
				return cell.getNumericCellValue() + "";
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
