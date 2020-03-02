package es7;

import lombok.SneakyThrows;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
/**
 *
 * @author unclewang
 * @date 2020/3/2 21:42
 */
public class HighRestClient {
    private static RestHighLevelClient client;

    private static class SingletonHolder {
        private static final HighRestClient INSTANCE = new HighRestClient();
    }

    public static final HighRestClient getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private HighRestClient() {
        init();
    }

    private void init() {
        client = new RestHighLevelClient(
                                RestClient.builder(
                                        new HttpHost("localhost", 9200, "http")));
    }

    public RestHighLevelClient getClient() {
        return client;
    }

    @SneakyThrows
    public void closeClient() {
        client.close();
    }
}