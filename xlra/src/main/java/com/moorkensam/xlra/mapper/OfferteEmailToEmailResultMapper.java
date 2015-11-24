package com.moorkensam.xlra.mapper;

import com.moorkensam.xlra.dto.OfferteMailDTO;
import com.moorkensam.xlra.model.mail.EmailResult;

public class OfferteEmailToEmailResultMapper {

	public EmailResult map(OfferteMailDTO input) {
		EmailResult result = new EmailResult();
		result.setEmail(input.getContent());
		result.setSubject(input.getSubject());
		result.setToAddress(input.getAddress());
		return result;
	}

}
