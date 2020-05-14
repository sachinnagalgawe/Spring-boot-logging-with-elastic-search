package com.example.elasticdemo.model;

import org.springframework.http.HttpHeaders;

import com.fasterxml.jackson.annotation.JsonRawValue;

/**
 * Response Details class to store request details logs
 */
public class ResponseLogDetails {

	private String traceId;

	private HttpHeaders headers;

	@JsonRawValue
	private String body;

	private int status;

	private long timeInMilliseconds;

	public ResponseLogDetails() {
		super();
	}

	public ResponseLogDetails(String traceId, HttpHeaders headers, String body, int status, long timeInMilliseconds) {
		super();
		this.traceId = traceId;
		this.headers = headers;
		this.body = body;
		this.status = status;
		this.timeInMilliseconds = timeInMilliseconds;
	}

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}

	public HttpHeaders getHeaders() {
		return headers;
	}

	public void setHeaders(HttpHeaders headers) {
		this.headers = headers;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getTimeInMilliseconds() {
		return timeInMilliseconds;
	}

	public void setTimeInMilliseconds(long timeInMilliseconds) {
		this.timeInMilliseconds = timeInMilliseconds;
	}

	@Override
	public String toString() {
		return "ResponseLogDetails [traceId=" + traceId + ", headers=" + headers + ", body=" + body + ", status="
				+ status + ", timeInMilliseconds=" + timeInMilliseconds + "]";
	}
}
