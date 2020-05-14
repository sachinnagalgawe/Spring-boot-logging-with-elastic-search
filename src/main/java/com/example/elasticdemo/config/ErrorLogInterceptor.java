package com.example.elasticdemo.config;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

//@Component
//@Order(value = 1)
public class ErrorLogInterceptor extends HandlerInterceptorAdapter {

	private static final Logger logger = LoggerFactory.getLogger(ErrorLogInterceptor.class);

	private ObjectMapper mapper = new ObjectMapper();

	private int maxPayloadLength = 10000;

	/**
	 * This method is used to log error response patterns for ReST API calls coming
	 * to Payment Methods Service.
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception arg3)
			throws Exception {
		if (response.getStatus() >= 400) {
			System.out.println("Error Headers: " + getResponseHeaders(response));
			String responseBody = "";
            ContentCachingResponseWrapper wrapper = (ContentCachingResponseWrapper) response;
            wrapper.copyBodyToResponse();
            String responseBody2 = mapper.writeValueAsString(wrapper);
			System.out.println("responseBody2 " + responseBody2);
            if (wrapper != null) {
                byte[] buf = wrapper.getContentAsByteArray();
                if (buf.length > 0) {
                	responseBody = new String(buf, 0, buf.length, wrapper.getCharacterEncoding());
                }
                wrapper.copyBodyToResponse();   // in other case you send null to the response
            }
			System.out.println("responseBody: " + responseBody);
		}
	}

	/**
	 * Get response header json string
	 * 
	 * @param response
	 * @return
	 */
	private String getResponseHeaders(HttpServletResponse response) {
		String responseHeadersJson = "";
		try {
			HttpHeaders responseHeaders = new HttpHeaders();
			for (String headerName : response.getHeaderNames()) {
				responseHeaders.add(headerName, response.getHeader(headerName));
			}
			responseHeadersJson = mapper.writeValueAsString(responseHeaders);
		} catch (JsonProcessingException e) {
			System.out.println("Unable to convert object to json string" + e);
		}
		return responseHeadersJson;
	}

	/**
	 * Get content into string
	 * 
	 * @param buf
	 * @param maxLength
	 * @param charsetName
	 * @return
	 */
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
}
