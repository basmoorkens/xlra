package com.moorkensam.xlra.service.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.moorkensam.xlra.model.ExcelUploadUtilData;
import com.moorkensam.xlra.model.RateLineExcelImportDTO;

public class ExcelUploadParser {

	private ExcelUploadUtilData data;

	public ExcelUploadUtilData parseRateFileExcel(XSSFWorkbook workBook) {
		data = new ExcelUploadUtilData();

		Iterator<Row> rowIterator = workBook.getSheetAt(0).iterator();
		boolean startReadingLines = false;
		boolean zoneLine = false;
		boolean zoneValuesLine = false;
		boolean readRateLines = false;
		boolean readTerms = false;
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
						if (cell.getCellType() != Cell.CELL_TYPE_BLANK) {
							zoneValuesLine = readZoneValues(cell);
						}
					}

					if (cell.getColumnIndex() == 0
							&& (cell.getCellType() == Cell.CELL_TYPE_NUMERIC)) {
						readRateLines = true;
					}

					if (readRateLines) {
						if (cell.getColumnIndex() == 0
								&& cell.getCellType() == Cell.CELL_TYPE_STRING) {
							readRateLines = false;
							readTerms = true;
						} else {
							readRateCells(cell);
						}
					}

					if (readTerms) {
						if (cell.getColumnIndex() == 0
								&& cell.getCellType() == Cell.CELL_TYPE_BLANK) {
							readTerms = false;
						} else {
							readTermsCell(cell);
						}
					}
				}
			}
			if (zoneLine) {
				zoneLine = false;
				zoneValuesLine = true;
			}
			if (zoneValuesLine && readRateLines) {
				zoneValuesLine = false;
			}
		}
		return data;
	}

	private void readTermsCell(Cell cell) {
		if (cell.getColumnIndex() == 0) {
			data.setSelectedTerm(getSafeCellString(cell, false));
		} else {
			readTermsDataCell(cell);
		}
	}

	private void readTermsDataCell(Cell cell) {
		if (data.getTermsMap().containsKey(data.getSelectedTerm())) {
			List<String> list = data.getTermsMap().get(data.getSelectedTerm());
			list.add(getSafeCellString(cell, false));
		} else {
			List<String> list = new ArrayList<String>();
			list.add(getSafeCellString(cell, false));
			data.getTermsMap().put(data.getSelectedTerm(), list);
		}
	}

	private void readRateCells(Cell cell) {
		if (cell.getColumnIndex() == 0) {
			data.setSelectedMeasurement(getSafeCellDouble(cell));
		} else {
			readRateCellData(cell);
		}
	}

	private void readRateCellData(Cell cell) {
		if (data.getRatesMap().containsKey(data.getSelectedMeasurement())) {
			List<RateLineExcelImportDTO> list = data.getRatesMap().get(
					data.getSelectedMeasurement());
			RateLineExcelImportDTO dto = AddRateLine(cell);
			list.add(dto);
		} else {
			List<RateLineExcelImportDTO> list = new ArrayList<RateLineExcelImportDTO>();
			RateLineExcelImportDTO dto = AddRateLine(cell);
			list.add(dto);
			data.getRatesMap().put(cell.getNumericCellValue(), list);
		}
	}

	private RateLineExcelImportDTO AddRateLine(Cell cell) {
		RateLineExcelImportDTO dto = new RateLineExcelImportDTO();
		dto.setValue(getSafeCellDouble(cell));
		dto.setZone(data.getZoneMap().get(cell.getColumnIndex()));
		return dto;
	}

	private boolean readZoneValues(Cell cell) {
		if (cell.getColumnIndex() == 0) {
			if (getSafeCellString(cell, false) != "") {
				return false;
			}
		} else {
			readZonesValueDataCell(cell);
		}
		return true;
	}

	private void readZonesValueDataCell(Cell cell) {
		if (data.getZoneValuesMap().containsKey(cell.getColumnIndex())) {
			List<String> list = data.getZoneValuesMap().get(
					cell.getColumnIndex());
			list.add(getSafeCellString(cell, false));
		} else {
			List<String> list = new ArrayList<String>();
			list.add(getSafeCellString(cell, false));
			data.getZoneValuesMap().put(cell.getColumnIndex(), list);
		}
	}

	private void readZone(Cell cell) {
		if (cell.getColumnIndex() == 0) {// dont process this one
			return;
		} else {
			data.getZoneMap().put(cell.getColumnIndex(),
					getSafeCellString(cell, true));
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

	public Double getSafeCellDouble(Cell cell) {
		if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			return cell.getNumericCellValue();
		}
		if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
			return Double.parseDouble(cell.getStringCellValue());
		}
		return 0d;
	}

	public String getSafeCellString(Cell cell, boolean parseAsInt) {
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
