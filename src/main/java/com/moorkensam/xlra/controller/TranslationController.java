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
import com.moorkensam.xlra.model.configuration.Language;
import com.moorkensam.xlra.model.configuration.TranslationForLanguage;
import com.moorkensam.xlra.model.error.XlraValidationException;
import com.moorkensam.xlra.model.translation.Translation;
import com.moorkensam.xlra.model.translation.TranslationKey;
import com.moorkensam.xlra.service.TranslationService;
import com.moorkensam.xlra.service.util.TranslationUtil;

@ManagedBean
@ViewScoped
public class TranslationController {

	@Inject
	private TranslationService translationService;

	private List<Translation> translations;

	private TranslationKey selectedKey;

	private List<TranslationKey> availableKeys;

	private Translation activeTranslation;

	private boolean editMode;

	private boolean updateMode;

	@PostConstruct
	public void initPage() {
		activeTranslation = new Translation();
		refreshPage();
	}

	private void refreshPage() {
		refreshTranslations();
		setupAvailableTranslationKeys();
	}

	public void saveActiveTranslations() {
		if (activeTranslation.getId() <= 0) {
			getActiveTranslation().setTranslationKey(selectedKey);
			doCreateTranslation();
		}
		updateTranslation();
	}

	private void doCreateTranslation() {
		try {
			translationService.createTranslation(getActiveTranslation());
			MessageUtil.addMessage("Translation created",
					"Added translations for "
							+ getActiveTranslation().getTranslationKey());
			refreshPage();
		} catch (XlraValidationException e) {
			MessageUtil.addErrorMessage("Error creating translation",
					e.getBusinessException());
		}
	}

	public void cancel() {
		editMode = false;
		updateMode = false;
		activeTranslation = new Translation();
	}

	public void setupNewTranslation() {
		editMode = true;
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

		setActiveTranslation(t);
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

	private void updateTranslation() {
		translationService.updateTranslation(activeTranslation);
		MessageUtil.addMessage(
				"Translation updated",
				"Updated translation for "
						+ activeTranslation.getTranslationKey());
		refreshTranslations();
	}

	public void setupPageForEdit(Translation translation) {
		selectedKey = translation.getTranslationKey();
		activeTranslation = translation;
		editMode = true;
		updateMode = true;
	}

	public List<Translation> getTranslations() {
		return translations;
	}

	public void setTranslations(List<Translation> translations) {
		this.translations = translations;
	}

	public List<TranslationKey> getAvailableKeys() {
		return availableKeys;
	}

	public void setAvailableKeys(List<TranslationKey> availableKeys) {
		this.availableKeys = availableKeys;
	}

	public TranslationKey getSelectedKey() {
		return selectedKey;
	}

	public void setSelectedKey(TranslationKey selectedKey) {
		this.selectedKey = selectedKey;
	}

	public boolean isEditMode() {
		return editMode;
	}

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}

	public Translation getActiveTranslation() {
		return activeTranslation;
	}

	public void setActiveTranslation(Translation activeTranslation) {
		this.activeTranslation = activeTranslation;
	}

	public boolean isUpdateMode() {
		return updateMode;
	}

	public void setUpdateMode(boolean updateMode) {
		this.updateMode = updateMode;
	}
}
