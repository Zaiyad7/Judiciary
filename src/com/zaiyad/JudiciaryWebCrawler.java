package com.zaiyad;
import org.jsoup.nodes.Element;
import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashMap;

public class JudiciaryWebCrawler {
public HashMap<String,Judiciary> judiciaryLinkToCaseMap = new HashMap<>();



    public static void main(String[] args) throws  InterruptedException {
      JudiciaryWebCrawler obj = new JudiciaryWebCrawler();

      obj.judiciaryCrawler();

    }
    public void judiciaryCrawler() throws InterruptedException {

            for (int i = 1; i < 1000; i++) {
                String pageNum = (i) + "/?filter_type=judgment";
                String urlPage = "https://www.judiciary.uk/judgments/page/" + pageNum;
                crawl(1, urlPage, new ArrayList<String>());



            }

    }



    public void crawl(int level, String url, ArrayList<String> visited) throws InterruptedException {
        if(level <=10) {
            Judiciary judiciaryCase = request(url,visited);
            Thread.sleep(2000);
            if (judiciaryCase.document != null){
                for(Element link : judiciaryCase.document.select("h5.entry-title > a[href]")) {

                    String next_link = link.absUrl("href");
                    judiciaryLinkToCaseMap.put(next_link,judiciaryCase);
                    if(visited.contains(next_link) == false) {
                        crawl(level++, next_link, visited);
                    }
                }
            }

        }
    }

    public void courtMatcher(Judiciary judiciaryCase) {

        String[] arrOfCourt = judiciaryCase.courtMatch.split(" ", 0);
        StringBuffer sb = new StringBuffer();
        StringBuffer sb2 = new StringBuffer();

        try {
            if (!judiciaryCase.document.title().contains(judiciaryCase.titleCheck)) {
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
                    judiciaryCase.courtName = "Court name not available";

                } else if (str.contains("of")) {
                    String str2 = sb2.toString();
                    judiciaryCase.courtName = ("Court name: " + str2);

                } else {
                    judiciaryCase.courtName =("Court name: " + str);
                }
            }
        } catch (ArrayIndexOutOfBoundsException a) {
            for (int i = 0; i < arrOfCourt.length; i++) {
                sb.append(arrOfCourt[i]);
            }
                String str3 = sb.toString();
                judiciaryCase.courtName =("Court name: " + str3);
            }
        }


    public void dateMatcher(Judiciary judiciaryCase) {

        if (!judiciaryCase.document.title().contains(judiciaryCase.titleCheck)) {
            Pattern ptD = Pattern.compile("Date(.*)");
            Matcher mtD = ptD.matcher(judiciaryCase.caseDate);
            boolean isFound = judiciaryCase.caseDate.contains("Date");

            Pattern ptD2 = Pattern.compile("\\d{1,2}\\s\\w*\\s\\d\\d\\d\\d");
            Matcher mtD2 = ptD2.matcher(judiciaryCase.caseDate);
            boolean mtD2Found = mtD2.find();

            boolean isOldFound = judiciaryCase.caseDateOld.contains("Date");
            Matcher mtDO2 = ptD2.matcher(judiciaryCase.caseDateOld);
            boolean mtDO2Found = mtDO2.find();

            if (isFound) {
                boolean mtDFound = mtD.find();
                String date = mtD.group(1);
                judiciaryCase.dateOfTrial =("Trial date: " + date);

            } else if (mtD2Found) {
                String dateAlt = mtD2.group();
                judiciaryCase.dateOfTrial =("Trial date: " + dateAlt);

            } else if (isOldFound) {
                Matcher mtDO = ptD.matcher(judiciaryCase.caseDateOld);
                boolean mtDOFound = mtDO.find();
                String dateOld = mtDO.group(1);
                judiciaryCase.dateOfTrial =("Trial date: " + dateOld);

            } else if (mtDO2Found) {
                String dateAltOld = mtDO2.group();
                judiciaryCase.dateOfTrial =("Trial date: " + dateAltOld);

            } else {
                judiciaryCase.dateOfTrial = "Trial date is not available";

            }
        }
    }
    public void judgeMatcher(Judiciary judiciaryCase) {

        if (!judiciaryCase.document.title().contains(judiciaryCase.titleCheck)) {
            Pattern ptJ = Pattern.compile("(Before|BEFORE|before)(.*)(Between|BETWEEN|between|B e t w e e n|B E T W E E N|Re)");
            Matcher mtJ = ptJ.matcher(judiciaryCase.caseText);
            boolean mtJFound = mtJ.find();

            Pattern ptJ2 = Pattern.compile("^Between|BETWEEN|B E T W E E N");
            Matcher mtJ2 = ptJ2.matcher(judiciaryCase.caseText);
            boolean mtJ2Found = mtJ2.find();

            Matcher mtJO = ptJ.matcher(judiciaryCase.caseTextOld);
            boolean mtJOFound = mtJO.find();

            if (mtJFound) {
                judiciaryCase.judgeName =("Judge name " + mtJ.group(2));

            } else if (mtJ2Found){

                judiciaryCase.judgeName = "Judge name not available";

            } else if (mtJOFound){
                judiciaryCase.judgeName =("Judge name " + mtJO.group(2));

            } else {
                judiciaryCase.judgeName =("Judge/Party info: " + judiciaryCase.caseText + judiciaryCase.caseTextOld);
            }
        }


    }
    public void titleMatcher(Judiciary judiciaryCase) {

        if (!judiciaryCase.document.title().contains("Decisions – guidance for Proscribed Organisations Tribunal")) {

                String[] arrOfTitle = judiciaryCase.document.title().split("\\|");
                if (!arrOfTitle[0].contains("Judgments")) {
                    judiciaryCase.title = ("Title: " + arrOfTitle[0]);
                }

        } else {
            System.out.println("All reports printed");
            System.exit(0);
        }

    }
    private  Judiciary request(String url, ArrayList<String> v) {

        try {
            Judiciary judiciaryCase = new Judiciary();
            Connection con = Jsoup.connect(url);
            judiciaryCase.document = con.get();

            if (con.response().statusCode() == 200) {

                Elements page = judiciaryCase.document.select("p[style*=center]");
                Elements pageDate = judiciaryCase.document.select("p[style*=right]");
                Elements pageCourt = judiciaryCase.document.select("p:containsOwn(COURT)");
                Elements courtCheck = judiciaryCase.document.select("span > a[href]");
                Elements dateOld = judiciaryCase.document.select("p[align*=right]");
                Elements pageOld = judiciaryCase.document.select("p[align*=center]");



                judiciaryCase.caseCourt = pageCourt.text();
                judiciaryCase.courtMatch = courtCheck.text();
                judiciaryCase.caseDate = pageDate.text();
                judiciaryCase.caseText = page.text();
                judiciaryCase.caseDateOld = dateOld.text();
                judiciaryCase.caseTextOld = pageOld.text();

                this.titleMatcher(judiciaryCase);
                this.dateMatcher(judiciaryCase);
                this.courtMatcher(judiciaryCase);
                this.judgeMatcher(judiciaryCase);

                if (!judiciaryCase.document.title().contains(judiciaryCase.titleCheck)) {

                    System.out.println(judiciaryCase.title);
                    System.out.println(judiciaryCase.dateOfTrial);
                    System.out.println(judiciaryCase.courtName);
                    System.out.println(judiciaryCase.judgeName);

                }
                v.add(url);

                return judiciaryCase;
            }
            return null;
        }
        catch(IOException e) {
            return null;

        }

    }

}


