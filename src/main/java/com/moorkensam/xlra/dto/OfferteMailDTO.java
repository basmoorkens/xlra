package com.moorkensam.xlra.dto;

public class OfferteMailDTO {

	private String address;

	private String subject;

	private String content;

	public OfferteMailDTO(String address, String subject, String content) {
		this.address = address;
		this.subject = subject;
		this.content = content;
	}

	public OfferteMailDTO() {

	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
