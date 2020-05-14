package com.example.elasticdemo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConfigHolder {

	@Value("${logging.internal.log.level}")
	private String internalLogLevel;

	@Value("${logging.enable.internal.log}")
	private boolean enableInternalLogs;

	@Value("${elastic.logging.enable}")
	private boolean enableElasticLogs;
	
	public String getInternalLogLevel() {
		return internalLogLevel;
	}

	public boolean isEnableInternalLogs() {
		return enableInternalLogs;
	}
	
	public boolean isEnableElasticLogs() {
		return enableElasticLogs;
	}
}
