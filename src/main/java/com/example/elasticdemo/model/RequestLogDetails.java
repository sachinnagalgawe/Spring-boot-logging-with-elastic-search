package com.example.elasticdemo.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonRawValue;

/**
 * Request Details class to store request details logs
 */
public class RequestLogDetails {

	private String traceId;

	private String method;

	private String url;
	
	private String queryString;
	
	private Map headers;

    @JsonRawValue
	private String body;

	public RequestLogDetails() {
		super();
	}

	public RequestLogDetails(String traceId, String method, String url, String queryString, Map headers, String body) {
		super();
		this.traceId = traceId;
		this.method = method;
		this.url = url;
		this.queryString = queryString;
		this.headers = headers;
		this.body = body;
	}

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Map getHeaders() {
		return headers;
	}

	public void setHeaders(Map headers) {
		this.headers = headers;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return "RequestLogDetails [traceId=" + traceId + ", method=" + method + ", url=" + url + ", queryString="
				+ queryString + ", headers=" + headers + ", body=" + body + "]";
	}
}
