package com.example.elasticdemo.config;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

import com.example.elasticdemo.model.Log;
import com.example.elasticdemo.model.Metadata;
import com.example.elasticdemo.model.Payload;
import com.example.elasticdemo.model.RequestLogDetails;
import com.example.elasticdemo.model.ResponseLogDetails;
import com.example.elasticdemo.util.TracerUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import feign.Logger;
import feign.Request;
import feign.Response;
import feign.Util;

@Configuration
public class FeignClientInterceptor extends Logger {

	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(FeignClientInterceptor.class);

	@Autowired
	private TracerUtil tracer;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private LogSubscriber logSubscriber;

	@Override
	protected void logRequest(String configKey, Level logLevel, Request request) {
		System.out.println("Log Request");

		// Get trace Id
		String traceId = tracer.getTraceId();

		// Set request details
		RequestLogDetails requestDetails = new RequestLogDetails();
		requestDetails.setTraceId(traceId);
		requestDetails.setMethod(request.method());
		requestDetails.setUrl(request.url());
		if (request.url() != null && request.url().contains("?")) {
			List<String> queryParamsList = Arrays.asList(request.url().split("\\?"));
			String queryParams = queryParamsList.get(1);
			requestDetails.setQueryString(queryParams);
		}
		requestDetails.setHeaders(request.headers());
		byte[] bytes = request.body();

		try {
			if (bytes != null) {
				String body = new String(bytes, "UTF-8");
				requestDetails.setBody(body);
			}
			String requestLogDetailsString = mapper.writeValueAsString(requestDetails);
			System.out.println("requestJson: " + requestLogDetailsString);
			this.createLog(traceId, requestLogDetailsString, "INFO", "FEIGN_CLIENT_REQUEST", "logRequest");
		} catch (UnsupportedEncodingException e) {
			logger.error("Unable to convert the response to json string: ", e);
		} catch (JsonProcessingException e) {
			logger.error("Unable to convert the response to json string: ", e);
		}
	}

	@Override
	protected Response logAndRebufferResponse(String configKey, Level logLevel, Response response, long elapsedTime)
			throws IOException {
		System.out.println("Log Response");
		// Get trace Id
		String traceId = tracer.getTraceId();
		ResponseLogDetails responseLogDetails = new ResponseLogDetails();
		responseLogDetails.setTraceId(traceId);

		// Set response headers
		HttpHeaders headers = new HttpHeaders();
		Map<String, Collection<String>> headerMap = response.headers();
		Map<String, List<String>> listsaps = new HashMap<>();
		headerMap.forEach((k, v) -> listsaps.put(k, new ArrayList<>(v)));
		headers.putAll(listsaps);
		responseLogDetails.setHeaders(headers);

		// Set response s
		responseLogDetails.setStatus(response.status());

		// Set response time
		responseLogDetails.setTimeInMilliseconds(elapsedTime);

		if (response.body() != null) {
			byte[] bytes = Util.toByteArray(response.body().asInputStream());
			if (bytes != null) {
				try {
					String body = new String(bytes, "UTF-8");
					responseLogDetails.setBody(body);

					String responseLogDetailsString = mapper.writeValueAsString(responseLogDetails);
					System.out.println("responseLogDetailsJson: " + responseLogDetailsString);
					this.createLog(traceId, responseLogDetailsString, "INFO", "FEIGN_CLIENT_RESPONSE",
							"logAndRebufferResponse");

					return response.toBuilder().body(bytes).build();
				} catch (UnsupportedEncodingException e) {
					logger.error("Unable to convert the response to json string: ", e);
				} catch (JsonProcessingException e) {
					logger.error("Unable to convert the response to json string: ", e);
				}
			}
		}
		return response;
	}

	/**
	 * Create a log
	 *
	 * @param traceId
	 * @param logTxt
	 * @param logLevel
	 * @param identifire
	 */
	private void createLog(String traceId, String logTxt, String logLevel, String identifire, String method) {
		String generatedBy = this.getClass().getName().toString() + "/" + method;
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

	@Override
	protected void log(String configKey, String format, Object... args) {
	
	}
}
