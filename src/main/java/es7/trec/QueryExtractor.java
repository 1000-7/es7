package es7.trec;


import es7.trec.bean.QueryWords;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class QueryExtractor {
    public static List<QueryWords> extract(String filePath) {
        List<QueryWords> res = new ArrayList<>();
        try {
            List<String> lines = FileUtils.readLines(new File(filePath), StandardCharsets.UTF_8);
            QueryWords qw = new QueryWords();
            for (String line : lines) {
                if (line.startsWith("<num>")) {
                    qw.setQueryId(line.replace("<num>", "").replace(" Number:", "").trim());
                } else if (line.startsWith("<title>")) {
                    qw.setQuery(line.replace("<title>", "").replace("Topic:", "").trim());
                    res.add(qw);
                    qw = new QueryWords();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static void main(String[] args) {
        System.out.println(QueryExtractor.extract("src/main/resources/topics.151-200"));
    }
}
