package com.zaiyad;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)

class JudiciaryWebCrawlerTest {
    JudiciaryWebCrawler testObj;
    Judiciary test1;
    Judiciary test2;
    String test1Url = "https://www.judiciary.uk/judgments/abrahart-v-university-of-bristol/";
    String test2Url = "https://www.judiciary.uk/judgments/sentencing-remarks-by-the-hon-mrs-justice-ellenbogen-dbe-r-v-ali-zain/";

 @BeforeAll
 public void setupAll() throws InterruptedException {
     testObj = new JudiciaryWebCrawler();
     testObj.setPageCount(2);
     test1 = new Judiciary();
     test2 = new Judiciary();
     testObj.judiciaryCrawler();

 }
    @Test
    public void shouldReturnPageNumber() {
     testObj.setPageCount(2);
     assertEquals(2,testObj.pageCount);

    }
    @Test
    public void shouldReturnMapSize()  {
        assertEquals(10,testObj.judiciaryLinkToCaseMap.size());

    }
    @Test
    public void shouldReturnDocument() {
     assertNotNull(testObj.judiciaryLinkToCaseMap.get(test1Url).document);
     assertNotNull(testObj.judiciaryLinkToCaseMap.get(test2Url).document);

    }
    @Test
    public void shouldReturnCaseInfo() {
        test1.title = "Title: Abrahart -v- University of Bristol ";
        test1.dateOfTrial = "Trial date: 20 May 2022";
        test1.courtName = "Court name: CountyCourt";
        test1.judgeName = "Judge name  HHJ Ralton ";
        assertTrue((testObj.judiciaryLinkToCaseMap.get(test1Url).title).contains(test1.title));
        assertTrue(testObj.judiciaryLinkToCaseMap.get(test1Url).dateOfTrial.contains(test1.dateOfTrial));
        assertTrue(testObj.judiciaryLinkToCaseMap.get(test1Url).courtName.contains(test1.courtName));
        assertTrue(testObj.judiciaryLinkToCaseMap.get(test1Url).judgeName.contains(test1.judgeName));

    }
    @Test
    public void shouldReturnCaseInfo2()  {
        test2.title = "Title: Sentencing remarks by The Hon Mrs Justice Ellenbogen DBE: R -v- Ali Zain ";
        test2.dateOfTrial = "Trial date: 17 May 2022";
        test2.courtName = "Court name: CrownCourt";
        test2.judgeName = "Judge name : The Hon Mrs Justice Ellenbogen DBE ";
        assertTrue(testObj.judiciaryLinkToCaseMap.get(test2Url).title.contains(test2.title));
        assertTrue(testObj.judiciaryLinkToCaseMap.get(test2Url).dateOfTrial.contains(test2.dateOfTrial));
        assertTrue(testObj.judiciaryLinkToCaseMap.get(test2Url).courtName.contains(test2.courtName));
        assertTrue(testObj.judiciaryLinkToCaseMap.get(test2Url).judgeName.contains(test2.judgeName));

    }
    @Test
    public void shouldReturnPdfURL() {
     assertEquals("https://www.judiciary.uk/wp-content/uploads/2022/05/Abrahart-v-Uni-Bristol-judgment-200522.pdf",testObj.judiciaryLinkToCaseMap.get(test1Url).pdfURL);
     assertEquals("https://www.judiciary.uk/wp-content/uploads/2022/05/R-v-Musharraf-judgment-180522.pdf",testObj.judiciaryLinkToCaseMap.get("https://www.judiciary.uk/judgments/sana-musharraf-v-r/").pdfURL);

    }
    @Test
    public void shouldReturnPdf()  {
        assertNotNull(testObj.judiciaryLinkToCaseMap.get(test1Url).casePdfText);
        assertNotNull(testObj.judiciaryLinkToCaseMap.get("https://www.judiciary.uk/judgments/sana-musharraf-v-r/").casePdfText);

    }


}