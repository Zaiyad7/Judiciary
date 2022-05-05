package com.zaiyad;
import org.jsoup.nodes.Element;
import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.nodes.Document;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JudiciaryWebCrawler {

    public String caseCourt;
    public String courtMatch;
    public String caseDate;
    public String caseText;
    public String titleCheck = "Judgments";
    public String caseDateOld;
    public String caseTextOld;
    public org.jsoup.nodes.Document doc;
    public static String url;

    public static void main(String[] args) throws  InterruptedException {

        JudiciaryWebCrawler obj = new JudiciaryWebCrawler();
        obj.judiciaryCrawler();


    }
    public void judiciaryCrawler() throws InterruptedException {

            for (int i = 1; i < 1000; i++) {
                String pageNum = (i) + "/?filter_type=judgment";
                url = "https://www.judiciary.uk/judgments/page/" + pageNum;
                crawl(1, url, new ArrayList<String>());



            }

    }



    public void crawl(int level, String url, ArrayList<String> visited) throws InterruptedException {
        if(level <=10) {
            Document doc = request(url,visited);
            Thread.sleep(2000);
            if (doc != null){
                for(Element link : doc.select("h5.entry-title > a[href]")) {
                    String next_link = link.absUrl("href");
                    if(visited.contains(next_link) == false) {
                        crawl(level++, next_link, visited);
                    }
                }
            }

        }
    }

    public void courtMatcher() {
        String[] arrOfCourt = this.courtMatch.split(" ", 0);
        StringBuffer sb = new StringBuffer();
        StringBuffer sb2 = new StringBuffer();
        try {
            if (!doc.title().contains(titleCheck)) {
                for (int i = 4; i < 6; i++) {
                    sb.append(arrOfCourt[i]);
                }
                String str = sb.toString();

                if (str.contains("of")) {
                    for (int i = 4; i < arrOfCourt.length; i++) {
                        sb2.append(arrOfCourt[i]);
                    }
                }
                if (!str.contains("Court")) {
                    System.out.println("Court name not available");

                } else if (str.contains("of")) {
                    String str2 = sb2.toString();
                    System.out.println("Court name: " + str2);

                } else {
                    System.out.println("Court name: " + str);
                }
            }
        } catch (ArrayIndexOutOfBoundsException a) {
            for (int i = 0; i < arrOfCourt.length; i++) {
                sb.append(arrOfCourt[i]);
            }
                String str3 = sb.toString();
                System.out.println("Court name: " + str3);
            }
        }


    public void dateMatcher() {
        if (!doc.title().contains(titleCheck)) {
            Pattern ptD = Pattern.compile("Date(.*)");
            Matcher mtD = ptD.matcher(caseDate);
            boolean isFound = caseDate.contains("Date");

            Pattern ptD2 = Pattern.compile("\\d{1,2}\\s\\w*\\s\\d\\d\\d\\d");
            Matcher mtD2 = ptD2.matcher(caseDate);
            boolean mtD2Found = mtD2.find();

            boolean isOldFound = caseDateOld.contains("Date");
            Matcher mtDO2 = ptD2.matcher(caseDateOld);
            boolean mtDO2Found = mtDO2.find();

            if (isFound) {
                boolean mtDFound = mtD.find();
                String date = mtD.group(1);
                System.out.println("Trial date: " + date);

            } else if (mtD2Found) {
                String dateAlt = mtD2.group();
                System.out.println("Trial date: " + dateAlt);

            } else if (isOldFound) {
                Matcher mtDO = ptD.matcher(caseDateOld);
                boolean mtDOFound = mtDO.find();
                String dateOld = mtDO.group(1);
                System.out.println("Trial date: " + dateOld);

            } else if (mtDO2Found) {
                String dateAltOld = mtDO2.group();
                System.out.println("Trial date: " + dateAltOld);

            } else {
                System.out.println("Trial date is not available");
            }
        }
    }
    public void judgeMatcher() {
        if (!doc.title().contains(titleCheck)) {
            Pattern ptJ = Pattern.compile("(Before|BEFORE|before)(.*)(Between|BETWEEN|between|B e t w e e n|B E T W E E N|Re)");
            Matcher mtJ = ptJ.matcher(caseText);
            boolean mtJFound = mtJ.find();

            Pattern ptJ2 = Pattern.compile("^Between|BETWEEN|B E T W E E N");
            Matcher mtJ2 = ptJ2.matcher(caseText);
            boolean mtJ2Found = mtJ2.find();

            Matcher mtJO = ptJ.matcher(caseTextOld);
            boolean mtJOFound = mtJO.find();

            if (mtJFound) {
                System.out.println("Judge name " + mtJ.group(2));

            } else if (mtJ2Found){
                System.out.println("Judge name not available");

            } else if (mtJOFound){
                System.out.println("Judge name " + mtJO.group(2));

            } else {
                System.out.println("Judge/Party info: " + caseText + caseTextOld);
            }
        }


    }
    public void titleMatcher() {
        if (!doc.title().contains("Decisions – guidance for Proscribed Organisations Tribunal")) {

            if (doc.title().contains(titleCheck)) {
                String[] arrOfTitle = this.url.split("\\/");
                System.out.println("Page: " + arrOfTitle[5]);

            } else {
                String[] arrOfTitle = doc.title().split("\\|");
                System.out.println("Title: " + arrOfTitle[0]);
            }

        } else {
            System.out.println("All reports printed");
            System.exit(0);
        }

    }
    private  Document request(String url, ArrayList<String> v) {
        try {
            Connection con = Jsoup.connect(url);
            this.doc = con.get();
            if (con.response().statusCode() == 200) {

                Elements page = doc.select("p[style*=center]");
                Elements pageDate = doc.select("p[style*=right]");
                Elements pageCourt = doc.select("p:containsOwn(COURT)");
                Elements courtCheck = doc.select("span > a[href]");
                Elements dateOld = doc.select("p[align*=right]");
                Elements pageOld = doc.select("p[align*=center]");

                this.caseCourt = pageCourt.text();
                this.courtMatch = courtCheck.text();
                this.caseDate = pageDate.text();
                this.caseText = page.text();
                this.caseDateOld = dateOld.text();
                this.caseTextOld = pageOld.text();

                this.titleMatcher();
                this.dateMatcher();
                this.courtMatcher();
                this.judgeMatcher();

                v.add(url);
                return doc;
            }
            return null;
        }
        catch(IOException e) {
            return null;

        }

    }

}


