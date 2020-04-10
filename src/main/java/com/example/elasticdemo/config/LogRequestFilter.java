package com.example.elasticdemo.config;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.example.elasticdemo.model.Log;
import com.example.elasticdemo.model.Metadata;
import com.example.elasticdemo.model.Payload;
import com.example.elasticdemo.util.TracerUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class LogRequestFilter extends OncePerRequestFilter {

	// Logger
	private static final Logger logger = LoggerFactory.getLogger(LogRequestFilter.class);

	private boolean includeResponsePayload = true;
	private int maxPayloadLength = 10000;

	@Autowired
	private TracerUtil tracer;

	@Autowired
	private LogSubscriber logSubscriber;

	private ObjectMapper mapper = new ObjectMapper();

	private String getContentAsString(byte[] buf, int maxLength, String charsetName) {
		if (buf == null || buf.length == 0)
			return "";
		int length = Math.min(buf.length, this.maxPayloadLength);
		try {
			return new String(buf, 0, length, charsetName);
		} catch (UnsupportedEncodingException ex) {
			return "Unsupported Encoding";
		}
	}

	private void createLog(String traceId, String logTxt, String logLevel, String identifire) {
		System.out.println("Create log..");
		String generatedBy = this.getClass().getName().toString() + "/doFilterInternal";
		Log log = new Log();
		Metadata metadata = new Metadata();
		metadata.setPayloadType("midaslog");
		metadata.setEventName("LogEvent");// what is event name
		metadata.setCategory("OTC-PaymentMethodsService");
		// elastic.logger.payload.type:midaslog
		Payload payload = new Payload();
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

	/**
	 * Log each request and respponse with full Request URI, content payload and
	 * duration of the request in ms.
	 * 
	 * @param request     the request
	 * @param response    the response
	 * @param filterChain chain of filters
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		System.out.println("Request received");
		long startTime = System.currentTimeMillis();
		String reqId = tracer.getTraceId();
		StringBuffer reqInfo = new StringBuffer().append("Trace Id: [").append(reqId).append("] ")
				.append("Method:  [" + request.getMethod() + "] ").append("Url: [")
				.append(request.getRequestURL() + "] ");

		String queryString = request.getQueryString();
		if (queryString != null) {
			reqInfo.append("Query Params: [?").append(queryString + "] ");
		}

		if (request.getRequestURI().contains("/elastic/search")) {
			filterChain.doFilter(request, response);
			return;
		}

		Map<String, String> map = new HashMap<String, String>();
		Enumeration headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String key = (String) headerNames.nextElement();
			String value = request.getHeader(key);
			map.put(key, value);
		}

		if (!CollectionUtils.isEmpty(map)) {
			String requestHeadersJson = mapper.writeValueAsString(map);
			reqInfo.append("Headers: [" + requestHeadersJson + "] ");
		}

		ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
		ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

		filterChain.doFilter(wrappedRequest, wrappedResponse);
		long duration = System.currentTimeMillis() - startTime;

		String requestBody = this.getContentAsString(wrappedRequest.getContentAsByteArray(), this.maxPayloadLength,
				request.getCharacterEncoding());
		if (requestBody.length() > 0) {
		}
		String reqDetails = "Request Info: " + reqInfo + "Request Body:" + requestBody;
		this.createLog(reqId, reqDetails, "SUCCESS", "REQUEST");
		if (includeResponsePayload) {
			byte[] buf = wrappedResponse.getContentAsByteArray();
			String responseBody = getContentAsString(buf, this.maxPayloadLength, response.getCharacterEncoding());
			String responseDetails = "Response body: [" + responseBody + "] " + "returned status: ["
					+ response.getStatus() + "] in [" + duration + "] ms ";
			this.createLog(reqId, responseDetails, "SUCCESS", "RESPONSE");
		}
		wrappedResponse.copyBodyToResponse(); // IMPORTANT: copy content of response back into original response

	}
}
