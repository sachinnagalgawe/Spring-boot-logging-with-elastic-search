/* ***************************************************************************
 * Copyright 2018 VMware, Inc.  All rights reserved.
 * -- VMware Confidential
 * ***************************************************************************
 * $Author$ $Id$ $DateTime$
 * ***************************************************************************/

package com.example.elasticdemo.config;

/**
 * <code>LogPatternConstants</code> is is used to hold log pattern constants
 * used in Payment services.
 * 
 * @author Hitesh Wagh
 */
public class LogPatternConstants {

    /**
     * Separator used in log pattern.
     */
    public static final String LOG_SEPARATOR = " :: ";

    /**
     * Log pattern constant to log attributes for SLF4J with [] for checking
     * nulls/spaces.
     */
    public static final String SLF4J_LOG_ATTRIBUTE_PATTERN = LOG_SEPARATOR + "[{}]";

    /**
     * Log pattern constant to log attributes for SLF4J without [] for logging
     * objects.
     */
    public static final String SLF4J_LOG_ATTRIBUTE_PATTERN2 = LOG_SEPARATOR + "{}";

    /**
     * Log pattern constant for datacentre name details.
     */
    public static final String CF_DC_NAME = "CF_DC_NAME: ";

    /**
     * App level Success pattern constant.
     */
    public static final String SUCCESS = "SUCCESS";

    /**
     * App level Error pattern constant.
     */
    public static final String ERROR = "ERROR";

    /**
     * Constant for Colon character.
     */
    public static final String COLON = ":";

    /**
     * Constant for dash character.
     */
    public static final String DASH = "-";

    /**
     * Log pattern constant for MariaDB Operations.
     */
    public static final String MARIA_DB_OPERATION = "MARIA_DB_OPERATION";

    /**
     * Log pattern constant for SDP DB Operations.
     */
    public static final String SDP_DB_OPERATION = "SDP_DB_OPERATION";

    /**
     * Log pattern constant for EPP DB Operations.
     */
    public static final String EPP_DB_OPERATION = "EPP_DB_OPERATION";

    /**
     * Log pattern constant for EBS DB Operations.
     */
    public static final String EBS_DB_OPERATION = "EBS_DB_OPERATION";

    /**
     * Log pattern constant for APPS DB Operations.
     */
    public static final String APPS_DB_OPERATION = "APPS_DB_OPERATION";

    /**
     * Log pattern constant for business error
     */
    public static final String BUSINESS_ERROR = "BUSINESS_ERROR";

    /**
     * Log pattern constant for Database communication error.
     */
    public static final String NON_TRANSIENT_ERROR = "NON_TRANSIENT_ERROR";

    /**
     * Log pattern constant for Database transient data access error.
     */
    public static final String TRANSIENT_ERROR = "TRANSIENT_ERROR";

    /**
     * Log pattern constant for Database invalid data access resource usage
     * error.
     */
    public static final String INVALID_DATA_ACCESS_RESOURCE_USAGE_ERROR = "INVALID_RESOURCE_USAGE";

    /**
     * Log pattern constant for Uncategorized data access error.
     */
    public static final String UNCATEGORIZED_DATA_ACCESS_ERROR = "UNCATEGORIZED_ERROR";

    /**
     * Log pattern constant for db script error.
     */
    public static final String SCRIPT_ERROR = "SCRIPT_ERROR";

    /**
     * Log pattern constant for retry attempt string.
     */
    public static final String RETRY_ATTEMPT = "RETRY_ATTEMPT: [{}]";

    /**
     * Log pattern constant for exception class name.
     */
    public static final String EXCEPTION_DETAILS = "EXCEPTION_DETAILS";

    /**
     * Space character.
     */
    public static final String SPACE = " ";

    /**
     * Log pattern constant for application name for Payment Methods Service.
     */
    public static final String PAYMENT_METHODS_SERVICE = "PAYMENT_METHODS_SERVICE";

    /**
     * Log pattern constant for application name for Payment Methods Fund
     * Service.
     */
    public static final String PAYMENT_METHODS_FUND_SERVICE = "PAYMENT_METHODS_FUND_SERVICE";

