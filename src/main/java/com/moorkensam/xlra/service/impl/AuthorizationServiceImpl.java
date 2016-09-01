package com.moorkensam.xlra.service.impl;

import com.moorkensam.xlra.model.error.UnAuthorizedAccessException;
import com.moorkensam.xlra.model.offerte.QuotationResult;
import com.moorkensam.xlra.model.security.User;
import com.moorkensam.xlra.service.AuthorizationService;
import com.moorkensam.xlra.service.UserSessionService;

import java.util.Arrays;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class AuthorizationServiceImpl implements AuthorizationService {

  @Inject
  private UserSessionService userSessionService;

  @Override
  public void authorizeOfferteAccess(QuotationResult offerte) throws UnAuthorizedAccessException {
    User user = getUserSessionService().getLoggedInUser();
    if (!getUserSessionService().doesLoggedInUserHaveAdminRights()) {
      if (!user.getUserName().equalsIgnoreCase(offerte.getCreatedUserName())) {
        UnAuthorizedAccessException exc =
            new UnAuthorizedAccessException("message.offerte.unauthorized.access.detail");
        exc.setExtraArguments(Arrays.asList(user.getUserName()));
      }
    }
  }

  public UserSessionService getUserSessionService() {
    return userSessionService;
  }

  public void setUserSessionService(UserSessionService userSessionService) {
    this.userSessionService = userSessionService;
  }

}
