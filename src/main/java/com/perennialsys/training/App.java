package com.perennialsys.training;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class App {
    
    private static final String
        OUTPUT_FILE_NAME = "/home/uday/HelloWorld.pdf",
        LOGO_LOCATION = "/home/uday/logo.png",
        TEXT = "Welcome to Hello World";

    public static void main(String[] args) {
        createDocument(TEXT, OUTPUT_FILE_NAME, LOGO_LOCATION);
    }

    private static void createDocument(String text, String outputFileName, String logoLocation) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(outputFileName));
        } catch (FileNotFoundException | DocumentException e) {
            e.printStackTrace();
        }

        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA , 10, BaseColor.Blue);
        Chunk chunk = new Chunk(text, font);

        try {
            document.add(chunk);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        PdfPTable table = new PdfPTable(3);
        addTableHeader(table);
        addRows(table);
        try {
            addCustomRows(table, logoLocation);
            document.add(table);
        } catch (DocumentException | URISyntaxException | IOException e1) {
            e1.printStackTrace();
        }

        document.close();
    }

    private static void addTableHeader(PdfPTable table) {
        Stream.of("Header 1", "Header 2", "Header 3").forEach(columnTitle -> {
            PdfPCell header = new PdfPCell();
            header.setBackgroundColor(BaseColor.LIGHT_GRAY);
            header.setBorderWidth(1);
            header.setPhrase(new Phrase(columnTitle));
            table.addCell(header);
        });
    }

    private static void addRows(PdfPTable table) {
        table.addCell("row 1, col 1");
        table.addCell("row 1, col 2");
        table.addCell("row 1, col 3");
    }

    private static void addCustomRows(PdfPTable table, String logoLocation) throws URISyntaxException, BadElementException, IOException {
        Path path = Paths.get(logoLocation);
        Image img = Image.getInstance(path.toAbsolutePath().toString());
        img.scalePercent(10);

        PdfPCell imageCell = new PdfPCell(img);
        table.addCell(imageCell);

        PdfPCell horizontalAlignCell = new PdfPCell(new Phrase("row 2, col 2"));
        horizontalAlignCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(horizontalAlignCell);

        PdfPCell verticalAlignCell = new PdfPCell(new Phrase("row 2, col 3"));
        verticalAlignCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        table.addCell(verticalAlignCell);
    }
}
