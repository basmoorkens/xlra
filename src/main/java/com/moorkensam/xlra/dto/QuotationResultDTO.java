package com.moorkensam.xlra.dto;

import com.moorkensam.xlra.model.rate.RateFile;
import com.moorkensam.xlra.model.rate.RateLine;

public class QuotationResultDTO {

	private String resultingMail;

	private RateFile resultingFile;

	private RateLine resultingLine;

	public RateLine getResultingLine() {
		return resultingLine;
	}

	public void setResultingLine(RateLine resultingLine) {
		this.resultingLine = resultingLine;
	}

	public RateFile getResultingFile() {
		return resultingFile;
	}

	public void setResultingFile(RateFile resultingFile) {
		this.resultingFile = resultingFile;
	}

	public String getResultingMail() {
		return resultingMail;
	}

	public void setResultingMail(String resultingMail) {
		this.resultingMail = resultingMail;
	}

}
