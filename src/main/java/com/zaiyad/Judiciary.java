package com.zaiyad;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.net.URL;

public class Judiciary {

    // List of attributes

    public String url;
    public org.jsoup.nodes.Document document;
    public String courtName;
    public String dateOfTrial;
    public String judgeName;
    public String pdfURL;
    public String casePdfText;
    public String title;
    public String caseDateOld;
    public String caseTextOld;
    public String caseCourt;
    public String courtMatch;
    public String caseDate;
    public String caseText;
    public String titleCheck = "Judgments";
    public String documentURL;
    public org.jsoup.nodes.Document doc;

    // List of methods


//  Sets DocumentURL attribute by passing in the url of the report, which you want to extract information from.

    public void setDocumentURL(String url) {
       this.documentURL = url;
    }

/*  Sets the pdfUrl attribute by passing the pdf url, so the pdf text can be extracted and is called in pdfExtractor
  method in JudiciaryWebCrawler class.
 */
    public void setPdfURL(String pdfUrl) {
        this.pdfURL = pdfUrl;
    }

// Obtains the html information from the url of the case and stores in doc attribute.

    public String setCourtName() {
        return this.courtName;
    }

    public void getJudiciaryWebPage() throws IOException {
        this.doc = Jsoup.connect(documentURL).get();
    }
// Obtains the pdf url and stores in pdfURL attribute but only for R-v-Zephaniah case.

    public void getPdfURlFromWebPage() {
        Elements links = doc.select("a[href]");
        String[] urls = new String[links.size()];
        for (int i = 0; i < links.size(); i++) {
            urls[i] = links.get(i).attr("href");

        }
        this.pdfURL = urls[51];
        System.out.println(this.pdfURL);
    }
// Obtains the case info and stores them in caseName,judgeName and dateOfTrial attribute, only for R-v-Zephaniah case.

    public void parseJudiciaryPage() {

        final Elements ps = doc.select("p[style*=center]");
        String caseData = ps.text();
        System.out.println(caseData);
        Pattern pt = Pattern.compile("\\s\\w{5}\\s\\w{5}");
        Matcher mt = pt.matcher(caseData);
        boolean matchFound = mt.find();
        String court = mt.group();
        Pattern pt1 = Pattern.compile("\\d+\\s\\w+\\s\\d+");
        Matcher mt1 = pt1.matcher(caseData);
        boolean mt1found = mt1.find();
        String date = mt1.group();


        this.courtName = court;
        this.dateOfTrial = date;
        this.judgeName =  caseData.substring(caseData.indexOf("MR"),((caseData.indexOf("LL") + 2)));
    }
/* Obtains the pdf text and stores in casePdfText attribute, is called in pdfExtractor method and normally called after
setting the pdfUrl by setPdfURL or getPdfURLFromWebPage methods.
 */
    public void getPdfFromWebPage() throws IOException {
            InputStream downloadedPdfFileStream = new URL(pdfURL).openStream();
            PDDocument pdfDocument = PDDocument.load(downloadedPdfFileStream);
            PDFTextStripper pdfStripper = new PDFTextStripper();
            this.casePdfText = pdfStripper.getText(pdfDocument);
            pdfDocument.close();
    }
//
//    public void getSentencesForKeywords(List<String> keywords) {
//
//    }
//
    }


