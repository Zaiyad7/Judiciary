package com.zaiyad;

import com.zaiyad.programs.ExcelExporter;
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
public int pageCount;




    public static void main(String[] args) throws InterruptedException  {

    }

/* Generates page urls which allows crawler to traverse all the pages on the site, the maximum page number to crawl can be
set with setPageCount method and passing in the page number, this method also calls the crawl method and must be called to
start the program.
 */
    public void judiciaryCrawler() throws InterruptedException  {

            for (int i = 1; i < pageCount; i++) {
                String pageNum = (i) + "/?filter_type=judgment";
                String urlPage = "https://www.judiciary.uk/judgments/page/" + pageNum;
                crawl(1, urlPage, new ArrayList<String>());



            }

    }
// Sets the maximum page number for the crawler to traverse through and is stored in pageCount attribute.

    public void setPageCount(int pageNumber) {
        this.pageCount = pageNumber;

    }


/* Traverses through all the reports on the page by calling crawl method recursively, calls the request method
 and this method itself is called in judiciaryCrawler.
 */

    public void crawl(int level, String url, ArrayList<String> visited) throws InterruptedException {
        if(level <=10) {
            Judiciary judiciaryCase = request(url,visited);
            Thread.sleep(1000);
            if (judiciaryCase.document != null){
                for(Element link : judiciaryCase.document.select("h5.entry-title > a[href]")) {
                    String next_link = link.absUrl("href");
                    if(visited.contains(next_link) == false) {
                        crawl(level++, next_link, visited);
                    }
                }
            }

        }
    }

// Matches and stores the name of the court in courtName attribute and is called in request method.

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
                    judiciaryCase.courtName = ( str2);

                } else {
                    judiciaryCase.courtName =( str);
                }
            }
        } catch (ArrayIndexOutOfBoundsException a) {
            for (int i = 0; i < arrOfCourt.length; i++) {
                sb.append(arrOfCourt[i]);
            }
                String str3 = sb.toString();
                judiciaryCase.courtName =( str3);
            }
        }

// Matches and stores the trial date in dateOfTrial attribute and is called in request method.

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
                judiciaryCase.dateOfTrial =(date);

            } else if (mtD2Found) {
                String dateAlt = mtD2.group();
                judiciaryCase.dateOfTrial =( dateAlt);

            } else if (isOldFound) {
                Matcher mtDO = ptD.matcher(judiciaryCase.caseDateOld);
                boolean mtDOFound = mtDO.find();
                String dateOld = mtDO.group(1);
                judiciaryCase.dateOfTrial =( dateOld);

            } else if (mtDO2Found) {
                String dateAltOld = mtDO2.group();
                judiciaryCase.dateOfTrial =( dateAltOld);

            } else {
                judiciaryCase.dateOfTrial = "Trial date is not available";

            }
        }
    }
// Matches and stores the judge name in judgeName attribute and is called in request method.

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
                judiciaryCase.judgeName =( mtJ.group(2));

            } else if (mtJ2Found){

                judiciaryCase.judgeName = "Judge name not available";

            } else if (mtJOFound){
                judiciaryCase.judgeName =( mtJO.group(2));

            } else {
                judiciaryCase.judgeName =( judiciaryCase.caseText + judiciaryCase.caseTextOld);
            }
        }


    }
// Stores the title of report in title attribute and is called in request method.

    public void titleMatcher(Judiciary judiciaryCase) {

        if (!judiciaryCase.document.title().contains("Decisions – guidance for Proscribed Organisations Tribunal")) {

                String[] arrOfTitle = judiciaryCase.document.title().split("\\|");
                if (!arrOfTitle[0].contains("Judgments")) {
                    judiciaryCase.title = ( arrOfTitle[0]);
                }

        } else {
            System.out.println("All reports printed");
            System.exit(0);
        }

    }
/* Extracts the pdf text where available and stores in casePdfText attribute, by calling setPdfURL and getPdfFromWebPage
methods in Judiciary class. This method is called in request.
 */
    public void pdfExtractor(Judiciary judiciaryCase) throws IOException {

        Elements links = judiciaryCase.document.select("div > a[href]");
        String[] urls = new String[links.size()];
        for (int i = 0; i < links.size(); i++) {
            urls[i] = links.get(i).attr("href");
            if (urls[i].contains("pdf")) {
                judiciaryCase.setPdfURL(urls[i]);
                judiciaryCase.getPdfFromWebPage();
            }
        }

    }
/* Creates a Judiciary class object to store case information by calling Extractor and Matcher methods, using these to
populate attributes in the created Judiciary object and storing this in a hashmap with the case url as the key and the
object as the value. Returns judiciaryCase object to be used in crawl method.
 */

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

                  this.pdfExtractor(judiciaryCase);
                  this.titleMatcher(judiciaryCase);
                  this.dateMatcher(judiciaryCase);
                  this.courtMatcher(judiciaryCase);
                  this.judgeMatcher(judiciaryCase);


                if (!judiciaryCase.document.title().contains(judiciaryCase.titleCheck)) {

                    System.out.println(judiciaryCase.title);
                    System.out.println(judiciaryCase.dateOfTrial);
                    System.out.println(judiciaryCase.courtName);
                    System.out.println(judiciaryCase.judgeName);

                    judiciaryCase.documentURL = url;
                   judiciaryLinkToCaseMap.put(url,judiciaryCase);

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


