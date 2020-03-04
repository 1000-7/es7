package es7.trec.bean;

import lombok.Data;

@Data
public class QueryResult {
    private String queryId;
    private String q0;
    private String docId;
    private Integer rank;
    private Float score;
    private Integer runId;

    @Override
    public String toString() {
        return queryId + "\t" + q0 + "\t" + docId + "\t" + rank + "\t" + score + "\t" + runId;
    }
}
