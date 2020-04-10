package com.example.elasticdemo.repository;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.example.elasticdemo.model.Payload;

//@Repository
public interface LogRepository {

	public List<Payload> findByPayloadlogLevel(String logLevel);
	
}
