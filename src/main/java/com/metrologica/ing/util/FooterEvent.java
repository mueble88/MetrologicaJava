package com.metrologica.ing.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

public class FooterEvent extends PdfPageEventHelper {

    public void onEndPage(PdfWriter writer, Document document) {
        PdfContentByte cb = writer.getDirectContent();

        Paragraph link = new Paragraph("www.ingenieriametrologica.com\n\nadmin@ingenieriametrologica.com Medellín-Colombia Cel.350 263 49 47,312 257 12 36",
                FontFactory.getFont("arial",7,Font.NORMAL,BaseColor.BLACK)
        );

        Phrase footer = new Phrase(link); // Reemplaza "Texto del pie de página" por tu propio texto

        ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                footer,
                (document.right() - document.left()) / 2 + document.leftMargin(),
                document.bottom() - 10, 0);
    }

/*
    public void onEndPage(PdfWriter writer, Document document) {
        PdfContentByte cb = writer.getDirectContent();
//        PdfPTable table = new PdfPTable(1);

        PdfPCell link = new PdfPCell(new Paragraph("www.ingenieriametrologica.com",
                FontFactory.getFont("arial",7,Font.NORMAL,BaseColor.BLACK))
        );
        link.setHorizontalAlignment(Element.ALIGN_CENTER);
        link.setFixedHeight(12f);
        link.setBorder(0);
//        PdfPCell data = new PdfPCell(new Paragraph("admin@ingenieriametrologica.com Medellín-Colombia Cel.350 263 49 47,312 257 12 36",
//                FontFactory.getFont("arial",7,Font.NORMAL,BaseColor.BLACK))
//        );
//        data.setHorizontalAlignment(Element.ALIGN_CENTER);
//        data.setFixedHeight(12f);
//        data.setBorder(0);
//        table.setWidthPercentage(100);
//        table.addCell(link);
//        table.addCell(data);

        Phrase footer = new Phrase(String.valueOf(link)); // Reemplaza "Texto del pie de página" por tu propio texto

        ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                footer,
                (document.right() - document.left()) / 2 + document.leftMargin(),
                document.bottom() - 10, 0);
    }*/
}
