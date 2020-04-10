/* ***************************************************************************
 * Copyright 2018 VMware, Inc.  All rights reserved.
 * -- VMware Confidential
 * ***************************************************************************
 * $Author$ $Id$ $DateTime$
 * ***************************************************************************/

package com.example.elasticdemo.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

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

    /**
     * This method is used to log request patterns for ReST API calls coming to Payment Methods Service.
     * 
     * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
        //Logging request coming to user domain services. 
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
    }

}
