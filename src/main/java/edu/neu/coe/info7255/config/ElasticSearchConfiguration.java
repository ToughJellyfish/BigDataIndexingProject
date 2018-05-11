package edu.neu.coe.info7255.config;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;


import java.util.Collections;
import java.util.Map;

@Configuration
public class ElasticSearchConfiguration {
    private org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RestHighLevelClient ecClient;

    @Bean
    public RestHighLevelClient ecClient() {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")));
        return client;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initForIndexJoin() {
        try {
            String jsonString = "{\n" +
                    "  \"mappings\": {\n" +
                    "    \"_doc\": {\n" +
                    "      \"properties\": {\n" +
                    "        \"join_field\": { \n" +
                    "          \"type\": \"join\",\n" +
                    "          \"relations\": {\n" +
                    "            \"plan\": [\"membercostshare\"],\n" +
                    "            \"planservice\": \"service\"\n" +
                    "          }\n" +
                    "        },\n" +
                    "        \"creationDate\": {\n" +
                    "          \"type\":   \"date\",\n" +
                    "          \"format\": \"MM-dd-yyyy\"\n" +
                    "        }\n" +
                    "      }\n" +
                    "    }\n" +
                    "  }\n" +
                    "}";
            Map<String, String> params = Collections.singletonMap("pretty", "true");
            HttpEntity entity = new NStringEntity(jsonString, ContentType.APPLICATION_JSON);
            ecClient.getLowLevelClient().performRequest("PUT", "index_join", params, entity);
            logger.info("Init index_join field success.");
        } catch (Exception e) {
            logger.error("Init index_join field failed(acceptable if field already exist), error:"+e);
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initForIndexAll() {
        try {
            String jsonString = "{\n" +
                    "  \"mappings\": {\n" +
                    "    \"plan\": {\n" +
                    "      \"properties\": {\n" +
                    "        \"creationDate\": {\n" +
                    "          \"type\":   \"date\",\n" +
                    "          \"format\": \"MM-dd-yyyy\"\n" +
                    "        }\n" +
                    "      }\n" +
                    "    }\n" +
                    "  }\n" +
                    "}";
            Map<String, String> params = Collections.singletonMap("pretty", "true");
            HttpEntity entity = new NStringEntity(jsonString, ContentType.APPLICATION_JSON);
            ecClient.getLowLevelClient().performRequest("PUT", "index_all", params, entity);
            logger.info("Init index_all field success.");
        } catch (Exception e) {
            logger.error("Init index_all date field failed(acceptable if field already exist), error:"+e);
        }
    }

}
