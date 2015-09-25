package com.moorkensam.xlra.model.configuration;

import java.util.Date;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.moorkensam.xlra.model.BaseEntity;

@MappedSuperclass
public abstract class LogRecord extends BaseEntity {

	private static final long serialVersionUID = -9102740273707056738L;

	@Temporal(TemporalType.TIMESTAMP)
	protected Date logDate;

	@Enumerated(EnumType.STRING)
	protected LogType type;

	public Date getLogDate() {
		return logDate;
	}

	public void setLogDate(Date logDate) {
		this.logDate = logDate;
	}

	public LogType getType() {
		return type;
	}

	public void setType(LogType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return getLogDate() + " - " + type;
	}

	
	
}
