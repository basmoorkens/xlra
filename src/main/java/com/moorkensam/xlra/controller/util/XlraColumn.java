package com.moorkensam.xlra.controller.util;

import java.io.Serializable;

public class XlraColumn implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4396650932546823297L;
	private String header;
	private String property;

	public XlraColumn(String header, String property) {
		this.header = header;
		this.property = property;
	}

	public String getHeader() {
		return header;
	}

	public String getProperty() {
		return property;
	}
}
