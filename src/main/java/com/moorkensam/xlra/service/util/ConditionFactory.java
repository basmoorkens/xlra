package com.moorkensam.xlra.service.util;

import com.moorkensam.xlra.model.configuration.Language;
import com.moorkensam.xlra.model.rate.Condition;
import com.moorkensam.xlra.model.rate.ConditionType;
import com.moorkensam.xlra.model.translation.TranslationKey;

public class ConditionFactory {

	private QuotationUtil quotationUtil;

	private TranslationKeyToi8nMapper mapper;

	private TranslationUtil translationUtil;

	public ConditionFactory() {
		setQuotationUtil(QuotationUtil.getInstance());
		setMapper(new TranslationKeyToi8nMapper());
		setTranslationUtil(new TranslationUtil());
	}

	public Condition createCondition(TranslationKey key, String value) {
		Condition condition = new Condition();
		condition.setConditionKey(key);
		condition.setConditionType(getConditionTypeForTranslationKey(key));
		condition.setI8nKey(getMapper().map(key));
		if (condition.getConditionType() == ConditionType.CALCULATION) {
			condition.setValue(value);
		} else {
			initializeTranslations(value, condition);
		}
		getTranslationUtil().fillInTranslation(condition);
		return condition;
	}

	private void initializeTranslations(String value, Condition condition) {
		condition.addTranslation(Language.NL, value);
		condition.addTranslation(Language.EN, "");
		condition.addTranslation(Language.FR, "");
		condition.addTranslation(Language.DE, "");
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

	public TranslationKeyToi8nMapper getMapper() {
		return mapper;
	}

	public void setMapper(TranslationKeyToi8nMapper mapper) {
		this.mapper = mapper;
	}

	public TranslationUtil getTranslationUtil() {
		return translationUtil;
	}

	public void setTranslationUtil(TranslationUtil translationUtil) {
		this.translationUtil = translationUtil;
	}
}
