package com.moorkensam.xlra.service.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.moorkensam.xlra.model.ExcelUploadUtilData;
import com.moorkensam.xlra.model.RateLineExcelImportDTO;
import com.moorkensam.xlra.model.configuration.TranslationKey;
import com.moorkensam.xlra.model.rate.Condition;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.rate.RateLine;
import com.moorkensam.xlra.model.rate.Zone;
import com.moorkensam.xlra.model.rate.ZoneType;

/**
 * This class actualy maps our parsed excel data model to the application model.
 * 
 * @author bas
 *
 */
public class ExcelToModelMapper {

	public void mapExcelToModel(RateFile rf, ExcelUploadUtilData data) {
		rf.setZones(createZones(rf, data));
		rf.setRateLines(createRateLines(data, rf));
		rf.setConditions(createConditions(data, rf));
	}

	protected List<Condition> createConditions(ExcelUploadUtilData data,
			RateFile rf) {
		List<Condition> conditions = new ArrayList<Condition>();
		for (String key : data.getTermsMap().keySet()) {
			Condition condition = new Condition();
			condition.setConditionKey(TranslationKey.valueOf(key));
			condition.setValue(concatTermValues(data.getTermsMap().get(key)));
			condition.setRateFile(rf);
			conditions.add(condition);
		}
		return conditions;
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
		List<RateLine> rateLines = new ArrayList<RateLine>();
		for (Double key : data.getRatesMap().keySet()) {
			for (RateLineExcelImportDTO dto : data.getRatesMap().get(key)) {
				RateLine rl = new RateLine();
				rl.setMeasurement(key);
				rl.setValue(new BigDecimal(dto.getValue()));
				rl.setZone(dto.getZone());
				rl.setRateFile(rf);
				rateLines.add(rl);
			}
		}
		return rateLines;
	}

	protected List<Zone> createZones(RateFile rf, ExcelUploadUtilData data) {
		List<Zone> zones = new ArrayList<Zone>();

		for (Integer key : data.getZoneMap().keySet()) {
			Zone zone = new Zone();
			zone.setName(data.getZoneMap().get(key));
			zones.add(zone);
			zone.setRateFile(rf);
			List<String> postalCodes = data.getZoneValuesMap().get(key);
			String postalCodeAsString = constructPostalCodeString(postalCodes);
			if (rf.getCountry().getZoneType() == ZoneType.ALPHANUMERIC_LIST) {
				zone.setAlphaNumericPostalCodesAsString(postalCodeAsString);
			} else {
				zone.setNumericalPostalCodesAsString(postalCodeAsString);
			}
		}

		return zones;
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

}
