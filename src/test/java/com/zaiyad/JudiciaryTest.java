package com.zaiyad;

import org.jsoup.Jsoup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class JudiciaryTest {
    Judiciary test = new Judiciary();
    @Test
    public void shouldReturnURL() {

        test.setDocumentURL("https://www.judiciary.uk/judgments/r-v-zephaniah-mcleod/");
        assertEquals("https://www.judiciary.uk/judgments/r-v-zephaniah-mcleod/",test.documentURL);
    }
    @Test
    public void shouldSetPdfUrl() {
        test.setPdfURL("https://www.judiciary.uk/wp-content/uploads/2022/05/Abrahart-v-Uni-Bristol-judgment-200522.pdf");
        assertEquals("https://www.judiciary.uk/wp-content/uploads/2022/05/Abrahart-v-Uni-Bristol-judgment-200522.pdf",test.pdfURL);
    }
    @Test
    public void shouldReturnDoc() throws IOException {
        test.setDocumentURL("https://www.judiciary.uk/judgments/r-v-zephaniah-mcleod/");
        test.getJudiciaryWebPage();
        assertNotNull(test.doc);
    }
    @Test
    public void shouldReturnPdfUrl() throws IOException {
        test.setDocumentURL("https://www.judiciary.uk/judgments/r-v-zephaniah-mcleod/");
        test.getJudiciaryWebPage();
        test.getPdfURlFromWebPage();
        assertEquals("https://www.judiciary.uk/wp-content/uploads/2021/11/R-v-McLeod-Sentencing-Remarks.pdf",test.pdfURL);
    }
    @Test
    public void shouldExtractCaseInfo() throws IOException {
        test.setDocumentURL("https://www.judiciary.uk/judgments/r-v-zephaniah-mcleod/");
        test.getJudiciaryWebPage();
        test.parseJudiciaryPage();
        assertTrue(test.dateOfTrial.contains("18 NOVEMBER 2021"));
        assertTrue(test.courtName.contains("CROWN COURT"));
        assertTrue(test.judgeName.contains("MR JUSTICE PEPPERALL"));

    }
    @Test
    public void shouldReturnPdf() throws IOException {
        test.setDocumentURL("https://www.judiciary.uk/judgments/r-v-zephaniah-mcleod/");
        test.getJudiciaryWebPage();
        test.getPdfURlFromWebPage();
        test.getPdfFromWebPage();
        assertNotNull(test.casePdfText);

    }
}