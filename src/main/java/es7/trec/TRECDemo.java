package es7.trec;


import es7.trec.bean.QueryResult;
import es7.trec.bean.QueryWords;
import es7.trec.sgml.SGMLSearch;
import lombok.extern.java.Log;
import org.apache.commons.io.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 一次完整的TREC实验
 *
 * @author unclewang
 * @date 2020/3/4 13:52
 */
@Log
public class TRECDemo {
    public static final String INDEX_NAME = "trec";

    public static void writeToFile(List<QueryResult> allResult, String filePath) {
        File resFile = new File(filePath);
        try {
            FileUtils.writeLines(resFile, allResult);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        log.info("1.对文件进行解析并创建索引，索引名称为trec，若已经创建，直接从第二步开始");
//        SGMLIndex.index("src/main/resources/1991", INDEX_NAME);
//        SGMLIndex.index("src/main/resources/1992", INDEX_NAME);
        log.info("2.解析TREC的主题词文件");
        List<QueryWords> queryWords = QueryExtractor.extract("src/main/resources/topics.151-200");
        log.info("3.对es索引进行搜索，获取搜索结果");
        List<QueryResult> allResult = new ArrayList<>();
        for (QueryWords qw : queryWords) {
            allResult.addAll(SGMLSearch.search(qw, INDEX_NAME));
        }
        log.info("4.将结果转换程TREC官方格式");
        writeToFile(allResult, "src/main/resources/res.txt");
        log.info("流程全部结束");
    }
}
