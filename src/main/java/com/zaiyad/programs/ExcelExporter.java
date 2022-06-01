package com.zaiyad.programs;
import java.io.FileOutputStream;
import java.io.IOException;

import com.zaiyad.Judiciary;
import com.zaiyad.JudiciaryWebCrawler;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelExporter {


    public void ExcelExporter(JudiciaryWebCrawler newObj) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Cases");


            int rowCount = 0;

            Row headRow = sheet.createRow(0);
            Cell cellCourt = headRow.createCell(1);
            cellCourt.setCellValue("courtName");
            Cell cellDate = headRow.createCell(2);
            cellDate.setCellValue("dateOfTrial");
            Cell cellJudge = headRow.createCell(3);
            cellJudge.setCellValue("judgeName");
            Cell cellTitle = headRow.createCell(4);
            cellTitle.setCellValue("title");
            Cell cellDocument = headRow.createCell(5);
            cellDocument.setCellValue("documentUrl");
            Cell cellPdf = headRow.createCell(6);
            cellPdf.setCellValue("pdfUrl");
            Cell cellClaimant = headRow.createCell(7);
            cellClaimant.setCellValue("claimantName");
            Cell cellDefendant = headRow.createCell(8);
            cellDefendant.setCellValue("defendantName");
            Cell cellClaimantRep = headRow.createCell(9);
            cellClaimantRep.setCellValue("claimantRep");
            Cell cellDefendantRep = headRow.createCell(10);
            cellDefendantRep.setCellValue("defendantRep");


            for (Judiciary j : newObj.judiciaryLinkToCaseMap.values()) {
                Row row = sheet.createRow(++rowCount);
                Cell cell = row.createCell(1);
                cell.setCellValue(j.courtName);
                cell = row.createCell(2);
                cell.setCellValue(j.dateOfTrial);
                cell = row.createCell(3);
                cell.setCellValue(j.judgeName);
                cell = row.createCell(4);
                cell.setCellValue(j.title);
                cell = row.createCell(5);
                cell.setCellValue(j.documentURL);
                cell = row.createCell(6);
                cell.setCellValue(j.pdfURL);
                cell = row.createCell(7);
                cell.setCellValue(j.claimantName);
                cell = row.createCell(8);
                cell.setCellValue(j.defendantName);
                cell = row.createCell(9);
                cell.setCellValue(j.claimantRep);
                cell = row.createCell(10);
                cell.setCellValue(j.defendantRep);

            }


        try (FileOutputStream outputStream = new FileOutputStream("JudiciaryCases.xlsx")) {
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }



    }



