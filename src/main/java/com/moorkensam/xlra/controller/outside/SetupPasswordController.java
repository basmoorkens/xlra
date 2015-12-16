package com.moorkensam.xlra.controller.outside;

import com.moorkensam.xlra.controller.util.MessageUtil;
import com.moorkensam.xlra.model.security.User;
import com.moorkensam.xlra.service.UserService;
import com.moorkensam.xlra.service.util.ConfigurationLoader;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

@ManagedBean
@ViewScoped
public class SetupPasswordController {

  @Inject
  private UserService userService;

  private String password;

  private String confirmPassword;

  private boolean validRequest = false;

  private User user;

  private String welcomeMessage;

  private boolean completed;

  private String loginLink;

  private ConfigurationLoader config;

  /**
   * Initializes the controller.
   */
  @PostConstruct
  public void initialize() {
    config = ConfigurationLoader.getInstance();
    loginLink = config.getProperty(ConfigurationLoader.APPLICATION_BASE_URL);
    Map<String, String> requestParameters =
        FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
    String userEmail = requestParameters.get("email");
    String token = requestParameters.get("token");
    user = userService.isValidPasswordRequest(userEmail, token);
    if (user == null) {
      setValidRequest(false);
    } else {
      setValidRequest(true);
      welcomeMessage = "Hello " + user.getFullName();
    }
  }

  /**
   * Saves the password.
   */
  public void savePassword() {
    if (validatePasswords()) {
      userService.setPasswordAndActivateUser(user, password);
      MessageUtil.addMessage("Password set",
          "Your account is now active. You can login with your email and the provided password.");
      completed = true;
    }
  }

  private boolean validatePasswords() {
    if (password == null || password.isEmpty() || confirmPassword == null
        || confirmPassword.isEmpty()) {

      MessageUtil.addErrorMessage("Fill in passwords", "Please fill in both password fields!");
      return false;
    } else {
      if (!password.equals(confirmPassword)) {
        MessageUtil.addErrorMessage("Passwords don't match",
            "Please make sure that the password and confirm password field are the same!");
        return false;
      }
    }
    return true;
  }

  public String getConfirmPassword() {
    return confirmPassword;
  }

  public void setConfirmPassword(String confirmPassword) {
    this.confirmPassword = confirmPassword;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public boolean isValidRequest() {
    return validRequest;
  }

  public void setValidRequest(boolean validRequest) {
    this.validRequest = validRequest;
  }

  public String getWelcomeMessage() {
    return welcomeMessage;
  }

  public void setWelcomeMessage(String welcomeMessage) {
    this.welcomeMessage = welcomeMessage;
  }

  public boolean isCompleted() {
    return completed;
  }

  public void setCompleted(boolean completed) {
    this.completed = completed;
  }

  public String getLoginLink() {
    return loginLink;
  }

  public void setLoginLink(String loginLink) {
    this.loginLink = loginLink;
  }

  public boolean isValidAndNotCompleted() {
    return validRequest && !completed;
  }

}
