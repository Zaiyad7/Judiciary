package com.zaiyad.programs;
import com.zaiyad.*;

import java.io.IOException;


public class MyClass {
/* Starts crawler by first creating JudiciaryWebCrawler object, setting max page count to 1000 with setPageCount method to
crawl through every page of the website and calling judiciaryCrawler method to start the program.
 */
    public static void main(String[] args) throws InterruptedException {
        JudiciaryWebCrawler myObj = new JudiciaryWebCrawler();
        myObj.setPageCount(1000);
        myObj.judiciaryCrawler();

    }
    }


