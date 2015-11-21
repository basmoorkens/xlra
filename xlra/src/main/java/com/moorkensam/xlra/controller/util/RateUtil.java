package com.moorkensam.xlra.controller.util;

import java.util.Arrays;
import java.util.List;

import org.primefaces.event.CellEditEvent;

import com.moorkensam.xlra.model.configuration.Language;
import com.moorkensam.xlra.model.rate.IncoTermType;

public class RateUtil {

	public static void onRateLineCellEdit(CellEditEvent event) {
		Object newValue = event.getNewValue();
		Object oldValue = event.getOldValue();

		if (newValue != null && !newValue.equals(oldValue)) {
			MessageUtil.addMessage("Rate updated", "Rate value updated to "
					+ newValue);
		}
	}

	public static void onConditionCellEdit(CellEditEvent event) {
		Object newValue = event.getNewValue();
		Object oldValue = event.getOldValue();

		if (newValue != null && !newValue.equals(oldValue)) {
			MessageUtil.addMessage("Condition updated", "Condition updated to"
					+ newValue);
		}
	}

	public static List<IncoTermType> getIncoTermTypes() {
		return Arrays.asList(IncoTermType.values());
	}

	public static List<Language> getLanguages() {
		return Arrays.asList(Language.values());
	}
}
