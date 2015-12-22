package com.moorkensam.xlra.service.util.excel;

/**
 * Enum to keep track of the state of the excel parser.
 * 
 * @author bas
 *
 */
public enum ExcelReaderState {

  NOT_READING, READ_ZONES, READ_ZONE_VALUES, READ_ZONE_EXTRA_INFO, WAIT_FOR_RATES, READ_RATES, READ_TERMS, DONE;

}
