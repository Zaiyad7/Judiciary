package com.zaiyad;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

public class Judiciary {

    // List of attributes

    public String documentURL;
    public org.jsoup.nodes.Document document;
    public String courtName;
    public String dateOfTrial;
    public String judgeName;
    public String pdfURL;

    // List of methods

    public void setDocumentURL(String url) {
        this.documentURL = url;
    }

    public void getJudiciaryWebPage() throws IOException {
        this.document = Jsoup.connect(documentURL).get();
    }

    public void getPdfURlFromWebPage() {
        Elements links = document.select("a[href]");
        String[] urls = new String[links.size()];
        for (int i = 0; i < links.size(); i++) {
            urls[i] = links.get(i).attr("href");


        }
        this.pdfURL = urls[51];


    }
    public void parseJudiciaryPage() {
        final Elements ps = document.select("p[style*=center]");
        String caseData = ps.text();
        this.courtName = caseData.substring(caseData.indexOf("CROWN"),caseData.indexOf("AT"));
        this.dateOfTrial = caseData.substring(caseData.indexOf("1"),caseData.indexOf("SEN"));
        this.judgeName =  caseData.substring(caseData.indexOf("MR"),((caseData.indexOf("LL") + 2)));
    }

    public void getPdfFromWebPage() {

    }

    public void getSentencesForKeywords(List<String> keywords) {

    }

    public static void main(String[] args) throws IOException {

        // Calling Methods and printing attributes

        Judiciary obj = new Judiciary();
        obj.setDocumentURL("https://www.judiciary.uk/judgments/r-v-zephaniah-mcleod/");
        obj.getJudiciaryWebPage();
        obj.parseJudiciaryPage();
        obj.getPdfURlFromWebPage();
        System.out.println(obj.pdfURL);
        System.out.println(obj.dateOfTrial);
        System.out.println(obj.courtName);
        System.out.println(obj.judgeName);

    }
}

