package com.example.elasticdemo.config;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.scheduling.annotation.Async;
import org.springframework.util.StringUtils;

import com.example.elasticdemo.model.Log;
import com.example.elasticdemo.model.Metadata;
import com.example.elasticdemo.model.Payload;
import com.example.elasticdemo.util.BeanUtil;
import com.example.elasticdemo.util.TracerUtil;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.CoreConstants;

public class TestAppender extends AppenderBase<ILoggingEvent> {

	@Async
	public void test(ILoggingEvent event) {
		ConfigHolder configHolder = BeanUtil.getBean(ConfigHolder.class);
		if(configHolder.isEnableElasticLogs()) {
			boolean enableInternalLogs = configHolder.isEnableInternalLogs();
			String internalLogLevel = configHolder.getInternalLogLevel();
			
			System.out.println("Test appender");
			String logLevelToStore = getlogLevelsToStore(enableInternalLogs, internalLogLevel);
			System.out.println("logLevelMap : "+logLevelToStore);
			System.out.println("internal log : "+event.getLevel().toString());
			System.out.println("getLoggerName : "+event.getLoggerName().toString());
			
			if(logLevelToStore.contains(event.getLevel().toString()) || logLevelToStore.contains(event.getLevel().toString())) {
				if (event.getLoggerName().trim().toString().contains("com.example") || event.getLoggerName().trim()
						.toString().contains("org.springframework.cloud.sleuth.instrument.web.ExceptionLoggingFilter")) {
					LogSubscriber logSubscriber = BeanUtil.getBean(LogSubscriber.class);
					//System.out.println("Calling subscriber with msg "+event.getFormattedMessage());
					String stackTrace = "";
					if (event.getLoggerName().trim().toString()
							.contains("org.springframework.cloud.sleuth.instrument.web.ExceptionLoggingFilter") || event.getLoggerName().trim().toString()
							.contains("com.example")) {
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

					// Get the tracer util bean
					TracerUtil tracerUtil = BeanUtil.getBean(TracerUtil.class);
					
					Log log = new Log();
					Metadata metadata = new Metadata();
					metadata.setPayloadType("midaslog");
					metadata.setEventName("LogEvent");
					metadata.setCategory("System-Log");
					Payload payload = new Payload();
					if ((event.getLoggerName().trim().toString()
							.contains("org.springframework.cloud.sleuth.instrument.web.ExceptionLoggingFilter") || event.getLoggerName().trim().toString()
							.contains("com.example")) && !StringUtils.isEmpty(stackTrace)) {
						payload.setLog(stackTrace);
					} else {
						payload.setLog(event.getFormattedMessage());
					}
					payload.setTraceId(tracerUtil.getTraceId());
					payload.setServiceId("elastic demo");
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
		}
	}

	private String getlogLevelsToStore(boolean enableInternalLogs, String internalLogLevel) {
		System.out.println("In logLevels");
		String logLevelToReturn = "";
		if (enableInternalLogs) {
			if (internalLogLevel.equalsIgnoreCase("TRACE")) {
				logLevelToReturn = LogPatternConstants.LOG_LEVELS_FOR_TRACE;
			} else if (internalLogLevel.equalsIgnoreCase("DEBUG")) {
				logLevelToReturn = LogPatternConstants.LOG_LEVELS_FOR_DEBUG;
			} else if (internalLogLevel.equalsIgnoreCase("INFO")) {
				logLevelToReturn = LogPatternConstants.LOG_LEVELS_FOR_INFO;
			} else if (internalLogLevel.equalsIgnoreCase("WARN")) {
				logLevelToReturn = LogPatternConstants.LOG_LEVELS_FOR_WARN;
			} else if (internalLogLevel.equalsIgnoreCase("ERROR")) {
				logLevelToReturn = LogPatternConstants.LOG_LEVELS_FOR_ERROR;
			} else if (internalLogLevel.equalsIgnoreCase("FATAL")) {
				logLevelToReturn = LogPatternConstants.LOG_LEVELS_FOR_FATAL;
			}
		}else {
			logLevelToReturn = LogPatternConstants.DEFAULT_LOG_LEVELS;
		}
		return logLevelToReturn;
	}
	
	@Override
	protected void append(ILoggingEvent e) {
		this.test(e);
	}
}