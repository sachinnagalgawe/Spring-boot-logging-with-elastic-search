package com.example.elasticdemo.config;

import java.sql.Timestamp;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.StringUtils;

import com.example.elasticdemo.model.Log;
import com.example.elasticdemo.model.Metadata;
import com.example.elasticdemo.model.Payload;
import com.example.elasticdemo.util.BeanUtil;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.CoreConstants;

public class TestAppender extends AppenderBase<ILoggingEvent> {

	private static final Logger logger = LoggerFactory.getLogger(TestAppender.class);

	@Async
	public void test(ILoggingEvent event) {
		if (event.getLoggerName().trim().toString().contains("com.example.elasticdemo") || event.getLoggerName().trim()
				.toString().contains("org.springframework.cloud.sleuth.instrument.web.ExceptionLoggingFilter")) {
			LogSubscriber logSubscriber = BeanUtil.getBean(LogSubscriber.class);
			System.out.println("Calling subscriber with msg "+event.getFormattedMessage());
			String stackTrace = "";
			if (event.getLoggerName().trim().toString()
					.contains("org.springframework.cloud.sleuth.instrument.web.ExceptionLoggingFilter")) {
				StringBuffer sbuf = new StringBuffer();
				IThrowableProxy throwbleProxy = event.getThrowableProxy();
				if (throwbleProxy != null) {
					String throwableStr = ThrowableProxyUtil.asString(throwbleProxy);
					sbuf.append(throwableStr);
					sbuf.append(CoreConstants.LINE_SEPARATOR);
				}
				stackTrace = sbuf.toString();
			}

			String className = "";
			String methodName = "";
			String generatedBy = "";
			final StackTraceElement[] stElements = event.getCallerData();
			if (stElements.length >= 1) {
				final StackTraceElement stackTraceElement = stElements[0];
				className = stackTraceElement.getClassName();
				methodName = stackTraceElement.getMethodName();
				generatedBy = className + "/" + methodName;
			}

			Log log = new Log();
			Metadata metadata = new Metadata();
			metadata.setPayloadType("midaslog");
			metadata.setEventName("LogEvent");// what is event name
			metadata.setCategory("OTC-PaymentMethodsService");
			Payload payload = new Payload();
			if (event.getLoggerName().trim().toString()
					.contains("org.springframework.cloud.sleuth.instrument.web.ExceptionLoggingFilter")) {
				payload.setLog(stackTrace);
			} else {
				payload.setLog(event.getFormattedMessage());
			}
			payload.setLogLevel(event.getLevel().toString());
			payload.setGeneratedBy(generatedBy);
			payload.setGeneratedOn(new Timestamp(new Date().getTime()));
			if (!StringUtils.isEmpty("INTERNAL_LOGS")) {
				payload.setIdentifier("INTERNAL_LOGS");
			}
			log.setMetadata(metadata);
			log.setPayload(payload);
			try {
				logSubscriber.subscribe(log);
			} catch (Exception e1) {
				System.out.println("Error: " + e1);
			}
		}
	}

	@Override
	protected void append(ILoggingEvent e) {
		this.test(e);
	}
}