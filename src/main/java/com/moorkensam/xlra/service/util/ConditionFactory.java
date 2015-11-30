package com.moorkensam.xlra.service.util;

import com.moorkensam.xlra.model.rate.Condition;
import com.moorkensam.xlra.model.rate.ConditionType;
import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.translation.TranslationKey;

public class ConditionFactory {

	private QuotationUtil quotationUtil;

	public ConditionFactory() {
		setQuotationUtil(QuotationUtil.getInstance());
	}

	public Condition createCondition(TranslationKey key, String value) {
		Condition condition = new Condition();
		condition.setConditionKey(key);
		condition.setValue(value);
		condition.setConditionType(getConditionTypeForTranslationKey(key));
		return condition;
	}

	protected ConditionType getConditionTypeForTranslationKey(TranslationKey key) {
		if (quotationUtil.isCalculationKey(key)) {
			return ConditionType.CALCULATION;
		}
		return ConditionType.TRANSLATION;
	}

	public QuotationUtil getQuotationUtil() {
		return quotationUtil;
	}

	public void setQuotationUtil(QuotationUtil quotationUtil) {
		this.quotationUtil = quotationUtil;
	}
}
