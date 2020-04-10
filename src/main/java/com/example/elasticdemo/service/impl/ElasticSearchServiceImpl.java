package com.example.elasticdemo.service.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.elasticdemo.model.Log;
import com.example.elasticdemo.model.Metadata;
import com.example.elasticdemo.model.Payload;
import com.example.elasticdemo.service.ElasticSearchService;

@Service
public class ElasticSearchServiceImpl implements ElasticSearchService {

	private static final Logger LOG = LoggerFactory.getLogger(ElasticSearchServiceImpl.class);

	@Autowired
	private Client client;

	// @Autowired
	// private LogRepository logRepo;

	@Override
	public IndexResponse create(Log message) {
		LOG.debug("Elastic Sync Service message got : [{}]", message);

		Metadata metadata = message.getMetadata();
		Payload payload = message.getPayload();

//		try {
//			XContentBuilder builder = XContentFactory.jsonBuilder();
//			builder.startObject().field("generatedBy", payload.getGeneratedBy())
//					.field("identifier", payload.getIdentifier()).field("log", payload.getLog())
//					.field("logLevel", payload.getLogLevel()).field("traceId", payload.getTraceId());
//			if (null != payload.getGeneratedOn()) {
//				builder.field("generatedOn", payload.getGeneratedOn().toString());
//			} else {
//				payload.setGeneratedOn(Timestamp.valueOf(LocalDateTime.now()));
//				builder.field("generatedOn", payload.getGeneratedOn().toString());
//			}
//			builder.endObject();
		// String json = Strings.toString(builder);
		// LOG.info("Builder : " + json);
//			IndexResponse response = client.prepareIndex(metadata.getPayloadType(), metadata.getCategory())
//					.setSource(builder).get();
//			LOG.info("Elastic Sync Service response :- [{}]", response);
//			LOG.info("------------------------------");
//			LOG.info("Index name: " + response.getIndex());
//			LOG.info("Type name: " + response.getType());
//			LOG.info("ID: " + response.getId());
//			LOG.info("Version: " + response.getVersion());
//			LOG.info("------------------------------");
		try {
			throw new NullPointerException();
		} catch (Exception e) {
			LOG.error("Error msg: " + e);
		}
		// throw new NullPointerException();
		return null;
//		} catch (Exception e) {
//			System.out.println("Error masg: "+e);
//		}
	}

	@Override
	public List<Object> search(String logLevel, String traceId, String appName) {
		LOG.info("Searching elastic search with Log Level: [{}], Trace Id: [{}], App Name: [{}]", logLevel, traceId,
				appName);
		SearchResponse response = null;
		if (!StringUtils.isEmpty(logLevel) && !StringUtils.isEmpty(traceId) && !StringUtils.isEmpty(appName)) {
			response = client.prepareSearch("midaslog").setTypes(appName).setSearchType(SearchType.QUERY_THEN_FETCH)
					.setQuery(QueryBuilders.matchQuery("logLevel", logLevel))
					.setQuery(QueryBuilders.matchQuery("traceId", traceId)).get();
		} else if (!StringUtils.isEmpty(logLevel) && !StringUtils.isEmpty(appName)) {
			response = client.prepareSearch("midaslog").setTypes(appName).setSearchType(SearchType.QUERY_THEN_FETCH)
					.setQuery(QueryBuilders.matchQuery("logLevel", logLevel)).get();
		} else if (!StringUtils.isEmpty(logLevel) && !StringUtils.isEmpty(traceId)) {
			response = client.prepareSearch("midaslog").setSearchType(SearchType.QUERY_THEN_FETCH)
					.setQuery(QueryBuilders.matchQuery("traceId", traceId))
					.setQuery(QueryBuilders.matchQuery("logLevel", logLevel)).get();
		} else if (!StringUtils.isEmpty(traceId) && !StringUtils.isEmpty(appName)) {
			response = client.prepareSearch("midaslog").setTypes(appName).setSearchType(SearchType.QUERY_THEN_FETCH)
					.setQuery(QueryBuilders.matchQuery("traceId", traceId)).get();
		} else if (!StringUtils.isEmpty(traceId)) {
			response = client.prepareSearch("midaslog").setSearchType(SearchType.QUERY_THEN_FETCH)
					.setQuery(QueryBuilders.matchQuery("traceId", traceId)).get();
		} else if (!StringUtils.isEmpty(logLevel)) {
			response = client.prepareSearch("midaslog").setSearchType(SearchType.QUERY_THEN_FETCH)
					.setQuery(QueryBuilders.matchQuery("logLevel", logLevel)).get();
		} else if (!StringUtils.isEmpty(appName)) {
			response = client.prepareSearch("midaslog").setTypes(appName).setSearchType(SearchType.QUERY_THEN_FETCH)
					.get();
		}

		Map<String, Object> map = new LinkedHashMap<String, Object>();
		List<SearchHit> searchHits = Arrays.asList(response.getHits().getHits());
		List<Object> sourceObjectList = new ArrayList<>();
		for (int i = 0; i < searchHits.size(); i++) {
			sourceObjectList.add(searchHits.get(i).getSourceAsMap());
		}
		return sourceObjectList;
	}
}
