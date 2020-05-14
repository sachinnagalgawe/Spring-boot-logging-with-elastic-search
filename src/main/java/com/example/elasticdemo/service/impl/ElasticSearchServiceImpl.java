package com.example.elasticdemo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
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
	
	public static final String ELASTIC_SEARCH_INDEX_NAME = "midaslog";
	
	public static final String ELASTIC_SEARCH_DOC_TYPE = "System-Log";
	
	public static final String ELASTIC_SEARCH_TRACE_ID = "traceId";

	public static final String ELASTIC_SEARCH_SERVICE_ID = "serviceId";

	public static final String ELASTIC_SEARCH_LOG_LEVEL = "logLevel";
	
	public static final String ELASTIC_SEARCH_SORT_KEYWORD = "generatedOn.keyword";

	// @Autowired
	// private LogRepository logRepo;

	@Override
	public IndexResponse create(String message) {
		LOG.debug("Elastic Sync Service message got : [{}]", message);
		throw new NullPointerException();
	}
	
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
//		try {
//			throw new NullPointerException();
//		} catch (Exception e) {
//			LOG.error("Error msg: " + e);
//		}
		throw new NullPointerException();
		//return null;
//		} catch (Exception e) {
//			//System.out.println("Error masg: "+e);
//		}
	}

	@Override
	public List<Object> search(String logLevel, String traceId, String appName, int page, int count) {
		LOG.info("Searching elastic search with Log Level: [{}], Trace Id: [{}], App Name: [{}], Page: [{}], Count: [{}]", logLevel, traceId, appName, page, count);
		SearchRequestBuilder requestBuilder = null;
		if (!StringUtils.isEmpty(logLevel) && !StringUtils.isEmpty(traceId) && !StringUtils.isEmpty(appName)) {
			BoolQueryBuilder boolQuery = new BoolQueryBuilder();
			boolQuery.must(QueryBuilders.matchQuery(ELASTIC_SEARCH_LOG_LEVEL, logLevel));
			boolQuery.must(QueryBuilders.matchQuery(ELASTIC_SEARCH_TRACE_ID, traceId));
			boolQuery.must(QueryBuilders.matchQuery(ELASTIC_SEARCH_SERVICE_ID, appName));
			requestBuilder = client.prepareSearch(ELASTIC_SEARCH_INDEX_NAME)
							.setTypes(ELASTIC_SEARCH_DOC_TYPE).setQuery(boolQuery);
		} else if (!StringUtils.isEmpty(logLevel) && !StringUtils.isEmpty(appName)) {
			BoolQueryBuilder boolQuery = new BoolQueryBuilder();
			boolQuery.must(QueryBuilders.matchQuery(ELASTIC_SEARCH_LOG_LEVEL, logLevel));
			boolQuery.must(QueryBuilders.matchQuery(ELASTIC_SEARCH_SERVICE_ID, appName));
			requestBuilder = client.prepareSearch(ELASTIC_SEARCH_INDEX_NAME)
							.setTypes(ELASTIC_SEARCH_DOC_TYPE).setQuery(boolQuery);
		} else if (!StringUtils.isEmpty(logLevel) && !StringUtils.isEmpty(traceId)) {
			BoolQueryBuilder boolQuery = new BoolQueryBuilder();
			boolQuery.must(QueryBuilders.matchQuery(ELASTIC_SEARCH_LOG_LEVEL, logLevel));
			boolQuery.must(QueryBuilders.matchQuery(ELASTIC_SEARCH_TRACE_ID, traceId));
			requestBuilder = client.prepareSearch(ELASTIC_SEARCH_INDEX_NAME)
							.setTypes(ELASTIC_SEARCH_DOC_TYPE).setQuery(boolQuery);
		} else if (!StringUtils.isEmpty(traceId) && !StringUtils.isEmpty(appName)) {
			BoolQueryBuilder boolQuery = new BoolQueryBuilder();
			boolQuery.must(QueryBuilders.matchQuery(ELASTIC_SEARCH_TRACE_ID, traceId));
			boolQuery.must(QueryBuilders.matchQuery(ELASTIC_SEARCH_SERVICE_ID, appName));
			requestBuilder = client.prepareSearch(ELASTIC_SEARCH_INDEX_NAME)
							.setTypes(ELASTIC_SEARCH_DOC_TYPE).setQuery(boolQuery);
		} else if (!StringUtils.isEmpty(traceId)) {
			BoolQueryBuilder boolQuery = new BoolQueryBuilder();
			boolQuery.must(QueryBuilders.matchQuery(ELASTIC_SEARCH_TRACE_ID, traceId));
			requestBuilder = client.prepareSearch(ELASTIC_SEARCH_INDEX_NAME)
							.setTypes(ELASTIC_SEARCH_DOC_TYPE).setQuery(boolQuery);
		} else if (!StringUtils.isEmpty(logLevel)) {
			BoolQueryBuilder boolQuery = new BoolQueryBuilder();
			boolQuery.must(QueryBuilders.matchQuery(ELASTIC_SEARCH_LOG_LEVEL, logLevel));
			requestBuilder = client.prepareSearch(ELASTIC_SEARCH_INDEX_NAME)
							.setTypes(ELASTIC_SEARCH_DOC_TYPE).setQuery(boolQuery);
		} else if (!StringUtils.isEmpty(appName)) {
			BoolQueryBuilder boolQuery = new BoolQueryBuilder();
			boolQuery.must(QueryBuilders.matchQuery(ELASTIC_SEARCH_SERVICE_ID, appName));
			requestBuilder = client.prepareSearch(ELASTIC_SEARCH_INDEX_NAME)
							.setTypes(ELASTIC_SEARCH_DOC_TYPE).setQuery(boolQuery);
		}
		
		if(requestBuilder != null) {
			requestBuilder.setFrom(page).setSize(count);
		}
		SearchResponse response = requestBuilder.addSort(SortBuilders.fieldSort(ELASTIC_SEARCH_SORT_KEYWORD)
									.order(SortOrder.ASC)).execute().actionGet();
		List<Object> list = new ArrayList<>();
		if (response != null && response.getHits() != null && response.getHits().getHits() != null) {
			SearchHit[] results = response.getHits().getHits();
			if (results != null && results.length != 0) {
				for (SearchHit hit : results) {
					list.add(hit.getSourceAsMap());
				}
			}	
		}
		return list;
	}
}
