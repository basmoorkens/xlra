package com.moorkensam.xlra.service;

import com.moorkensam.xlra.model.configuration.Language;

import java.util.List;

public interface LocaleService {

  List<Language> getSupportedLanguages();

  Language getDefaultUserLanguage();
}
