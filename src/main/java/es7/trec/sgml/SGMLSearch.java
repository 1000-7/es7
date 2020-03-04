package es7.trec.sgml;

import es7.HighRestClient;
import es7.trec.bean.QueryResult;
import es7.trec.bean.QueryWords;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

/**
 * @author unclewang
 * @date 2020/3/3 18:27
 * 主要看QueryBuilders各种实现方法
 */
public class SGMLSearch {
    private static RestHighLevelClient client = HighRestClient.getInstance().getClient();

    public static List<QueryResult> search(QueryWords query, String indexName) {
        List<QueryResult> queryResults = new ArrayList<>();
        SearchRequest searchRequest = new SearchRequest(indexName)
                .source(new SearchSourceBuilder()
                        .query(matchQuery("text", query.getQuery()))
                        .minScore(10)
                        .size(1000))
                .scroll(TimeValue.timeValueMinutes(1L));
        try {
            SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHit[] hits = search.getHits().getHits();
            for (int i = 0; i < hits.length; i++) {
                QueryResult result = new QueryResult();
                result.setQueryId(query.getQueryId());
                result.setQ0("q0");
                result.setDocId(((String) hits[i].getSourceAsMap().get("docNo")).trim());
                result.setRank(i + 1);
                result.setScore(hits[i].getScore());
                result.setRunId(0);
                queryResults.add(result);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return queryResults;
    }

    public static void main(String[] args) {
        search(new QueryWords("Coping with overcrowded prisons", "151"), "sgml");
    }
}
