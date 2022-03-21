package com.zaiyad.programs;
import com.zaiyad.*;

import java.io.IOException;

public class MyClass {
    public static void main(String[] args) throws IOException {
        Judiciary myObj = new Judiciary();

        myObj.setDocumentURL("https://www.judiciary.uk/judgments/r-v-zephaniah-mcleod/");
        myObj.getJudiciaryWebPage();
        myObj.parseJudiciaryPage();
        myObj.getPdfURlFromWebPage();
        System.out.println(myObj.pdfURL);
        System.out.println(myObj.dateOfTrial);
        System.out.println(myObj.courtName);
        System.out.println(myObj.judgeName);
    }
}
