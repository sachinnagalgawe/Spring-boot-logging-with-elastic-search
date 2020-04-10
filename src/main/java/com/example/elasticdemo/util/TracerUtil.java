package com.example.elasticdemo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import brave.Span;
import brave.Tracer;

@Component
public class TracerUtil {

	private static final Logger logger = LoggerFactory.getLogger(TracerUtil.class);
	
	@Autowired
    private Tracer tracer;

	/**
	 * This method is used to return trace Id.
	 * 
	 * @return traceId.
	 */
	public String getTraceId() {
		String traceId = null;

		if (null != tracer) {
			Span currentSpan = tracer.currentSpan();
			if (null != currentSpan) {
				traceId = currentSpan.context().traceIdString();
			} else {
				logger.warn("TraceId is not available");
			}
			logger.debug("TraceId for the current request: {}", traceId);
		}
		return traceId;
	}
}
