package com.moorkensam.xlra.service.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.ejb.Stateless;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.moorkensam.xlra.service.ExcelService;

@Stateless
public class ExcelServiceImpl implements ExcelService {

	private Map<Integer, String> zoneMap = new HashMap<Integer, String>();

	private Map<Integer, String> zoneValuesMap = new HashMap<Integer, String>();

	@Override
	public void uploadRateFileExcel(HSSFWorkbook workBook) {
		Iterator<Row> rowIterator = workBook.getSheetAt(0).iterator();
		boolean startReadingLines = false;
		boolean zoneLine = false;
		boolean zoneValuesLine = false;
		boolean readRateLines = false;
		while (rowIterator.hasNext()) {
			Iterator<Cell> cellIterator = rowIterator.next().cellIterator();
			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				if (!startReadingLines) {
					startReadingLines = checkStartReadingFile(cell);
					if (startReadingLines) {
						zoneLine = true;
					}
				} else {
					if (zoneLine) {
						readZone(cell);
					}
					if (zoneValuesLine) {
						readZoneValues(cell);
					}

					if (cell.getColumnIndex() == 0
							&& (cell.getCellType() == Cell.CELL_TYPE_NUMERIC)) {
						readRateLines = true;
					}
					
					if(readRateLines) {
						
					}
				}
			}
			if (zoneLine) {
				zoneLine = false;
				zoneValuesLine = true;
			}
			if (zoneValuesLine) {
				zoneValuesLine = false;
			}
		}

	}

	private void readZoneValues(Cell cell) {
		if (cell.getColumnIndex() == 0) {
			return;
		} else {
			zoneValuesMap.put(cell.getColumnIndex(), cell.getStringCellValue());
		}
	}

	private void readZone(Cell cell) {
		if (cell.getColumnIndex() == 0) {// dont process this one
			return;
		} else {
			zoneMap.put(cell.getColumnIndex(), cell.getStringCellValue());
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
