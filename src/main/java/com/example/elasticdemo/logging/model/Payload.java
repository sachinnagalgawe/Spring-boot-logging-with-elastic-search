/* ***************************************************************************
 * Copyright 2020 VMware, Inc.  All rights reserved.
 * -- VMware Confidential
 * ***************************************************************************
 * $Author$ $Id$ $DateTime$
 * ***************************************************************************/

package com.example.elasticdemo.logging.model;

import java.sql.Timestamp;

/** 
 * <code>Payload</code> is model used to store actual payload in the log. <br>
 * 
 * @author Ambikeshwar Singh
 */
public class Payload {

    private String traceId;
    private String generatedby;
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
     * @param traceId
     * @param generatedby
     * @param generatedOn
     * @param identifier
     * @param log
     * @param logLevel
     */
    public Payload(String traceId, String generatedby, Timestamp generatedOn, String identifier, String log, String logLevel) {
        super();
        this.traceId = traceId;
        this.generatedby = generatedby;
        this.generatedOn = generatedOn;
        this.identifier = identifier;
        this.log = log;
        this.logLevel = logLevel;
    }

    /**
     * @return the traceId
     */
    public String getTraceId() {
        return traceId;
    }

    /**
     * @return the generatedby
     */
    public String getGeneratedby() {
        return generatedby;
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
     * @param generatedby the generatedby to set
     */
    public void setGeneratedby(String generatedby) {
        this.generatedby = generatedby;
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

}
