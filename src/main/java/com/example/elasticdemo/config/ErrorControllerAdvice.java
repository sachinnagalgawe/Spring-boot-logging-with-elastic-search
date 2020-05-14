package com.example.elasticdemo.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.example.elasticdemo.model.ResponseLogDetails;
import com.example.elasticdemo.util.TracerUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ControllerAdvice
public class ErrorControllerAdvice implements ResponseBodyAdvice<Object> {

	@Autowired
	private ConfigHolder configHolder;

	@Autowired
	private TracerUtil tracer;

	@Autowired
	private LogSubscriber logSubscriber;
	
	// Object Mapper
	private ObjectMapper mapper = new ObjectMapper();

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}
	
	/**
	 * Method to intercept response and log Error messages
	 */
	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
			ServerHttpResponse response) {
		if(configHolder.isEnableElasticLogs()) {
			ResponseLogDetails responseLogDetails = new ResponseLogDetails();
			System.out.println("In beforeBodyWrite");
			int status = ((ServletServerHttpResponse) response).getServletResponse().getStatus();
			String characterEncoding = ((ServletServerHttpRequest) request).getServletRequest().getCharacterEncoding();
			if (status >= 400 && (!body.toString().contains("error=null") && !body.toString().contains("message=null"))) {
				String traceId = tracer.getTraceId();
				String responseBody = "";
				if(body instanceof Map) {
					Map<String, Object> responseToReturn  = (Map<String, Object>) body;
					responseToReturn.put("traceId", traceId);
					responseBody = this.getResponseBodyString(responseToReturn);
					responseLogDetails.setBody(responseBody);
				}else {
					responseBody = this.getResponseBodyString(body);
					responseLogDetails.setBody(responseBody);
				}
				ServletServerHttpRequest servletServerRequest = (ServletServerHttpRequest) request;
				servletServerRequest.getServletRequest().setAttribute("errorResponseDetails", responseLogDetails);
			}
		}
		return body;
	}
	
	private String getResponseBodyString(Object body) {
		String responseBodyJson = "";
		try {
			responseBodyJson = mapper.writeValueAsString(body);
		} catch (JsonProcessingException e) {
			System.out.println("Unable to convert object to json string"+e);
		}
		return responseBodyJson;
	}
}
