package com.example.elasticdemo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
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
import com.example.elasticdemo.util.SearchHitIterator;

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
//			//System.out.println("Error masg: "+e);
//		}
	}

	@Override
	public List<Object> search(String logLevel, String traceId, String appName) {
		LOG.info("Searching elastic search with Log Level: [{}], Trace Id: [{}], App Name: [{}]", logLevel, traceId,
				appName);
		SearchRequestBuilder requestBuilder = null;
		if (!StringUtils.isEmpty(logLevel) && !StringUtils.isEmpty(traceId) && !StringUtils.isEmpty(appName)) {
			// System.out.println("All three..");

			BoolQueryBuilder boolQuery = new BoolQueryBuilder();
			boolQuery.must(QueryBuilders.matchQuery("logLevel", logLevel));
			boolQuery.must(QueryBuilders.matchQuery("traceId", traceId));

			requestBuilder = client.prepareSearch("midaslog").setTypes(appName).setQuery(boolQuery);

		} else if (!StringUtils.isEmpty(logLevel) && !StringUtils.isEmpty(appName)) {
			// System.out.println("Two logLevel and appName");

			QueryBuilder qb = QueryBuilders.matchQuery("logLevel", logLevel);
			requestBuilder = client.prepareSearch("midaslog").setTypes(appName).setQuery(qb);

		} else if (!StringUtils.isEmpty(logLevel) && !StringUtils.isEmpty(traceId)) {
			// System.out.println("Two logLevel and traceId");

			BoolQueryBuilder boolQuery = new BoolQueryBuilder();
			boolQuery.must(QueryBuilders.matchQuery("logLevel", logLevel));
			boolQuery.must(QueryBuilders.matchQuery("traceId", traceId));

			// QueryBuilder qb = QueryBuilders.multiMatchQuery("logLevel", logLevel,
			// "traceId", traceId);
			requestBuilder = client.prepareSearch("midaslog").setQuery(boolQuery);

		} else if (!StringUtils.isEmpty(traceId) && !StringUtils.isEmpty(appName)) {
			// System.out.println("Two appName and traceId");
			QueryBuilder qb = QueryBuilders.matchQuery("traceId", traceId);
			requestBuilder = client.prepareSearch("midaslog").setTypes(appName).setQuery(qb);

		} else if (!StringUtils.isEmpty(traceId)) {
			// System.out.println("One traceId");
			QueryBuilder qb = QueryBuilders.matchQuery("traceId", traceId);
			requestBuilder = client.prepareSearch("midaslog").setQuery(qb);

		} else if (!StringUtils.isEmpty(logLevel)) {
			// System.out.println("One logLevel");
			QueryBuilder qb = QueryBuilders.matchQuery("logLevel", logLevel);
			requestBuilder = client.prepareSearch("midaslog").setQuery(qb);
		} else if (!StringUtils.isEmpty(appName)) {
			// System.out.println("One appName");
			requestBuilder = client.prepareSearch("midaslog").setTypes(appName);
		}

		SearchHitIterator hitIterator = new SearchHitIterator(requestBuilder);
		List<Object> list = new ArrayList<>();
		while (hitIterator.hasNext()) {
			SearchHit hit = hitIterator.next();
			list.add(hit.getSourceAsMap());
		}
		return list;
	}
}
