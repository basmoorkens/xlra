package com.moorkensam.xlra.controller;

import com.moorkensam.xlra.controller.util.MessageUtil;
import com.moorkensam.xlra.model.configuration.Language;
import com.moorkensam.xlra.model.security.User;
import com.moorkensam.xlra.service.UserService;
import com.moorkensam.xlra.service.UserSessionService;
import com.moorkensam.xlra.service.util.LocaleUtil;

import org.primefaces.context.RequestContext;

import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

@ManagedBean
@ViewScoped
public class UserController {

  @Inject
  private UserService userService;

  private LocaleUtil localeUtil;

  private User user;

  private String newPassword;

  private String retypedPassword;

  private Language languageForUser;

  @Inject
  private UserSessionService userSessionService;

  @ManagedProperty("#{msg}")
  private ResourceBundle messageBundle;

  @ManagedProperty("#{localeController}")
  private LocaleController localeController;

  private MessageUtil messageUtil;

  /**
   * Initialise the controller with predefined data.
   */
  @PostConstruct
  public void initialize() {
    messageUtil = MessageUtil.getInstance(messageBundle);
    localeUtil = new LocaleUtil();
    loadUserForEdit();
  }

  private void loadUserForEdit() {
    user = userSessionService.getLoggedInUser();
    languageForUser = user.getLanguage();
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public UserService getUserService() {
    return userService;
  }

  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  public String getNewPassword() {
    return newPassword;
  }

  public void setNewPassword(String newPassword) {
    this.newPassword = newPassword;
  }

  public String getRetypedPassword() {
    return retypedPassword;
  }

  public void setRetypedPassword(String retypedPassword) {
    this.retypedPassword = retypedPassword;
  }

  /**
   * Get all supported languages and fill in translation by user locale.
   * 
   * @return The transated langauges
   */
  public List<Language> getSupportedLanguages() {
    List<Language> supportedLanguages = localeController.getSupportedLanguages();
    localeUtil.fillInLanguageTranslations(supportedLanguages, getMessageBundle());
    return supportedLanguages;
  }

  /**
   * Save the user.
   */
  public void saveUser() {
    boolean updatedLocale = false;
    if (user.getLanguage() != languageForUser) {
      user.setLanguage(languageForUser);
      updatedLocale = true;
    }
    user = userService.updateUser(user);

    if (updatedLocale) {
      notifyLocaleController();
      messageUtil.addMessage("message.user.profile.updated.title",
          "message.user.profile.updated.detail.locale");
    } else {
      messageUtil.addMessage("message.user.profile.updated.title",
          "message.user.profile.updated.detail");
    }
  }

  private void notifyLocaleController() {
    localeController.loadUserLocale(true);
  }

  public void hidePasswordDialog() {
    RequestContext context = RequestContext.getCurrentInstance();
    context.execute("PF('changePasswordDialog').hide();");
  }

  public void showPasswordDialog() {
    RequestContext context = RequestContext.getCurrentInstance();
    context.execute("PF('changePasswordDialog').show();");
  }

  /**
   * Save the new password for the user if it is valid.
   */
  public void savePassword() {
    if (validatePasswords()) {
      user.setPassword(newPassword);
      user = userService.updateUserPassword(user);
      messageUtil.addMessage("message.user.profile.updated.title",
          "message.user.profile.updated.detail");
    } else {
      showPasswordDialog();
    }
  }

  /**
   * Redirect to the current page to force refresh the entire page so the menu and header gets
   * refreshed aswell.
   * 
   * @throws IOException
   */
  public void refreshFullPage() throws IOException {
    FacesContext
        .getCurrentInstance()
        .getExternalContext()
        .redirect(
            FacesContext.getCurrentInstance().getExternalContext().getApplicationContextPath()
                + "/views/user/editProfile.xhtml");
  }

  private boolean validatePasswords() {
    if (newPassword == null || newPassword.isEmpty() || retypedPassword == null
        || retypedPassword.isEmpty()) {
      messageUtil.addErrorMessage("message.user.save.password.empty.passwords.title",
          "message.user.save.password.empty.passwords.detail");
      return false;
    } else {
      if (!newPassword.equals(retypedPassword)) {
        messageUtil.addErrorMessage("message.user.profile.password.mismatch.title",
            "message.user.profile.password.mismatch.detail");
        return false;
      }
    }
    return true;
  }

  public MessageUtil getMessageUtil() {
    return messageUtil;
  }

  public void setMessageUtil(MessageUtil messageUtil) {
    this.messageUtil = messageUtil;
  }

  public ResourceBundle getMessageBundle() {
    return messageBundle;
  }

  public void setMessageBundle(ResourceBundle messageBundle) {
    this.messageBundle = messageBundle;
  }

  public Language getLanguageForUser() {
    return languageForUser;
  }

  public void setLanguageForUser(Language languageForUser) {
    this.languageForUser = languageForUser;
  }

  public LocaleController getLocaleController() {
    return localeController;
  }

  public void setLocaleController(LocaleController localeController) {
    this.localeController = localeController;
  }

}