    /**
     * Log pattern constant for method to register credit card.
     */
    public static final String REGISTER_CREDIT_CARD_METHOD = "Register Credit Card";

    /**
     * Log pattern constant for method Payment Methods Service - register credit
     * card.
     */
    public static final String REGISTER_CREDIT_CARD_API_METHOD = "REGISTER_CREDIT_CARD_API";

    /**
     * Log pattern constant for API Payment Methods Service - Fetch Payment
     * Methods by CN
     */
    public static final String FETCH_PAYMENT_METHODS_BY_CN_API_METHOD = "FETCH_PAYMENT_METHODS_BY_CN";

    /**
     * Log pattern constant for API Payment Methods Service - Fetch Payment
     * Methods by optionIds.
     */
    public static final String FETCH_PAYMENT_METHODS_BY_OPTION_IDS_API_METHOD = "FETCH_PAYMENT_METHODS_BY_OPTIONS";

    /**
     * Log pattern constant for method Payment Methods Fund Service - fetch
     * funds by CN.
     */
    public static final String FETCH_FUNDS_BY_CN = "FETCH_FUNDS_BY_CN";

    /**
     * Log pattern constant for request details.
     */
    public static final String REQUEST_DETAILS = "Request Details: {}";

    /**
     * Log pattern constant for validation.
     */
    public static final String VALIDATION = "Validation";

    /**
     * Log pattern constant for validation success.
     */
    public static final String VALIDATION_SUCCESS = "Success";

    /**
     * Log pattern constant for validation error.
     */
    public static final String VALIDATION_ERROR = "Error :: [{}]";

    /**
     * Log pattern constant for validation failure cause - invalid value
     */
    public static final String VALIDATION_FAILURE_CAUSE_INVALID_VALUE = "Invalid Value";

    /**
     * Log pattern constant for validation failure cause - inactive user
     */
    public static final String VALIDATION_FAILURE_CAUSE_INACTIVE_USER = "Inactive User";

    /**
     * Log pattern constant for validation failure cause - inactive user
     */
    public static final String VALIDATION_FAILURE_CAUSE_NULL_USER = "Null User Details";

    /**
     * Log pattern constant for validation failure cause - inactive user
     */
    public static final String VALIDATION_FAILURE_CAUSE_CSP_ERROR = "CSP Error";

    /**
     * Log pattern constant for validation failure cause - mandatory field
     */
    public static final String VALIDATION_FAILURE_CAUSE_MANDATORY_FIELD = "Mandatory Field";

    /**
     * Log pattern constant for validation failure cause - wrong format
     */
    public static final String VALIDATION_FAILURE_CAUSE_WRONG_FORMAT = "Wrong Format";

    /**
     * Log pattern constant for ReST API call.
     */
    public static final String REST_API_CALL = "REST_API_CALL";

    /**
     * Log pattern constant for HTTP Error code for CSP ReST API Call.
     */
    public static final String HTTP_ERROR_CODE = "HTTP_ERROR";

    /**
     * Log pattern constant for IO Error for CSP ReST API Call.
     */
    public static final String IO_ERROR = "IO_ERROR";

    /**
     * Log pattern constant for CSP Login OAuth API
     */
    public static final String CSP_LOGIN_OAUTH_API = "CSP" + LOG_SEPARATOR + "LOGIN" + LOG_SEPARATOR + "OAUTH";

    /**
     * Log pattern constant for Payment Invoice processing events.
     */
    public static final String INVOICE_PROCESSING = "INVOICE_PROCESSING";

    /**
     * Log pattern constant for Payment Invoice processing events.
     */
    public static final String INVOICE_PROCESSING_RETRY = "INVOICE_PROCESSING_RETRY";

    /**
     * Log pattern constant for RMQ eventId.
     */
    public static final String EVENT_ID = "EVENT_ID: [%s]";

    /**
     * Log pattern constant for RMQ event Name.
     */
    public static final String EVENT_NAME = "EVENT_NAME: [%s]";

