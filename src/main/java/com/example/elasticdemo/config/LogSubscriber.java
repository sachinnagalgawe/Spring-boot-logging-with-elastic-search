/*****************************************************************************
 * Copyright 2020 VMware, Inc.  All rights reserved.
 * -- VMware Confidential
 * ***************************************************************************
 * $Author$ $Id$ $DateTime$
 * ***************************************************************************/

package com.example.elasticdemo.config;

import java.io.IOException;

import org.apache.commons.text.StringEscapeUtils;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.example.elasticdemo.model.Log;
import com.example.elasticdemo.model.Metadata;
import com.example.elasticdemo.model.Payload;
import com.example.elasticdemo.util.TracerUtil;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

/**
 * <code>LogSubscriber</code> is used to subscribe the provided logs. <br>
 * 
 * @author Ambikeshwar Singh
 */
@Component
public class LogSubscriber {

	private static final Logger LOG = LoggerFactory.getLogger(LogSubscriber.class);

	@Autowired
	private Client client;
	
	@Autowired
	private TracerUtil tracerUtil;
	
	/**
	 * Consume logs from the Queue to store in Elastic DB.
	 * 
	 * @param message
	 * @throws IOException
	 */
	//@RabbitListener(queues = ElasticSyncServiceConstant.ELASTIC_LOGGER_QUEUE_NAME, autoStartup = ElasticSyncServiceConstant.ELASTIC_LOGGER_QUEUE_AUTOSTART, containerFactory = "jsaFactory")
	@Async
	public void subscribe(final Log message) throws IOException {
		System.out.println("Elastic Sync Service message got : [{}] and traceId : [{}] "+ message+ tracerUtil.getTraceId());

		Metadata metadata = message.getMetadata();
		Payload payload = message.getPayload();
		payload.setTraceId(tracerUtil.getTraceId());
		XContentBuilder builder = XContentFactory.jsonBuilder();
		builder.startObject().field("generatedBy", payload.getGeneratedBy())
				.field("identifier", payload.getIdentifier()).field("log", StringEscapeUtils.unescapeJava(payload.getLog()))
				.field("logLevel", payload.getLogLevel());
		
		if(null != payload.getTraceId()) {
			builder.field("traceId", payload.getTraceId());
		}

		if (null != payload.getGeneratedOn()) {
			builder.field("generatedOn", payload.getGeneratedOn().toString());
		}
		builder.endObject();
		IndexResponse response = client.prepareIndex(metadata.getPayloadType(), metadata.getCategory()).setSource(builder).get();
		//LOG.info("Elastic Sync Service response :- [{}]", response);
	}
}
