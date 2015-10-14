package com.moorkensam.xlra.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.event.RowEditEvent;

import com.moorkensam.xlra.controller.util.MessageUtil;
import com.moorkensam.xlra.model.Language;
import com.moorkensam.xlra.model.configuration.Translation;
import com.moorkensam.xlra.model.configuration.TranslationKey;
import com.moorkensam.xlra.service.TranslationService;
import com.moorkensam.xlra.service.util.TranslationUtil;

@ManagedBean
@ViewScoped
public class TranslationController {

	@Inject
	private TranslationService translationService;

	private List<Translation> translations;

	private Translation selectedTranslation;

	private TranslationKey selectedKey;

	private List<TranslationKey> availableKeys;

	private Language langEng, langNl, langFr, langDe;

	private Translation[] newTranslations;

	@PostConstruct
	public void initPage() {
		setupLanguages();
		refreshPage();
	}

	private void refreshPage() {
		refreshTranslations();
		setupAvailableTranslationKeys();
		setupNewTranslations();
	}

	public void saveNewTranslations() {
		fillTranslationKeyForNewTranslations();
		translationService.createTranslations(newTranslations);
		MessageUtil.addMessage("Translation created", "Added translations for "
				+ newTranslations[0].getTranslationKey());
		refreshPage();
	}

	private void fillTranslationKeyForNewTranslations() {
		for (Translation t : newTranslations) {
			t.setTranslationKey(selectedKey);
		}
	}

	private void setupLanguages() {
		setLangEng(Language.EN);
		setLangNl(Language.NL);
		setLangDe(Language.DE);
		setLangFr(Language.FR);
	}

	private void setupNewTranslations() {
		Translation t1 = new Translation();
		t1.setTranslationKey(selectedKey);
		t1.setLanguage(langEng);

		Translation t2 = new Translation();
		t2.setTranslationKey(selectedKey);
		t2.setLanguage(langNl);

		Translation t3 = new Translation();
		t3.setTranslationKey(selectedKey);
		t3.setLanguage(langFr);

		Translation t4 = new Translation();
		t4.setTranslationKey(selectedKey);
		t4.setLanguage(langDe);

		newTranslations = new Translation[4];
		newTranslations[0] = t1;
		newTranslations[1] = t2;
		newTranslations[2] = t3;
		newTranslations[3] = t4;
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

	public void createNewTranslations() {
		translationService.createTranslations(newTranslations);
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
						+ editedTranslation.getTranslationKey()
						+ " - language "
						+ editedTranslation.getLanguage().getDescription());
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

	public TranslationKey getSelectedKey() {
		return selectedKey;
	}

	public void setSelectedKey(TranslationKey selectedKey) {
		this.selectedKey = selectedKey;
	}

	public List<TranslationKey> getAvailableKeys() {
		return availableKeys;
	}

	public void setAvailableKeys(List<TranslationKey> availableKeys) {
		this.availableKeys = availableKeys;
	}

	public Language getLangEng() {
		return langEng;
	}

	public void setLangEng(Language langEng) {
		this.langEng = langEng;
	}

	public Language getLangNl() {
		return langNl;
	}

	public void setLangNl(Language langNl) {
		this.langNl = langNl;
	}

	public Language getLangFr() {
		return langFr;
	}

	public void setLangFr(Language langFr) {
		this.langFr = langFr;
	}

	public Language getLangDe() {
		return langDe;
	}

	public void setLangDe(Language langDe) {
		this.langDe = langDe;
	}

	public Translation[] getNewTranslations() {
		return newTranslations;
	}

	public void setNewTranslations(Translation[] newTranslations) {
		this.newTranslations = newTranslations;
	}
}
