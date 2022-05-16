package com.zaiyad.programs;
import com.zaiyad.*;

import java.io.IOException;


public class MyClass {

    public static void main(String[] args) throws InterruptedException, IOException {
//        JudiciaryWebCrawler myObj = new JudiciaryWebCrawler();
//        myObj.judiciaryCrawler();
        Judiciary testCase = new Judiciary();
        testCase.setDocumentURL("https://www.judiciary.uk/judgments/r-v-zephaniah-mcleod/");
        testCase.getJudiciaryWebPage();
        testCase.getPdfURlFromWebPage();
        testCase.getPdfFromWebPage();

    }
    }


