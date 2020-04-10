package com.example.elasticdemo.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.elasticdemo.model.Log;
import com.example.elasticdemo.service.ElasticSearchService;

@RestController()
@RequestMapping("/elastic")
public class ElasticSearchController {

	private static final Logger LOG = LoggerFactory.getLogger(ElasticSearchController.class);

	@Autowired
	private ElasticSearchService searchService;

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Object createIndex(@RequestBody Log message) {
		LOG.info("Search elastic create index {} ", message);
		return searchService.create(message);
	}

	@PostMapping(value = "/test", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Object createTestIndex(@RequestBody Log message) {
		LOG.info("Search elastic create index {} ", message);
		return searchService.create(message);
	}

	@GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
	public Object paymenySwitchCallbackAPI(@RequestParam(value = "logLevel", required = false) String logLevel,
			@RequestParam(value = "traceId", required = false) String traceId,
			@RequestParam(value = "appName", required = false) String appName) {
		LOG.info("Search elastic search with Log Level: [{}], Trace Id: [{}], App Name: [{}]", logLevel, traceId, appName);
		List<Object> response = searchService.search(logLevel, traceId, appName);
		return response;
	}
}
