package es7.trec.sgml;

import es7.trec.bean.SGMLDocument;
import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author unclewang
 * @date 2020/3/3 17:20
 */
public class SGMLReader {
    public static final String DOC_NUM = "DOCNO";
    public static final String DOC_ID = "DOCID";
    public static final String TEXT = "TEXT";
    public static final String END_TAG = "</DOC>";
    public static final String TEMPORARY_FILE = "temporary.txt";

    public static List<SGMLDocument> readFromDictionary(String dictionary) {
        File file = new File(dictionary);
        List<SGMLDocument> documents = new ArrayList<>();
        Arrays.stream(Objects.requireNonNull(file.listFiles())).forEach(f -> {
            documents.addAll(readFromMultiDocuments(f));
        });
        return documents;
    }


    public static List<SGMLDocument> readFromMultiDocuments(String filePath) {
        return readFromMultiDocuments(new File(filePath));
    }

    public static List<SGMLDocument> readFromMultiDocuments(File file) {
        List<SGMLDocument> documents = new ArrayList<>();
        try {
            List<String> stringList = FileUtils.readLines(file, StandardCharsets.UTF_8);
            StringBuilder docString = new StringBuilder();
            for (String s : stringList) {
                docString.append(s.replace("&","")).append("\n");
                if (s.equals(END_TAG)) {
//                    System.out.println(docString.length());
                    FileUtils.writeStringToFile(new File(TEMPORARY_FILE), docString.toString(), StandardCharsets.UTF_8);
                    SGMLDocument sgmlDocument = readFromFile(TEMPORARY_FILE);
                    documents.add(sgmlDocument);
                    docString.delete(0, docString.length());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return documents;
    }


    public  static SGMLDocument readFromFile(String filePath) {
        return readFromFile(new File(filePath));
    }

    public static SGMLDocument readFromFile(File file) {
        SGMLDocument sd = new SGMLDocument();
        SAXReader saxReader = new SAXReader();
        Document doc = null;
        try {
            doc = saxReader.read(file);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        Element rootElement = doc.getRootElement();
        rootElement.elements().forEach(element -> {
            String name = element.getName();
            String text = element.getText();
            switch (name) {
                case DOC_NUM:
                    sd.setDocNum(text);
                    break;
                case DOC_ID:
                    sd.setDocId(text);
                    break;
                case TEXT:
                    sd.setText(text);
                    break;
            }

        });
//        System.out.println(rootElement);
        return sd;
    }

    public static void main(String[] args) {
        System.out.println(readFromDictionary("/Users/unclewang/Downloads/cqk/tipster/wsj/1991/").size());

    }
}
