package com.moorkensam.xlra.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.event.RowEditEvent;

import com.moorkensam.xlra.controller.util.MessageUtil;
import com.moorkensam.xlra.model.Language;
import com.moorkensam.xlra.model.configuration.Translation;
import com.moorkensam.xlra.model.configuration.TranslationForLanguage;
import com.moorkensam.xlra.model.configuration.TranslationKey;
import com.moorkensam.xlra.model.error.XlraValidationException;
import com.moorkensam.xlra.service.TranslationService;
import com.moorkensam.xlra.service.util.TranslationUtil;

@ManagedBean
@ViewScoped
public class TranslationController {

	@Inject
	private TranslationService translationService;

	private List<Translation> translations;

	private TranslationKey selectedKey;

	private Translation selectedTranslation;

	private List<TranslationKey> availableKeys;

	private Translation newTranslation;

	@PostConstruct
	public void initPage() {
		refreshPage();
	}

	private void refreshPage() {
		refreshTranslations();
		setupAvailableTranslationKeys();
		setupNewTranslations();
	}

	public void saveNewTranslations() {
		newTranslation.setTranslationKey(selectedKey);
		try {
			translationService.createTranslation(newTranslation);
			MessageUtil.addMessage(
					"Translation created",
					"Added translations for "
							+ newTranslation.getTranslationKey());
			refreshPage();
		} catch (XlraValidationException e) {
			MessageUtil.addErrorMessage("Error creating translation",
					e.getBusinessException());
		}
	}

	private void setupNewTranslations() {
		Translation t = new Translation();
		TranslationForLanguage tlEng = new TranslationForLanguage();
		tlEng.setLanguage(Language.EN);

		TranslationForLanguage tlNl = new TranslationForLanguage();
		tlNl.setLanguage(Language.NL);

		TranslationForLanguage tlFr = new TranslationForLanguage();
		tlFr.setLanguage(Language.FR);

		TranslationForLanguage tlDe = new TranslationForLanguage();
		tlDe.setLanguage(Language.DE);

		t.setTranslations(Arrays.asList(tlEng, tlNl, tlFr, tlDe));

		newTranslation = t;
	}

	private void setupAvailableTranslationKeys() {
		availableKeys = new ArrayList<TranslationKey>();
		for (TranslationKey key : TranslationUtil.getTranslationKeyKeys()) {
			boolean found = false;
			for (Translation t : translations) {
				if (t.getTranslationKey().equals(key)) {
					found = true;
					break;
				}
			}
			if (!found) {
				availableKeys.add(key);
			}
		}
	}

	private void refreshTranslations() {
		translations = translationService.getAllNonDeletedTranslations();
	}

	private void updateTranslation(Translation translation) {
		translationService.updateTranslation(translation);
	}

	public void onTranslationRowEdit(RowEditEvent event) {
		Translation editedTranslation = (Translation) event.getObject();

		updateTranslation(editedTranslation);
		MessageUtil.addMessage(
				"Translation updated",
				"Updated translation for "
						+ editedTranslation.getTranslationKey());
		refreshTranslations();
	}

	public List<Translation> getTranslations() {
		return translations;
	}

	public void setTranslations(List<Translation> translations) {
		this.translations = translations;
	}

	public Translation getSelectedTranslation() {
		return selectedTranslation;
	}

	public void setSelectedTranslation(Translation selectedTranslation) {
		this.selectedTranslation = selectedTranslation;
	}

	public List<TranslationKey> getAvailableKeys() {
		return availableKeys;
	}

	public void setAvailableKeys(List<TranslationKey> availableKeys) {
		this.availableKeys = availableKeys;
	}

	public Translation getNewTranslation() {
		return newTranslation;
	}

	public void setNewTranslation(Translation newTranslation) {
		this.newTranslation = newTranslation;
	}

	public TranslationKey getSelectedKey() {
		return selectedKey;
	}

	public void setSelectedKey(TranslationKey selectedKey) {
		this.selectedKey = selectedKey;
	}
}
