/* ***************************************************************************
 * Copyright 2020 VMware, Inc.  All rights reserved.
 * -- VMware Confidential
 * ***************************************************************************
 * $Author$ $Id$ $DateTime$
 * ***************************************************************************/

package com.example.elasticdemo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/** 
 * <code>BeanUtil</code> is class to get application context and create bean of a class <br>
 *  
 * @author 
 */
@Service
public class BeanUtil implements ApplicationContextAware {

    private static ApplicationContext context;

    private static final Logger logger = LoggerFactory.getLogger(BeanUtil.class);
    /**
     *This method is to get Application context which is set during application initialization. 
     * 
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
    
    /**
     * This method returns bean of the class passed to it.
     * 
     * @param <T>
     * @param beanClass
     * @return bean of the  class passes as parameter
     */
    public static <T> T getBean(Class<T> beanClass) {
        logger.info("In bean util [{}]", beanClass);
        return context.getBean(beanClass);
    }
}
