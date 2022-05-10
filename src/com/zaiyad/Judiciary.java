package com.zaiyad;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
public class Judiciary {

    // List of attributes

    public String url;
    public org.jsoup.nodes.Document document;
    public String courtName;
    public String dateOfTrial;
    public String judgeName;
    public String pdfURL;
    public String title;
    public String caseDateOld;
    public String caseTextOld;
    public String caseCourt;
    public String courtMatch;
    public String caseDate;
    public String caseText;
    public String titleCheck = "Judgments";

    // List of methods

    public void setDocumentURL(String url) {
//        this.documentURL = url;
    }

    public void getJudiciaryWebPage() throws IOException {
//        this.document = Jsoup.connect(documentURL).get();
    }

//    public void getPdfURlFromWebPage() {
//        Elements links = document.select("a[href]");
//        String[] urls = new String[links.size()];
//        for (int i = 0; i < links.size(); i++) {
//            urls[i] = links.get(i).attr("href");
//
//
//        }
//        this.pdfURL = urls[51];
//        System.out.println(this.pdfURL);
//
//
//    }
//    public void parseJudiciaryPage() {
//        final Elements ps = document.select("p[style*=center]");
//        String caseData = ps.text();
//        System.out.println(caseData);
//        Pattern pt = Pattern.compile("\\s\\w{5}\\s\\w{5}");
//        Matcher mt = pt.matcher(caseData);
//        boolean matchFound = mt.find();
//        String court = mt.group();
//        Pattern pt1 = Pattern.compile("\\d+\\s\\w+\\s\\d+");
//        Matcher mt1 = pt1.matcher(caseData);
//        boolean mt1found = mt1.find();
//        String date = mt1.group();
//
//
//
//
//
//        this.courtName = court;
//        this.dateOfTrial = date;
//        this.judgeName =  caseData.substring(caseData.indexOf("MR"),((caseData.indexOf("LL") + 2)));
//    }

    public void getPdfFromWebPage() {

    }

    public void getSentencesForKeywords(List<String> keywords) {

    }


}

