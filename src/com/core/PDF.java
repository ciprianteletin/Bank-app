package com.core;

import java.io.FileOutputStream;
import java.util.Date;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Clasa in cadrul careia folosesc API-ul ItextPDF, prin intermediul careia datele utilizatorului(precum numele acestuia, IBAN etc)
 * se transpun intr-un PDF , salvat in calculatorul personal a acestuia(la alegerea sa) si gata pentru a fi printat.
 */
public class PDF {
    private Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.RED);
    private Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    private Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);

    private String IBAN,fullname,bank,type,path;
    public PDF(String IBAN,String fullname,String bank,String type,String path){
        this.IBAN=IBAN;
        this.fullname=fullname;
        this.bank=bank;
        this.type=type;
        this.path=path;
    }

    private void addMetaData(Document document) {
        document.addTitle("Personal card details");
        document.addSubject("IBAN");
        document.addKeywords("Bank, IBAN, Card");
        document.addAuthor(bank);
        document.addCreator(bank);
    }

    /**
     * Adaugarea de informatii in PDF, date ce se regasesc in baza de date
     * @param document
     * @throws DocumentException
     */
    private void addTitlePage(Document document)
            throws DocumentException {
        Paragraph preface = new Paragraph();
        // We add one empty line
        addEmptyLine(preface, 1);
        // Lets write a big header
        preface.add(new Paragraph("Bank statement", catFont));

        addEmptyLine(preface, 1);
        // Will create: Report generated by: _name, _date
        preface.add(new Paragraph(
                "Report generated by: " + bank + ", " + new Date(), smallBold));
        addEmptyLine(preface, 2);

        preface.add(new Paragraph(
                "In the following section you will find data about your bank card",
                smallBold));
        addEmptyLine(preface, 2);
        preface.add(new Paragraph(
                "Your bank statement are received from your bank("+bank+") and consist in the following data: IBAN, " +
                        "with helps you to make different transactions, your full name, the name of our bank and the card type",
                smallBold));
        addEmptyLine(preface,2);
        preface.add(new Paragraph("IBAN: "+IBAN+"\n"+
                "FullName: "+fullname+"\n"+
                "Bank name: "+bank+"\n"+
                "Type: "+type,redFont));
        document.add(preface);
    }

    private void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    /**
     * metoda folosita pentru crearea documentului PDF, unde se poate evidentia folosirea API-ului.
     */
    public void createDoc(){
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();
            addMetaData(document);
            addTitlePage(document);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}