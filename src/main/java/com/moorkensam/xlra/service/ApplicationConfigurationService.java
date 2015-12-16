package com.moorkensam.xlra.service;

import com.moorkensam.xlra.model.configuration.Configuration;
import com.moorkensam.xlra.model.mail.MailTemplate;

import java.util.List;

public interface ApplicationConfigurationService {

  void updateXlraConfiguration(Configuration xlraConfiguration);

  Configuration getConfiguration();

  List<MailTemplate> getAllEmailTemplates();

  void updateEmailTemplate(MailTemplate mailTemplate);

}
