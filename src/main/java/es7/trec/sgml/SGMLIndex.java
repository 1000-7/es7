package es7.trec.sgml;

import es7.HighRestClient;
import es7.trec.bean.SGMLDocument;
import lombok.extern.java.Log;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.util.List;

/**
 * @author unclewang
 * @date 2020/3/4 13:58
 */
@Log
public class SGMLIndex {
    public static final String INDEX_NAME = "sgml";
    private static RestHighLevelClient client = HighRestClient.getInstance().getClient();

    /**
     * 封装的创建索引的方法
     * @param dir 文件夹路径
     * @param indexName 索引名称
     */
    public static void index(String dir, String indexName) {
        log.info("开始解析文件");
        List<SGMLDocument> documents = SGMLReader.readFromDictionary(dir);
        log.info("文件解析完成，开始创建索引，本次共有文件个数：" + documents.size());
        //使用bulk操作批量的创建索引
        BulkRequest bulkRequest = new BulkRequest();
        long id = 0;
        for (SGMLDocument sd : documents) {
            //将每一个文档封装成创建索引的请求IndexRequest
            bulkRequest.add(new IndexRequest(indexName).source("docNo", sd.getDocNum(), "docId", sd.getDocId(), "text", sd.getText()));
            id++;
            //100个请求批处理一次
            if (id % 100 == 0) {
                try {
                    BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
                    bulkRequest = new BulkRequest();
                    log.info("索引创建成功\t" + "id:" + id + "\t" + bulkResponse.getTook());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        client.bulkAsync(bulkRequest, RequestOptions.DEFAULT, new ActionListener<BulkResponse>() {
            @Override
            public void onResponse(BulkResponse bulkResponse) {
                log.info("剩余的文档索引创建成功，" + bulkResponse.getTook());
            }

            @Override
            public void onFailure(Exception e) {
                log.warning("索引创建失败");
                e.printStackTrace();
            }
        });
    }

    public static void main(String[] args) {
        index("/Users/unclewang/Downloads/cqk/tipster/wsj/1991/", INDEX_NAME);
    }
}
