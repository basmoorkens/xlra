package com.moorkensam.xlra.service;

import com.moorkensam.xlra.model.error.UnAuthorizedAccessException;
import com.moorkensam.xlra.model.offerte.QuotationResult;

public interface AuthorizationService {

  public void authorizeOfferteAccess(final QuotationResult offerte)
      throws UnAuthorizedAccessException;

}
