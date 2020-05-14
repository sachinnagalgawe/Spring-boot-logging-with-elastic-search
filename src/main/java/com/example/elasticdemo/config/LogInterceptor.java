/* ***************************************************************************
 * Copyright 2018 VMware, Inc.  All rights reserved.
 * -- VMware Confidential
 * ***************************************************************************
 * $Author$ $Id$ $DateTime$
 * ***************************************************************************/

package com.example.elasticdemo.config;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.example.elasticdemo.model.Log;
import com.example.elasticdemo.model.Metadata;
import com.example.elasticdemo.model.Payload;
import com.example.elasticdemo.model.RequestLogDetails;
import com.example.elasticdemo.model.ResponseLogDetails;
import com.example.elasticdemo.util.TracerUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * <p>
 * <code>LogInterceptor</code> is used for logging Payment Methods Service
 * requests and responses
 * 
 * @author Hitesh Wagh
 */
@Component
@Order(value = 1)
public class LogInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(LogInterceptor.class);
    
	private ObjectMapper mapper = new ObjectMapper();
	
	@Autowired
	private LogSubscriber logSubscriber;
	
	@Autowired
	private TracerUtil tracer;
	
	@Autowired
	private ConfigHolder configHolder;

    /**
     * This method is used to log request patterns for ReST API calls coming to Payment Methods Service.
     * 
     * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
        //Logging request coming to user domain services. 
		System.out.println("In pre handle");
    	logger.info("{}{} :: REQUEST :: {} :: {} :: {} :: {}", LogPatternConstants.CF_DC_NAME, "DatacenterName", "SpringApplicationName", request.getMethod(), request.getRequestURI(), request.getProtocol());
        return true;
    }

    /**
     * This method is used to log success/error response patterns for ReST API calls coming to Payment Methods Service.
     * 
     * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#afterCompletion(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception arg3) throws Exception {
    	if (response.getStatus() >= 400) {
            //Logging response error which is getting returned from payment services. 
            logger.error("{}{} :: RESPONSE :: {} :: {} :: RESPONSE_STATUS: {} :: {} :: {} :: {}", LogPatternConstants.CF_DC_NAME, "DatacenterName", "SpringApplicationName", LogPatternConstants.ERROR, response.getStatus(), request.getMethod(), request.getRequestURI(), request.getProtocol());
    	} else {
            //Logging response success which is getting returned from payment services. 
            logger.info("{}{} :: RESPONSE :: {} :: {} :: RESPONSE_STATUS: {} :: {} :: {} :: {}", LogPatternConstants.CF_DC_NAME, "DatacenterName", "SpringApplicationName", LogPatternConstants.SUCCESS, response.getStatus(), request.getMethod(), request.getRequestURI(), request.getProtocol());
        }
    	
		if(configHolder.isEnableElasticLogs()) {
	    	System.out.println("In afterCompletion");
	    	// trace Id
			String traceId = tracer.getTraceId();

	    	if(request instanceof ContentCachingRequestWrapper) {
	    		System.out.println("request is wrapper");
		        byte[] buf = ((ContentCachingRequestWrapper)request).getContentAsByteArray();
				String requestBody = getContentAsString(buf, 100000, request.getCharacterEncoding());
				System.out.println("requestBody: "+requestBody);
				
				// Query params
				String queryString = request.getQueryString();

				// Request Headers
				Map<String, String> requestHeaders = new HashMap<String, String>();
				Enumeration headerNames = request.getHeaderNames();
				while (headerNames.hasMoreElements()) {
					String key = (String) headerNames.nextElement();
					String value = request.getHeader(key);
					requestHeaders.put(key, value);
				}
				
				// Set the details into object
				RequestLogDetails requestLogDetails = new RequestLogDetails(traceId, request.getMethod(), request.getRequestURL().toString(), queryString, requestHeaders, requestBody);
				String requestLogDetailsString = mapper.writeValueAsString(requestLogDetails);
				
				// Create log
				this.createLog(traceId, requestLogDetailsString, "INFO", "REQUEST");
			}
			
			if((response instanceof ContentCachingResponseWrapper) || (request.getAttribute("errorResponseDetails") != null)) {
				String responseBody = "";
				HttpHeaders responseHeaders = null;
				if(response instanceof ContentCachingResponseWrapper) {
		    		System.out.println("response is wrapper");
					ContentCachingResponseWrapper wrappedResponse = ((ContentCachingResponseWrapper)response);
					byte[] buf = wrappedResponse.getContentAsByteArray();
					responseBody = getContentAsString(buf, 100000, response.getCharacterEncoding());
					wrappedResponse.copyBodyToResponse();
					responseHeaders = this.getResponseHeaders(wrappedResponse);
				}else if(request.getAttribute("errorResponseDetails") != null && response.getStatus() >= 400) {
		    		System.out.println("response is errorResponseDetails");
					ResponseLogDetails responseLogDetails = (ResponseLogDetails) request.getAttribute("errorResponseDetails");
					responseHeaders = getResponseHeaders(response);
					responseLogDetails.setHeaders(responseHeaders);
					if(!StringUtils.isEmpty(responseLogDetails.getBody())) {
						responseBody = responseLogDetails.getBody();
					}
				}
				
				long startTime = (long)request.getAttribute("startTime");
				long timeElapsed = System.currentTimeMillis() - startTime;

				ResponseLogDetails responseLogDetails = new ResponseLogDetails(traceId, responseHeaders, responseBody, response.getStatus(), timeElapsed);
				String responseLogDetailsString = mapper.writeValueAsString(responseLogDetails);
				if (!StringUtils.isEmpty(responseBody)) {
					if (response.getStatus() >= 400) {
						this.createLog(traceId, responseLogDetailsString, "ERROR", "RESPONSE");
					} else {
						this.createLog(traceId, responseLogDetailsString, "INFO", "RESPONSE");
					}
				}
			}
		}
    }
    
	private HttpHeaders getResponseHeaders(HttpServletResponse response) {
		String responseHeadersJson = "";
		HttpHeaders responseHeaders = new HttpHeaders();
		for (String headerName : response.getHeaderNames()) {
			responseHeaders.add(headerName, response.getHeader(headerName));
		}
		return responseHeaders;
	}

	private HttpHeaders getResponseHeaders(ContentCachingResponseWrapper responseWrapper) {
		String responseHeadersJson = "";
		HttpHeaders responseHeaders = new HttpHeaders();
		for (String headerName : responseWrapper.getHeaderNames()) {
			responseHeaders.add(headerName, responseWrapper.getHeader(headerName));
		}
		return responseHeaders;
	}
  
	/**
	 * Create a log
	 *
	 * @param traceId
	 * @param logTxt
	 * @param logLevel
	 * @param identifire
	 */
	private void createLog(String traceId, String logTxt, String logLevel, String identifire) {
		String generatedBy = this.getClass().getName().toString() + "/doFilterInternal";
		Log log = new Log();
		Metadata metadata = new Metadata();
		metadata.setPayloadType("midaslog");
		metadata.setEventName("LogEvent");
		metadata.setCategory("System-Log");

		Payload payload = new Payload();
		payload.setServiceId("elastic demo");
		payload.setTraceId(traceId);
		payload.setLog(logTxt);
		payload.setLogLevel(logLevel);
		payload.setGeneratedBy(generatedBy);
		payload.setGeneratedOn(new Timestamp(new Date().getTime()));
		if (!StringUtils.isEmpty(identifire)) {
			payload.setIdentifier(identifire);
		}
		log.setMetadata(metadata);
		log.setPayload(payload);
		try {
			logSubscriber.subscribe(log);
		} catch (IOException e) {
			logger.error("Error saving log: ", e);
		}
	}
    
	private String getContentAsString(byte[] buf, int maxLength, String charsetName) {
		if (buf == null || buf.length == 0)
			return "";
		int length = Math.min(buf.length, 100000);
		try {
			return new String(buf, 0, length, charsetName);
		} catch (UnsupportedEncodingException ex) {
			return "Unsupported Encoding";
		}
	}
}
