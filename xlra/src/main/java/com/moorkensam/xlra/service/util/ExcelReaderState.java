package com.moorkensam.xlra.service.util;

/**
 * Enum to keep track of the state of the excel parser.
 * 
 * @author bas
 *
 */
public enum ExcelReaderState {

	NOT_READING, READ_ZONES, READ_ZONE_VALUES, READ_RATES, READ_TERMS, DONE;

}
