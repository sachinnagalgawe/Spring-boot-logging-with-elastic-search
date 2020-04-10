/* ***************************************************************************
 * Copyright 2018 VMware, Inc.  All rights reserved.
 * -- VMware Confidential
 * ***************************************************************************
 * $Author$ $Id$ $DateTime$
 * ***************************************************************************/

package com.example.elasticdemo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/** 
 * <code>LogConfiguration</code> is used for log configuration for Payment Services.
 * 
 * @author Hitesh Wagh.
 */
@Configuration
public class LogConfiguration implements WebMvcConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(LogConfiguration.class);

    @Autowired
    private LogInterceptor logInterceptor;

    /**
     * This method is used to add {@link LogInterceptor} to
     * {@link InterceptorRegistry} for path patterns specified.
     * 
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Checking if LogInterceptor is enabled.
        if (true) {

            // Adding LogInterceptor to InterceptorRegistry
            registry.addInterceptor(logInterceptor).addPathPatterns("/**");

            if (logger.isDebugEnabled()) {
                logger.debug("Enabled LogInterceptor for [{}] application for path pattern: [{}]", "Demo-app", "/**");
            }
        }
    }

}