package com.example.elasticdemo.service;

import java.util.List;

import org.elasticsearch.action.index.IndexResponse;

import com.example.elasticdemo.model.Log;

public interface ElasticSearchService {

	public IndexResponse create(Log message);

	public List<Object> search(String logLevel, String traceId, String appName);
}
