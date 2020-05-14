/* ***************************************************************************
 * Copyright 2020 VMware, Inc.  All rights reserved.
 * -- VMware Confidential
 * ***************************************************************************
 * $Author$ $Id$ $DateTime$
 * ***************************************************************************/

package com.example.elasticdemo.model;

import java.sql.Timestamp;

import org.springframework.data.annotation.Id;

/**
 * <code>Payload</code> is model used to store actual payload in the log. <br>
 * 
 * @author Ambikeshwar Singh
 */
public class Payload {

	private String serviceId;
	private String traceId;
	private String generatedBy;
	private Timestamp generatedOn;
	private String identifier;
	private String log;
	private String logLevel;

	/**
	 * 
	 */
	public Payload() {
	}

	/**
	 * @return the traceId
	 */
	public String getTraceId() {
		return traceId;
	}

	/**
	 * @return the generatedBy
	 */
	public String getGeneratedBy() {
		return generatedBy;
	}

	/**
	 * @return the generatedOn
	 */
	public Timestamp getGeneratedOn() {
		return generatedOn;
	}

	/**
	 * @return the identifier
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * @return the log
	 */
	public String getLog() {
		return log;
	}

	/**
	 * @return the logLevel
	 */
	public String getLogLevel() {
		return logLevel;
	}

	/**
	 * @param traceId the traceId to set
	 */
	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}

	/**
	 * @param generatedBy the generatedBy to set
	 */
	public void setGeneratedBy(String generatedBy) {
		this.generatedBy = generatedBy;
	}

	/**
	 * @param generatedOn the generatedOn to set
	 */
	public void setGeneratedOn(Timestamp generatedOn) {
		this.generatedOn = generatedOn;
	}

	/**
	 * @param identifier the identifier to set
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	/**
	 * @param log the log to set
	 */
	public void setLog(String log) {
		this.log = log;
	}

	/**
	 * @param logLevel the logLevel to set
	 */
	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}

	/**
	 * @param serviceId
	 */
	public String getServiceId() {
		return serviceId;
	}

	/**
	 * @param set service id
	 */
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	
	@Override
	public String toString() {
		return "Payload [serviceId=" + serviceId + ", traceId=" + traceId + ", generatedBy=" + generatedBy
				+ ", generatedOn=" + generatedOn + ", identifier=" + identifier + ", log=" + log + ", logLevel="
				+ logLevel + "]";
	}

}