    /**
     * Log pattern constant for RMQ routing key.
     */
    public static final String ROUTING_KEY = "ROUTING_KEY: [%s]";

    /**
     * Log pattern constant for RMQ exchange info.
     */
    public static final String EXCHANGE_INFO = "EXCHANGE_INFO: [%s]";

    /**
     * Log pattern constant for current invoice processing state
     */
    public static final String CURRENT_STATE = "CURRENT_STATE: [%s]";

    /**
     * Log pattern constant for current invoice processing state
     */
    public static final String PREVIOUS_STATE = "PREVIOUS_STATE: [%s]";

    /**
     * Log pattern constant for invoice processing error details.
     */
    public static final String ERROR_DETAILS = "ERROR_DETAILS: [%s]";

    /**
     * Log pattern constant for invoice processing scheduler job start event.
     */
    public static final String JOB_STARTED = "JOB_STARTED";

    /**
     * Log pattern constant for invoice processing scheduler job completion
     * event
     */
    public static final String JOB_COMPLETED = "JOB_COMPLETED";

    /**
     * Log pattern constant for processed invoices count.
     */
    public static final String TOTAL_INVOICES_PROCESSED = "TOTAL_INVOICES_PROCESSED: [%s]";

    /**
     * Log pattern constant for count of invoices processed successfully.
     */
    public static final String SUCCESSFULLY_PROCESSED_INVOICES = "SUCCESSFULLY_PROCESSED_INVOICES: [%s]";

    /**
     * Log pattern constant for count of invoices for which processing failed.
     */
    public static final String FAILED_INVOICES = "FAILED_INVOICES: [%s]";

    /**
     * Log pattern constant for count of technical failures occurred during
     * retry scheduling job run.
     */
    public static final String TECHNICAL_FAILURES = "TECHNICAL_FAILURES: [%s]";

    /**
     * Log pattern constant for count of business failures occurred during retry
     * scheduling job run.
     */
    public static final String BUSINESS_FAILURES = "BUSINESS_FAILURES: [%s]";

    /**
     * Log pattern constant for count of invoices skipped during retry scheduling job run
     */
    public static final String SKIPPED_INVOICES = "SKIPPED_INVOICES: [%s]";

    /**
     * Log pattern constant for count of invoices for which retry count is maxed
     * out.
     */
    public static final String RETRY_MAX_OUT_INVOICES = "RETRY_MAX_OUT_INVOICES: [%s]";

    /**
     * Log pattern constant for payment invoice event source.
     */
    public static final String INVOICE_SOURCE = "SOURCE: [%s]";

    /**
     * Log pattern constant for invoice processing business error.
     */
    public static final String INVOICE_PROCESSING_BUSINESS_ERROR = BUSINESS_ERROR + " - [%s]";

    /**
     * Log pattern constant for invoice processing technical error.
     */
    public static final String INVOICE_PROCESSING_TECHNICAL_ERROR = "TECHNICAL_ERROR" + " - [%s]";

    /**
     * Log pattern constant for RMQ Operations
     */
    public static final String RMQ_OPERATION = "RMQ_OPERATION";

    /**
     * Log pattern constant for invoice processing status.
     */
    public static final String INVOICE_PROCESSING_STATUS = "INVOICE_STATUS: [%s]";

    /**
     * Log pattern constant for publishing RMQ event.
     */
    public static final String PUBLISH_EVENT = "PUBLISH_EVENT";

    /**
     * Log pattern constant for current retry count.
     */
    public static final String RETRY_COUNT = "RETRY_COUNT: [%s]";

    /**
     * Log pattern constant for datacentre in which payment invoice was orginally consumed.
     */
    public static final String INVOICE_RECEIVED_IN = "INVOICE_RECEIVED_IN: [%s]";

    /**
     * Log pattern constant when max retry reached for invoice.
     */
    public static final String MAX_RETRY_REACHED = "MAX_RETRY_REACHED";

    /**
     * Private constructor to hide class level details.
     */
    private LogPatternConstants() {
    }

}