package com.moorkensam.xlra.model.offerte;

import java.io.Serializable;

import com.moorkensam.xlra.model.translation.TranslationKey;

public class OfferteOptionDTO implements Serializable {

	private static final long serialVersionUID = -4073642999193664486L;

	private TranslationKey key;

	private boolean selected;

	private String value;

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public TranslationKey getKey() {
		return key;
	}

	public void setKey(TranslationKey key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
