package com.moorkensam.xlra.service.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.moorkensam.xlra.model.ExcelUploadUtilData;
import com.moorkensam.xlra.model.RateLineExcelImportDTO;
import com.moorkensam.xlra.model.rate.Condition;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.rate.RateLine;
import com.moorkensam.xlra.model.rate.Zone;
import com.moorkensam.xlra.model.rate.ZoneType;
import com.moorkensam.xlra.model.translation.TranslationKey;

/**
 * This class actualy maps our parsed excel data model to the application model.
 * 
 * @author bas
 *
 */
public class ExcelToModelMapper {

	private ConditionFactory conditionFactory;

	public ExcelToModelMapper() {
		conditionFactory = new ConditionFactory();
	}

	private final static Logger logger = LogManager.getLogger();

	public void mapExcelToModel(RateFile rf, ExcelUploadUtilData data) {
		rf.setZones(createZones(rf, data));
		rf.setRateLines(createRateLines(data, rf));
		rf.setConditions(createConditions(data, rf));
	}

	protected List<Condition> createConditions(ExcelUploadUtilData data,
			RateFile rf) {
		logger.debug("Creating conditions for excel data");
		for (String keyAsString : data.getTermsMap().keySet()) {
			TranslationKey key = TranslationKey.valueOf(keyAsString);
			String value = concatTermValues(data.getTermsMap().get(keyAsString));
			rf.addCondition(conditionFactory.createCondition(key, value));
		}
		return rf.getConditions();
	}

	protected String concatTermValues(List<String> terms) {
		String result = "";
		for (String s : terms) {
			result += s;
		}
		return result;
	}

	protected List<RateLine> createRateLines(ExcelUploadUtilData data,
			RateFile rf) {
		logger.debug("Creating ratelines for excel");
		List<RateLine> rateLines = new ArrayList<RateLine>();
		for (Double key : data.getRatesMap().keySet()) {
			for (RateLineExcelImportDTO dto : data.getRatesMap().get(key)) {
				RateLine rl = new RateLine();
				rl.setMeasurement(key);
				rl.setValue(new BigDecimal(dto.getValue()));
				rl.setZone(rf.getZoneForZoneName(dto.getZone()));
				rl.setRateFile(rf);
				rateLines.add(rl);
			}
		}
		return rateLines;
	}

	protected List<Zone> createZones(RateFile rf, ExcelUploadUtilData data) {
		logger.debug("Creating zones for ratefile");
		List<Zone> zones = new ArrayList<Zone>();

		for (Integer key : data.getZoneMap().keySet()) {
			Zone zone = new Zone();
			zone.setName(data.getZoneMap().get(key));
			zones.add(zone);
			zone.setRateFile(rf);
			zone.setZoneType(rf.getCountry().getZoneType());
			fillInPostalCodes(rf, data, key, zone);
			if (data.getZoneExtraInfoMap() != null) {
				if (!data.getZoneExtraInfoMap().isEmpty()) {
					zone.setExtraInfo(data.getZoneExtraInfoMap().get(key));
				}
			}
		}

		return zones;
	}

	private void fillInPostalCodes(RateFile rf, ExcelUploadUtilData data,
			Integer key, Zone zone) {
		List<String> postalCodes = data.getZoneValuesMap().get(key);
		String postalCodeAsString = constructPostalCodeString(postalCodes);
		if (rf.getCountry().getZoneType() == ZoneType.ALPHANUMERIC_LIST) {
			zone.setAlphaNumericPostalCodesAsString(postalCodeAsString);
		} else {
			zone.setNumericalPostalCodesAsString(postalCodeAsString);
		}
	}

	private String constructPostalCodeString(List<String> postalCodes) {
		if (postalCodes != null) {
			String postalCodeAsString = "";
			for (String s : postalCodes) {
				postalCodeAsString += s + ",";
			}
			postalCodeAsString = postalCodeAsString.substring(0,
					postalCodeAsString.length() - 1);
			return postalCodeAsString;
		}
		return "";
	}

	public ConditionFactory getConditionFactory() {
		return conditionFactory;
	}

	public void setConditionFactory(ConditionFactory conditionFactory) {
		this.conditionFactory = conditionFactory;
	}

}
