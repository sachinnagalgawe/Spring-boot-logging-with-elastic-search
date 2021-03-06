package com.example.elasticdemo.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.util.StopWatch;

import com.example.elasticdemo.model.Log;
import com.example.elasticdemo.model.Metadata;
import com.example.elasticdemo.model.Payload;
import com.example.elasticdemo.model.RequestLogDetails;
import com.example.elasticdemo.model.ResponseLogDetails;
import com.example.elasticdemo.util.TracerUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class HttpInterceptor implements ClientHttpRequestInterceptor {
	
    private static final Logger logger = LoggerFactory.getLogger(HttpInterceptor.class);
    
    @Autowired
    private ObjectMapper mapper;
    
    @Autowired
    private TracerUtil tracer;
    
	@Autowired
	private LogSubscriber logSubscriber;

	//@Async
	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		// Trace Request
		traceRequest(request, body);
        
		// Create a stop watch
		StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        
        // Execute the request
        ClientHttpResponse response = execution.execute(request, body);
        
        // Check response time
        stopwatch.stop();
        long timeInMiliseconds = stopwatch.getLastTaskTimeMillis();
		
        // Trace response
        traceResponse(response, timeInMiliseconds);
		return response;
	}

	private void traceRequest(HttpRequest request, byte[] body) throws IOException {
		// Get trace Id
		String traceId = tracer.getTraceId();
		// Get request body
		String requestBody = null;
		if (body.length > 0) {
			requestBody = new String(body, StandardCharsets.UTF_8);
		}
		// Set request details
		RequestLogDetails requestLogDetails = new RequestLogDetails(traceId, request.getMethod().toString(),
				request.getURI().toString(), request.getURI().getRawQuery(), request.getHeaders(), requestBody);
		
		// Convert request details to JSON string
		String requestLogDetailsString = mapper.writeValueAsString(requestLogDetails);
		this.createLog(traceId, requestLogDetailsString, "INFO", "REST_TEMPLATE_REQUEST", "traceRequest");
	}

	private void traceResponse(ClientHttpResponse response, long responseTime) throws IOException {
		// Get trace Id
		String traceId = tracer.getTraceId();
		// Get response body
		InputStream bodyData = response.getBody();
		String body = (StreamUtils.copyToString(bodyData, Charset.defaultCharset()));
		
		// Set response data
		ResponseLogDetails responseLogDetails = new ResponseLogDetails();
		responseLogDetails.setTraceId(traceId);
		responseLogDetails.setBody(body);
		responseLogDetails.setStatus(response.getStatusCode().value());
		responseLogDetails.setHeaders(response.getHeaders());
		responseLogDetails.setTimeInMilliseconds(responseTime);
		
		// Convert responseLogDetails to JSON string
		String responseLogDetailsString = mapper.writeValueAsString(responseLogDetails);
		this.createLog(traceId, responseLogDetailsString, "INFO", "REST_TEMPLATE_RESPONSE", "traceResponse");
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
}