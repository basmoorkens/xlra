package com.moorkensam.xlra.model.error;

public class PdfException extends Exception {

	private static final long serialVersionUID = -4145698228958030600L;

	private String businessException;

	public PdfException(String businessException, String msg, Throwable e) {
		super(msg, e);
		this.businessException = businessException;
	}

	public PdfException(String businessException, Throwable e) {
		super(e);
		this.businessException = businessException;
	}

	public PdfException(String businessException) {
		this.businessException = businessException;
	}

	public String getBusinessException() {
		return businessException;
	}

	public void setBusinessException(String businessException) {
		this.businessException = businessException;
	}

}
