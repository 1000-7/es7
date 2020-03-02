package es7;


import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

public class Search {
    public static void main(String[] args) {
        RestHighLevelClient client = HighRestClient.getInstance().getClient();
        SearchRequest searchRequest = new SearchRequest("posts")
                .source(new SearchSourceBuilder()
                        .query(matchQuery("message", "Elasticsearch"))
                        .size(1000))
                .scroll(TimeValue.timeValueMinutes(1L));

        try {
            SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
            System.out.println(search);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
