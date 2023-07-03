package com.metrologica.ing.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import com.metrologica.ing.dto.HumedInDto;
import com.metrologica.ing.dto.TemInDto;
import com.metrologica.ing.dto.TemOutDto;
import com.metrologica.ing.model.Client;
import com.metrologica.ing.model.EquipmentInfo;
import com.metrologica.ing.model.Measures;
import com.metrologica.ing.model.TraceInfo;
import com.metrologica.ing.util.Utils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class PDFService {

    double patternHumedIn = 0;
    double equipmentHumedIn = 0;
    double errorHumedIn = 0;
    double standardDeviationHumedIn = 0;
    double patternTemIn = 0;
    double equipmentTemIn = 0;
    double errorTemIn = 0;
    double standardDeviationTemIn = 0;
    double patternTemOut = 0;
    double equipmentTemOut  = 0;
    double errorTemOut= 0;
    double standardDeviationTemOut = 0;

    public void savePDF(String nombreArchivo, Client client, EquipmentInfo equipmentInfo, TraceInfo traceInfo,HumedInDto humedIn, TemInDto temIn, TemOutDto temOut) {
        Document documento = new Document();

        try {
            // Crea un objeto PdfWriter para escribir el contenido en el archivo
            PdfWriter.getInstance(documento, new FileOutputStream(nombreArchivo));

            // Abre el documento para agregar contenido y se crea margen
            documento.open();
            documento.setMargins(50,50,50,50);

            PdfPTable header = header();
            documento.add(header);

            // Agregar titulo
            Paragraph titulo = new Paragraph("\n\nInforme De Calibración Trazable\n\n",
                    FontFactory.getFont("arial",14,Font.BOLD,BaseColor.BLACK)
                    );
            titulo.setAlignment(Chunk.ALIGN_CENTER);
            documento.add(titulo);

            //Tabla cliente
            PdfPTable tableClient = infCliente(client);
            documento.add(tableClient);

            //Tabla equipo
            PdfPTable tableEquipment = infEquipment(equipmentInfo);
            documento.add(tableEquipment);

            //Tabla trazabilidad
            PdfPTable tableTrace = infTrace(traceInfo);
            documento.add(tableTrace);

            Paragraph saltolinea = new Paragraph();
            saltolinea.add("\n\n");
            documento.add(saltolinea);

            //crear pie de pagina imagen
            Image imgNote = Image.getInstance("C:/Proyectos/ing/uploads/parrafo.png");
            imgNote.scaleToFit(500, 100);
            imgNote.setAlignment(Chunk.ALIGN_CENTER);
            documento.add(imgNote);

            Paragraph saltolinea1 = new Paragraph();
            saltolinea1.add("\n\n");
            documento.add(saltolinea1);

            //titulo firmas
            PdfPTable firmas = firma();
            documento.add(firmas);

            Paragraph saltolinea2 = new Paragraph();
            saltolinea2.add("\n\n\n\n\n");
            documento.add(saltolinea2);

            // agregar pie de pagina
            PdfPTable footer = footer();
            documento.add(footer);

//            ColumnText.showTextAligned(cd, Element.ALIGN_CENTER, footer(),
//                (documento.right() - documento.left()) / 2 + documento.leftMargin(),
//                documento.top() + 10, 0);

            // Segunda pagina 2da--------------------------------------------------------------->
            documento.newPage();
            documento.add(header);
            documento.add(saltolinea1);

            Paragraph infCalibracion = new Paragraph("Método De Calibración\n\n",
                    FontFactory.getFont("arial",11,Font.BOLD,BaseColor.BLACK)
            );
            infCalibracion.setAlignment(Chunk.ALIGN_LEFT);

            Paragraph paragraphCalibration1 = new Paragraph("El presente certificado muestra errores de instrumento por medio de comparación directa.\n" +
                    "La calibración de dicho instrumento se realizó de acuerdo a los pasos descritos en la calibración\n"+
                    "de termóhigrometro.",
                    FontFactory.getFont("arial",9,Font.NORMAL,BaseColor.BLACK));
            paragraphCalibration1.setAlignment(Chunk.ALIGN_LEFT);

            Paragraph incertidumbre = new Paragraph("Incertidumbre De La Medición\n",
                    FontFactory.getFont("arial",9,Font.BOLD,BaseColor.BLACK)
            );
            incertidumbre.setAlignment(Chunk.ALIGN_LEFT);

            Paragraph paragraphCalibration2 = new Paragraph("La incertidumbre expandida de la medida se ha obtenido multiplicando la incertidunbre combinada\n"+
                    "(fuentes de incertidunbre) por el factor de cobertura.\n",
                    FontFactory.getFont("arial",9,Font.NORMAL,BaseColor.BLACK));
            paragraphCalibration2.setAlignment(Chunk.ALIGN_LEFT);

            Paragraph fuenteIncertidumbre = new Paragraph("Fuentes De Incertidumbre\n",
                    FontFactory.getFont("arial",9,Font.BOLD,BaseColor.BLACK)
            );
            fuenteIncertidumbre.setAlignment(Chunk.ALIGN_LEFT);

            Paragraph paragraphCalibration3 = new Paragraph("Resolución del instrumento, resolución del equipo patrón, desviación estándar de la mediciones,\n"+
                    "trazabilidad de los patrones utilizados.",
                    FontFactory.getFont("arial",9,Font.NORMAL,BaseColor.BLACK));
            paragraphCalibration3.setAlignment(Chunk.ALIGN_LEFT);

            Paragraph saltolineaTablas = new Paragraph();
            saltolineaTablas.add("\n");
            documento.add(infCalibracion);
            documento.add(paragraphCalibration1);
            documento.add(incertidumbre);
            documento.add(paragraphCalibration2);
            documento.add(fuenteIncertidumbre);
            documento.add(paragraphCalibration3);
            documento.add(saltolineaTablas);
            documento.add(saltolineaTablas);
            PdfPTable tablaCalTempIn = tablaCalibracionTemperaturaIn(equipmentInfo, traceInfo);
            documento.add(tablaCalTempIn);
            documento.add(saltolineaTablas);
            PdfPTable tablaCalHumedad = tablaCalibracionHumedad(equipmentInfo, traceInfo);
            documento.add(tablaCalHumedad);
            documento.add(saltolineaTablas);
            PdfPTable tablaCalTemOut = tablaCalibracionTemperaturaOut(equipmentInfo, traceInfo);
            documento.add(tablaCalTemOut);
            documento.add(saltolinea2);
            documento.add(footer);

            Paragraph saltoDeLinea = new Paragraph();
            saltoDeLinea.add("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
            // Tercera pagina 3da--------------------------------------------------------------->
            documento.newPage();
            Paragraph tituloHumedadIn = new Paragraph("TOMA DE DATOS HUMEDAD IN \n",
                    FontFactory.getFont("arial",12,Font.NORMAL,BaseColor.BLACK)
            );
            tituloHumedadIn.setAlignment(Chunk.ALIGN_CENTER);
            PdfPTable tableHumedIn = tableHumedIn(humedIn);
            PdfPTable tableResultHumedIn =  tableResult1(patternHumedIn, equipmentHumedIn, errorHumedIn, standardDeviationHumedIn);
            PdfPTable tableResultHumedIn2 =  tableResult2(standardDeviationHumedIn);

            documento.add(tituloHumedadIn);
            documento.add(saltolineaTablas);
            documento.add(tableHumedIn);
            documento.add(saltolineaTablas);
            documento.add(tableResultHumedIn);
            documento.add(saltolineaTablas);
            documento.add(tableResultHumedIn2);
            documento.add(saltoDeLinea);
            documento.add(footer);

            // Tercera pagina 4da--------------------------------------------------------------->
            documento.newPage();
            Paragraph tituloTemIn = new Paragraph("TOMA DE DATOS TEMPERATURA IN \n",
                    FontFactory.getFont("arial",12,Font.NORMAL,BaseColor.BLACK)
            );
            tituloTemIn.setAlignment(Chunk.ALIGN_CENTER);
            PdfPTable tableTemIn = tableTemIn(temIn);
            PdfPTable tableResultTemIn =  tableResult1(patternTemIn, equipmentTemIn, errorTemIn, standardDeviationTemIn);
            PdfPTable tableResultTemIn2 =  tableResult2(standardDeviationTemIn);

            documento.add(tituloTemIn);
            documento.add(saltolineaTablas);
            documento.add(tableTemIn);
            documento.add(saltolineaTablas);
            documento.add(tableResultTemIn);
            documento.add(saltolineaTablas);
            documento.add(tableResultTemIn2);
            documento.add(saltoDeLinea);
            documento.add(footer);

            // Tercera pagina 5da--------------------------------------------------------------->
            documento.newPage();

            Paragraph tituloTemOut = new Paragraph("TOMA DE DATOS TEMPERATURA OUT \n",
                    FontFactory.getFont("arial",12,Font.NORMAL,BaseColor.BLACK)
            );
            tituloTemOut.setAlignment(Chunk.ALIGN_CENTER);
            PdfPTable tableTemOut = tableTemOut(temOut);
            PdfPTable tableResultTemOut =  tableResult1(patternTemOut, equipmentTemOut, errorTemOut, standardDeviationTemOut);
            PdfPTable tableResultTemOut2 =  tableResult2(standardDeviationTemOut);

            documento.add(tituloTemOut);
            documento.add(saltolineaTablas);
            documento.add(tableTemOut);
            documento.add(saltolineaTablas);
            documento.add(tableResultTemOut);
            documento.add(saltolineaTablas);
            documento.add(tableResultTemOut2);
            documento.add(saltoDeLinea);
            documento.add(footer);

            // Cierra el documento
            documento.close();

            System.out.println("Archivo PDF creado exitosamente.");
        } catch (Exception e) {
            System.out.println("Error al crear el archivo PDF: " + e);
        }
    }

    public PdfPTable header() throws BadElementException, IOException {
        PdfPTable table = new PdfPTable(2);

        PdfPCell titulo = new PdfPCell(new Paragraph("Grupo IngeniarCorp SAS",
                FontFactory.getFont("arial",12,Font.BOLD,BaseColor.BLACK))
        );
        titulo.setColspan(2);
        titulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        titulo.setFixedHeight(20f);
        titulo.setBorder(0);
        table.addCell(titulo);

        Image logo1 = Image.getInstance("C:/Proyectos/ing/uploads/LogoIngM1.png");
        PdfPCell img1 = new PdfPCell(logo1);
        img1.setHorizontalAlignment(Element.ALIGN_LEFT);
        img1.setFixedHeight(10f);
        img1.setBorder(0);

        Image logo2 = Image.getInstance("C:/Proyectos/ing/uploads/LogoIngM2.png");
        PdfPCell img2 = new PdfPCell(logo2);
        img2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        img2.setFixedHeight(10f);
        img2.setBorder(0);

        table.setWidthPercentage(100);
        table.getDefaultCell().setBorder(0);
        table.addCell(logo1);
        table.addCell(logo2);

        return table;
    }

    public PdfPTable infCliente(Client client) throws DocumentException {

        int size = 8;
        PdfPTable table = new PdfPTable(4);
        table.setWidths(new float[] {25, 20, 25, 20});
        table.setWidthPercentage(90);
        table.getDefaultCell().setBorder(0);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell titulo = new PdfPCell(new Paragraph("INFORMACIÓN DEL CLIENTE",
                FontFactory.getFont("arial",9,Font.NORMAL,BaseColor.BLACK)));
        titulo.setColspan(4);
        titulo.setFixedHeight(14f);
        titulo.setBorder(0);
        titulo.setHorizontalAlignment(Element.ALIGN_LEFT);

        PdfPCell colum1 = new PdfPCell(new Paragraph(
                  "Nombre Solicitante: "+"\n"+
                        "Ciudad: "+"\n"+
                        "Dirección: ",
                FontFactory.getFont("arial",size,Font.BOLD,BaseColor.BLACK))
        );
        colum1.setHorizontalAlignment(Element.ALIGN_LEFT);
        colum1.setFixedHeight(36f);
        colum1.setBorder(0);

        PdfPCell colum2 = new PdfPCell(new Paragraph(
                client.getName()+"\n"
                        +client.getCity()+"\n"
                        +client.getAddress(),
                FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK))
        );
        colum2.setHorizontalAlignment(Element.ALIGN_LEFT);
        colum2.setFixedHeight(36f);
        colum2.setBorder(0);

        PdfPCell colum3 = new PdfPCell(new Paragraph(
                  "Teléfono: "+"\n"+
                        "Email: "+"\n"+
                        "NIT:",
                FontFactory.getFont("arial",size,Font.BOLD,BaseColor.BLACK))
        );
        colum3.setHorizontalAlignment(Element.ALIGN_LEFT);
        colum3.setFixedHeight(36f);
        colum3.setBorder(0);

        PdfPCell colum4 = new PdfPCell(new Paragraph(
                client.getNit()+"\n"
                        +client.getEmail()+"\n"
                        +client.getNit(),
                FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK))
        );
        colum4.setHorizontalAlignment(Element.ALIGN_LEFT);
        colum4.setFixedHeight(36f);
        colum4.setBorder(0);

        // agregar a la tabla
        table.addCell(titulo);
        table.addCell(colum1);
        table.addCell(colum2);
        table.addCell(colum3);
        table.addCell(colum4);

        return  table;
    }

    public PdfPTable infEquipment(EquipmentInfo equipmentInfo) throws DocumentException {

        int size = 8;
        PdfPTable table = new PdfPTable(4);
        table.setWidths(new float[] {25, 20, 25, 20});
        table.setWidthPercentage(90);
        table.getDefaultCell().setBorder(0);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell titulo = new PdfPCell(new Paragraph("INFORMACIÓN DEL EQUIPO",
                FontFactory.getFont("arial",9,Font.NORMAL,BaseColor.BLACK)));
        titulo.setColspan(4);
        titulo.setFixedHeight(14f);
        titulo.setBorder(0);
        titulo.setHorizontalAlignment(Element.ALIGN_LEFT);

        PdfPCell colum1 = new PdfPCell(new Paragraph(
                  "Equipo: "+"\n"+
                        "Marca:"+"\n"+
                        "Modelo:"+"\n"+
                        "Serie: "+"\n"+
                        "Placa: "+"\n"+
                        "Ubicación: ",
                FontFactory.getFont("arial",size,Font.BOLD,BaseColor.BLACK))
        );
        colum1.setHorizontalAlignment(Element.ALIGN_LEFT);
        colum1.setFixedHeight(72f);
        colum1.setBorder(0);

        PdfPCell colum2 = new PdfPCell(new Paragraph(
                equipmentInfo.getName()+"\n"
                        +equipmentInfo.getBrand()+"\n"
                        +equipmentInfo.getModel()+"\n"
                        +equipmentInfo.getSerialNumber()+"\n"
                        +equipmentInfo.getPlate()+"\n"
                        +equipmentInfo.getLocation(),
                FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK))
        );
        colum2.setHorizontalAlignment(Element.ALIGN_LEFT);
        colum2.setFixedHeight(72f);
        colum2.setBorder(0);

        PdfPCell colum3 = new PdfPCell(new Paragraph(
                "Fecha De Recepción: "+
                        "\n"+"Fecha De Calibración: "+
                        "\n"+"Magnitud A Medir: "+
                        "\n"+"Unidad: "+
                        "\n"+"Rango De Medición: "+
                        "\n"+"Resolución: ",
                FontFactory.getFont("arial",size,Font.BOLD,BaseColor.BLACK))
        );
        colum3.setHorizontalAlignment(Element.ALIGN_LEFT);
        colum3.setFixedHeight(72f);
        colum3.setBorder(0);

        PdfPCell colum4 = new PdfPCell(new Paragraph(
                equipmentInfo.getReceptionTimestamp()+
                        "\n"+equipmentInfo.getCalibrationTimestamp()+
                        "\n"+equipmentInfo.getMeasure()+
                        "\n"+equipmentInfo.getUnity()+
                        "\n"+equipmentInfo.getMeasureRange()+
                        "\n"+equipmentInfo.getResolution(),
                FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK))
        );
        colum4.setHorizontalAlignment(Element.ALIGN_LEFT);
        colum4.setFixedHeight(72f);
        colum4.setBorder(0);

        // agregar a la tabla
        table.addCell(titulo);
        table.addCell(colum1);
        table.addCell(colum2);
        table.addCell(colum3);
        table.addCell(colum4);

        return  table;
    }

    public PdfPTable infTrace(TraceInfo traceInfo) throws DocumentException {

        int size = 8;
        PdfPTable table = new PdfPTable(4);
        table.setWidths(new float[] {25, 20, 25, 20});
        table.setWidthPercentage(90);
        table.getDefaultCell().setBorder(0);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell titulo = new PdfPCell(new Paragraph("INFORMACIÓN TRAZABILIDAD",
                FontFactory.getFont("arial",9,Font.NORMAL,BaseColor.BLACK)));
        titulo.setColspan(4);
        titulo.setFixedHeight(14f);
        titulo.setBorder(0);
        titulo.setHorizontalAlignment(Element.ALIGN_LEFT);

        PdfPCell colum1 = new PdfPCell(new Paragraph(
                "Equipo: "+
                        "\n"+"Modelo: "+
                        "\n"+"Serie: "+
                        "\n"+"Fecha de Calibración Patrón:"+
                        "\n"+"Certificado: ",
                FontFactory.getFont("arial",size,Font.BOLD,BaseColor.BLACK))
        );
        colum1.setHorizontalAlignment(Element.ALIGN_LEFT);
        colum1.setFixedHeight(60f);
        colum1.setBorder(0);

        PdfPCell colum2 = new PdfPCell(new Paragraph(
                traceInfo.getName()+"\n"+
                        traceInfo.getModel()+"\n"+
                        traceInfo.getSerialNumber()+"\n"+
                        traceInfo.getCalibrationTimestamp()+"\n"+
                        traceInfo.getCertificate(),
                FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK))
        );
        colum2.setHorizontalAlignment(Element.ALIGN_LEFT);
        colum2.setFixedHeight(60f);
        colum2.setBorder(0);

        PdfPCell colum3 = new PdfPCell(new Paragraph(""));
        colum3.setFixedHeight(60f);
        colum3.setBorder(0);

        PdfPCell colum4 = new PdfPCell(new Paragraph(""));
        colum4.setFixedHeight(60f);
        colum4.setBorder(0);

        // agregar a la tabla
        table.addCell(titulo);
        table.addCell(colum1);
        table.addCell(colum2);
        table.addCell(colum3);
        table.addCell(colum4);

        return  table;
    }

    public PdfPTable firma() throws DocumentException, IOException {
        PdfPTable table = new PdfPTable(2);
        table.setWidths(new float[] {92, 92});
//        table.setWidthPercentage(80);
        table.getDefaultCell().setBorder(0);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell linea = new PdfPCell(new Paragraph("___________________________________",
                FontFactory.getFont("arial",7,Font.BOLD,BaseColor.BLACK)));
        linea.setHorizontalAlignment(Element.ALIGN_LEFT);
        linea.setFixedHeight(12f);
        linea.setBorder(0);

        PdfPCell titulo1  = new PdfPCell(new Paragraph("Revisado Por:",
                FontFactory.getFont("arial",9,Font.BOLD,BaseColor.BLACK))
        );
        titulo1.setHorizontalAlignment(Element.ALIGN_LEFT);
        titulo1.setFixedHeight(18f);
        titulo1.setBorder(0);
        titulo1.setPaddingBottom(4);

        PdfPCell titulo2  = new PdfPCell(new Paragraph("Calibrado Por:",
                FontFactory.getFont("arial",9,Font.BOLD,BaseColor.BLACK))
        );
        titulo2.setHorizontalAlignment(Element.ALIGN_LEFT);
        titulo2.setFixedHeight(18f);
        titulo2.setBorder(0);
        titulo2.setPaddingBottom(4);

        Image firma1 = Image.getInstance("C:/Proyectos/ing/uploads/firma.png");
        PdfPCell img1 = new PdfPCell(firma1);
        img1.setHorizontalAlignment(Element.ALIGN_LEFT);
        img1.setPaddingLeft(20);
        img1.setFixedHeight(32f);
        img1.setBorder(0);

        Image firma2 = Image.getInstance("C:/Proyectos/ing/uploads/firma2.png");
        PdfPCell img2 = new PdfPCell(firma2);
        img2.setHorizontalAlignment(Element.ALIGN_LEFT);
        img2.setPaddingLeft(20);
        img2.setFixedHeight(32f);
        img2.setBorder(0);

        PdfPCell name1 = new PdfPCell(new Paragraph("Ing. Biomédico J.J.Cárdenas",
                FontFactory.getFont("arial",7,Font.NORMAL,BaseColor.BLACK))
        );
        name1.setHorizontalAlignment(Element.ALIGN_LEFT);
        name1.setFixedHeight(11f);
        name1.setBorder(0);

        PdfPCell name2 = new PdfPCell(new Paragraph("Ing. Biomédico E.Castrillón",
                FontFactory.getFont("arial",7,Font.NORMAL,BaseColor.BLACK))
        );
        name2.setHorizontalAlignment(Element.ALIGN_LEFT);
        name2.setFixedHeight(11f);
        name2.setBorder(0);

        PdfPCell registro1 = new PdfPCell(new Paragraph("Registro Invima RH-201304-300",
                FontFactory.getFont("arial",7,Font.NORMAL,BaseColor.BLACK))
        );
        registro1.setHorizontalAlignment(Element.ALIGN_LEFT);
        registro1.setFixedHeight(11f);
        registro1.setBorder(0);

        PdfPCell registro2 = new PdfPCell(new Paragraph("Registro Invima RH-201912-9159",
                FontFactory.getFont("arial",7,Font.NORMAL,BaseColor.BLACK))
        );
        registro2.setHorizontalAlignment(Element.ALIGN_LEFT);
        registro2.setFixedHeight(11f);
        registro2.setBorder(0);

        PdfPCell matricula1 = new PdfPCell(new Paragraph("Matricula Profesional 05244-327846 ANT",
                FontFactory.getFont("arial",7,Font.NORMAL,BaseColor.BLACK))
        );
        matricula1.setHorizontalAlignment(Element.ALIGN_LEFT);
        matricula1.setFixedHeight(11f);
        matricula1.setBorder(0);

        PdfPCell matricula2 = new PdfPCell(new Paragraph("Matricula Profesional 011030-0530172 ANT",
                FontFactory.getFont("arial",7,Font.NORMAL,BaseColor.BLACK))
        );
        matricula2.setHorizontalAlignment(Element.ALIGN_LEFT);
        matricula2.setFixedHeight(11f);
        matricula2.setBorder(0);

        table.addCell(titulo1);
        table.addCell(titulo2);
        table.addCell(img1);
        table.addCell(img2);
        table.addCell(linea);
        table.addCell(linea);
        table.addCell(name1);
        table.addCell(name2);
        table.addCell(registro1);
        table.addCell(registro2);
        table.addCell(matricula1);
        table.addCell(matricula2);

        return  table;
    }

    public PdfPTable footer(){

        PdfPTable table = new PdfPTable(1);

        PdfPCell link = new PdfPCell(new Paragraph("www.ingenieriametrologica.com",
                FontFactory.getFont("arial",7,Font.NORMAL,BaseColor.BLACK))
        );
        link.setHorizontalAlignment(Element.ALIGN_CENTER);
        link.setFixedHeight(12f);
        PdfPCell datos = new PdfPCell(new Paragraph("admin@ingenieriametrologica.com Medellín-Colombia Cel.350 263 49 47,312 257 12 36",
                FontFactory.getFont("arial",7,Font.NORMAL,BaseColor.BLACK))
        );
        datos.setHorizontalAlignment(Element.ALIGN_CENTER);
        datos.setFixedHeight(12f);
        link.setBorder(0);
        datos.setBorder(0);
        table.setWidthPercentage(100);
        table.addCell(link);
        table.addCell(datos);

//        ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, footer(),
//                (document.right() - document.left()) / 2 + document.leftMargin(),
//                document.top() + 10, 0);

        return table;
    }

    public PdfPTable tablaCalibracionTemperaturaIn(EquipmentInfo equipmentInfo, TraceInfo traceInfo) throws DocumentException {

        PdfPTable table = new PdfPTable(3);
        table.setWidths(new float[] {60, 20, 20});
        int size = 9;

        PdfPCell celdaTitulo = new PdfPCell(new Paragraph("CALIBRACIÓN EN TEMPERATURA IN",
                FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        celdaTitulo.setColspan(3);
        celdaTitulo.setFixedHeight(19f);
        celdaTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(celdaTitulo);

        PdfPCell grados = new PdfPCell(new Paragraph("C°",
                FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        grados.setFixedHeight(19f);
        grados.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell cell1 = new PdfPCell(new Paragraph("PROMEDIO DE LAS MEDICIONES",
                FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cell1.setFixedHeight(19f);
        cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell1.setPaddingLeft(4);
        table.addCell(cell1);
        PdfPCell cell2 = new PdfPCell(new Paragraph("23,3",
                FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cell2.setFixedHeight(19f);
        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell2);
        table.addCell(grados);

        PdfPCell cell4 = new PdfPCell(new Paragraph("EQUIPO PATRON",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cell4.setFixedHeight(19f);
        cell4.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell4.setPaddingLeft(4);
        table.addCell(cell4);
        PdfPCell cell5 = new PdfPCell(new Paragraph("23,79",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cell5.setFixedHeight(20f);
        cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell5);
        table.addCell(grados);

        PdfPCell cell7 = new PdfPCell(new Paragraph("ERROR",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cell7.setFixedHeight(19f);
        cell7.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell7.setPaddingLeft(4);
        table.addCell(cell7);
        PdfPCell cell8 = new PdfPCell(new Paragraph("-0,49",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cell8.setFixedHeight(19f);
        cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell8);
        table.addCell(grados);

        PdfPCell cell10 = new PdfPCell(new Paragraph("INCERTIDUMBRE EXPANDIDA",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cell10.setFixedHeight(19f);
        cell10.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell10.setPaddingLeft(4);
        table.addCell(cell10);
        PdfPCell cell11 = new PdfPCell(new Paragraph("±  0,129",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cell11.setFixedHeight(19f);
        cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell11);
        table.addCell(grados);

        return table;
    }

    public PdfPTable tablaCalibracionHumedad(EquipmentInfo equipmentInfo, TraceInfo traceInfo) throws DocumentException {

        PdfPTable table = new PdfPTable(3);
        table.setWidths(new float[] {60, 20, 20});
        int size = 9;

        PdfPCell celdaTitulo = new PdfPCell(new Paragraph("CALIBRACIÓN EN HUMEDAD",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        celdaTitulo.setColspan(3);
        celdaTitulo.setFixedHeight(19f);
        celdaTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(celdaTitulo);

        PdfPCell porcentaje = new PdfPCell(new Paragraph("%",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        porcentaje.setFixedHeight(19f);
        porcentaje.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell cell1 = new PdfPCell(new Paragraph("PROMEDIO DE LAS MEDICIONES",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cell1.setFixedHeight(19f);
        cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell1.setPaddingLeft(4);
        table.addCell(cell1);
        PdfPCell cell2 = new PdfPCell(new Paragraph("68,0",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cell2.setFixedHeight(19f);
        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell2);
        table.addCell(porcentaje);

        PdfPCell cell4 = new PdfPCell(new Paragraph("EQUIPO PATRON",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cell4.setFixedHeight(19f);
        cell4.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell4.setPaddingLeft(4);
        table.addCell(cell4);
        PdfPCell cell5 = new PdfPCell(new Paragraph("68,29",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cell5.setFixedHeight(19f);
        cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell5);
        table.addCell(porcentaje);

        PdfPCell cell7 = new PdfPCell(new Paragraph("ERROR",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cell7.setFixedHeight(19f);
        cell7.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell7.setPaddingLeft(4);
        table.addCell(cell7);
        PdfPCell cell8 = new PdfPCell(new Paragraph("-0,29",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cell8.setFixedHeight(19f);
        cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell8);
        table.addCell(porcentaje);

        PdfPCell cell10 = new PdfPCell(new Paragraph("INCERTIDUMBRE EXPANDIDA",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cell10.setFixedHeight(19f);
        cell10.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell10.setPaddingLeft(4);
        table.addCell(cell10);
        PdfPCell cell11 = new PdfPCell(new Paragraph("±  0,211",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cell11.setFixedHeight(19f);
        cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell11);
        table.addCell(porcentaje);

        return table;
    }

    public PdfPTable tablaCalibracionTemperaturaOut(EquipmentInfo equipmentInfo, TraceInfo traceInfo) throws DocumentException {

        PdfPTable table = new PdfPTable(3);
        table.setWidths(new float[] {60, 20, 20});
        int size = 9;

        PdfPCell celdaTitulo = new PdfPCell(new Paragraph("CALIBRACIÓN EN TEMPERATURA OUT",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        celdaTitulo.setColspan(3);
        celdaTitulo.setFixedHeight(19f);
        celdaTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(celdaTitulo);

        PdfPCell grados = new PdfPCell(new Paragraph("C°",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        grados.setFixedHeight(19f);
        grados.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell cell1 = new PdfPCell(new Paragraph("PROMEDIO DE LAS MEDICIONES",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cell1.setFixedHeight(19f);
        cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell1.setPaddingLeft(4);
        table.addCell(cell1);
        PdfPCell cell2 = new PdfPCell(new Paragraph("24,0",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cell2.setFixedHeight(19f);
        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell2);
        table.addCell(grados);

        PdfPCell cell4 = new PdfPCell(new Paragraph("EQUIPO PATRON",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cell4.setFixedHeight(19f);
        cell4.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell4.setPaddingLeft(4);
        table.addCell(cell4);
        PdfPCell cell5 = new PdfPCell(new Paragraph("23,79",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cell5.setFixedHeight(19f);
        cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell5);
        table.addCell(grados);

        PdfPCell cell7 = new PdfPCell(new Paragraph("ERROR",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cell7.setFixedHeight(19f);
        cell7.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell7.setPaddingLeft(4);
        table.addCell(cell7);
        PdfPCell cell8 = new PdfPCell(new Paragraph("0,21",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cell8.setFixedHeight(19f);
        cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell8);
        table.addCell(grados);

        PdfPCell cell10 = new PdfPCell(new Paragraph("INCERTIDUMBRE EXPANDIDA",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cell10.setFixedHeight(19f);
        cell10.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell10.setPaddingLeft(4);
        table.addCell(cell10);
        PdfPCell cell11 = new PdfPCell(new Paragraph("±  0,129",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cell11.setFixedHeight(19f);
        cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell11);
        table.addCell(grados);

        return table;
    }

    public  PdfPTable tableHumedIn(HumedInDto humedIn) throws DocumentException {
        PdfPTable table = new PdfPTable(5);
        table.setWidths(new float[] {5, 20, 20, 20, 20});
        int size = 9;
        double[] equipos = new double[humedIn.getMeasures().length];
        double[] patrones = new double[humedIn.getMeasures().length];
        double[] errores = new double[humedIn.getMeasures().length];
        double promedioEquipo = 0;
        double promedioPatron = 0;
        double promedioError = 0;
        double standardDeviationEquipment = 0;
        double standardDeviationPattern = 0;
        double standardDeviationError= 0;

        PdfPCell cellN = new PdfPCell(new Paragraph("N#",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellN.setFixedHeight(13f);
        cellN.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellN);

        PdfPCell cellMedicion = new PdfPCell(new Paragraph("MEDICIÓN",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellMedicion.setFixedHeight(13f);
        cellMedicion.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellMedicion);

        PdfPCell cellEquipo = new PdfPCell(new Paragraph("EQUIPO H%",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellEquipo.setFixedHeight(13f);
        cellEquipo.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellEquipo);

        PdfPCell cellPatron = new PdfPCell(new Paragraph("PATRON",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellPatron.setFixedHeight(13f);
        cellPatron.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellPatron);

        PdfPCell cellError = new PdfPCell(new Paragraph("ERROR",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellError.setFixedHeight(13f);
        cellError.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellError);

        for (int i = 0; i < humedIn.getMeasures().length; i++){
            equipos[i] = humedIn.getMeasures()[i].getEquipoH();
            patrones[i] = humedIn.getMeasures()[i].getPatron();
            errores[i] = humedIn.getMeasures()[i].getError();

            PdfPCell cell1 = new PdfPCell(new Paragraph(String.valueOf(i+1),FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
            cell1.setFixedHeight(14f);
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell1);

            PdfPCell cellGrados = new PdfPCell(new Paragraph("%RH",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
            cellGrados.setFixedHeight(16f);
            cellGrados.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cellGrados);

            PdfPCell cellEquipoOut = new PdfPCell(new Paragraph(String.valueOf(equipos[i]), FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
            cellEquipoOut.setFixedHeight(14f);
            cellEquipoOut.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cellEquipoOut);

            PdfPCell patron = new PdfPCell(new Paragraph(String.valueOf(patrones[i]), FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
            patron.setFixedHeight(16f);
            patron.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(patron);

            PdfPCell error = new PdfPCell(new Paragraph(String.valueOf(errores[i]), FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
            error.setFixedHeight(16f);
            error.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(error);

        }

        //        promedios

        promedioEquipo = Utils.calculateAverage(equipos);
        promedioPatron = Utils.calculateAverage(patrones);
        promedioError = Utils.calculateAverage(errores);

        PdfPCell cellPromedios = new PdfPCell(new Paragraph("PROMEDIOS",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellPromedios.setColspan(2);
        cellPromedios.setFixedHeight(20f);
        cellPromedios.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellPromedios);

        PdfPCell cellEquipoPromedio = new PdfPCell(new Paragraph(String.valueOf(promedioEquipo),FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellEquipoPromedio.setFixedHeight(20f);
        cellEquipoPromedio.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellEquipoPromedio);

        PdfPCell cellPatronPromedio = new PdfPCell(new Paragraph(String.valueOf(promedioPatron),FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellPatronPromedio.setFixedHeight(20f);
        cellPatronPromedio.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellPatronPromedio);

        PdfPCell cellErrorPromedio = new PdfPCell(new Paragraph(String.valueOf(promedioError),FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellErrorPromedio.setFixedHeight(20f);
        cellErrorPromedio.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellErrorPromedio);

        //  Desviación estandar

        standardDeviationEquipment = Utils.calculateStandardDeviation(equipos);
        standardDeviationPattern = Utils.calculateStandardDeviation(patrones);
        standardDeviationError = Utils.calculateStandardDeviation(errores);

        PdfPCell cellDesviacion = new PdfPCell(new Paragraph("DESVIACIÓN ESTÁNDAR",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellDesviacion.setColspan(2);
        cellDesviacion.setFixedHeight(20f);
        cellDesviacion.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellDesviacion);

        PdfPCell cellEquipmentDesviation = new PdfPCell(new Paragraph(String.valueOf(standardDeviationEquipment),FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellEquipmentDesviation.setFixedHeight(20f);
        cellEquipmentDesviation.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellEquipmentDesviation);

        PdfPCell cellPatterDesviation = new PdfPCell(new Paragraph(String.valueOf(standardDeviationPattern),FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellPatterDesviation.setFixedHeight(20f);
        cellPatterDesviation.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellPatterDesviation);

        PdfPCell cellErrorDesviation = new PdfPCell(new Paragraph(String.valueOf(standardDeviationError),FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellErrorDesviation.setFixedHeight(20f);
        cellErrorDesviation.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellErrorDesviation);

        patternHumedIn = promedioPatron;
        equipmentHumedIn = promedioEquipo;
        errorHumedIn = promedioError;
        standardDeviationHumedIn = standardDeviationPattern;

        return table;
    }


    public PdfPTable tableTemIn(TemInDto temIn) throws DocumentException {

        PdfPTable table = new PdfPTable(5);
        table.setWidths(new float[] {5, 20, 20, 20, 20});
        int size = 9;
        double[] equipos = new double[temIn.getMeasures().length];
        double[] patrones = new double[temIn.getMeasures().length];
        double[] errores = new double[temIn.getMeasures().length];
        double promedioEquipo = 0;
        double promedioPatron = 0;
        double promedioError = 0;
        double standardDeviationEquipment = 0;
        double standardDeviationPattern = 0;
        double standardDeviationError= 0;

        PdfPCell cellN = new PdfPCell(new Paragraph("N#",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellN.setFixedHeight(13f);
        cellN.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellN);

        PdfPCell cellMedicion = new PdfPCell(new Paragraph("MEDICIÓN",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellMedicion.setFixedHeight(13f);
        cellMedicion.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellMedicion);

        PdfPCell cellEquipo = new PdfPCell(new Paragraph("EQUIPO IN",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellEquipo.setFixedHeight(13f);
        cellEquipo.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellEquipo);

        PdfPCell cellPatron = new PdfPCell(new Paragraph("PATRON",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellPatron.setFixedHeight(13f);
        cellPatron.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellPatron);

        PdfPCell cellError = new PdfPCell(new Paragraph("ERROR",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellError.setFixedHeight(13f);
        cellError.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellError);


        for (int i = 0; i < temIn.getMeasures().length; i++){
            equipos[i] = temIn.getMeasures()[i].getEquipoH();
            patrones[i] = temIn.getMeasures()[i].getPatron();
            errores[i] = temIn.getMeasures()[i].getError();

            PdfPCell cell1 = new PdfPCell(new Paragraph(String.valueOf(i+1),FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
            cell1.setFixedHeight(14f);
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell1);

            PdfPCell cellGrados = new PdfPCell(new Paragraph("C°",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
            cellGrados.setFixedHeight(16f);
            cellGrados.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cellGrados);

            PdfPCell cellEquipoOut = new PdfPCell(new Paragraph(String.valueOf(equipos[i]), FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
            cellEquipoOut.setFixedHeight(14f);
            cellEquipoOut.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cellEquipoOut);

            PdfPCell patron = new PdfPCell(new Paragraph(String.valueOf(patrones[i]), FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
            patron.setFixedHeight(16f);
            patron.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(patron);

            PdfPCell error = new PdfPCell(new Paragraph(String.valueOf(errores[i]), FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
            error.setFixedHeight(16f);
            error.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(error);
        }

        //        Promedios
        promedioEquipo = Utils.calculateAverage(equipos);
        promedioPatron = Utils.calculateAverage(patrones);
        promedioError = Utils.calculateAverage(errores);

        PdfPCell cellPromedios = new PdfPCell(new Paragraph("PROMEDIOS",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellPromedios.setColspan(2);
        cellPromedios.setFixedHeight(20f);
        cellPromedios.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellPromedios);

        PdfPCell cellEquipoPromedio = new PdfPCell(new Paragraph(String.valueOf(promedioEquipo),FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellEquipoPromedio.setFixedHeight(20f);
        cellEquipoPromedio.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellEquipoPromedio);

        PdfPCell cellPatronPromedio = new PdfPCell(new Paragraph(String.valueOf(promedioPatron),FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellPatronPromedio.setFixedHeight(20f);
        cellPatronPromedio.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellPatronPromedio);

        PdfPCell cellErrorPromedio = new PdfPCell(new Paragraph(String.valueOf(promedioError),FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellErrorPromedio.setFixedHeight(20f);
        cellErrorPromedio.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellErrorPromedio);

        //  Desviación estandar
        standardDeviationEquipment = Utils.calculateStandardDeviation(equipos);
        standardDeviationPattern = Utils.calculateStandardDeviation(patrones);
        standardDeviationError = Utils.calculateStandardDeviation(errores);

        PdfPCell cellDesviation = new PdfPCell(new Paragraph("DESVIACIÓN ESTÁNDAR",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellDesviation.setColspan(2);
        cellDesviation.setFixedHeight(20f);
        cellDesviation.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellDesviation);

        PdfPCell cellEquipmentDesviation = new PdfPCell(new Paragraph(String.valueOf(standardDeviationEquipment),FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellEquipmentDesviation.setFixedHeight(20f);
        cellEquipmentDesviation.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellEquipmentDesviation);

        PdfPCell cellPatternDesviation = new PdfPCell(new Paragraph(String.valueOf(standardDeviationPattern),FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellPatternDesviation.setFixedHeight(20f);
        cellPatternDesviation.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellPatternDesviation);

        PdfPCell cellErrorDesviation = new PdfPCell(new Paragraph(String.valueOf(standardDeviationError),FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellErrorDesviation.setFixedHeight(20f);
        cellErrorDesviation.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellErrorDesviation);

        patternTemIn = promedioPatron;
        equipmentTemIn = promedioEquipo;
        errorTemIn = promedioError;
        standardDeviationTemIn = standardDeviationPattern;

        return table;
    }

    public PdfPTable tableTemOut(TemOutDto temOut) throws DocumentException {

        PdfPTable table = new PdfPTable(5);
        table.setWidths(new float[] {5, 20, 20, 20, 20});
        int size = 9;
        double[] equipos = new double[temOut.getMeasures().length];
        double[] patrones = new double[temOut.getMeasures().length];
        double[] errores = new double[temOut.getMeasures().length];
        double promedioEquipo = 0;
        double promedioPatron = 0;
        double promedioError = 0;
        double standardDeviationEquipment = 0;
        double standardDeviationPattern = 0;
        double standardDeviationError= 0;


        PdfPCell cellN = new PdfPCell(new Paragraph("N#",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellN.setFixedHeight(15f);
        cellN.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellN);

        PdfPCell cellMedicion = new PdfPCell(new Paragraph("MEDICIÓN",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellMedicion.setFixedHeight(15f);
        cellMedicion.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellMedicion);

        PdfPCell cellEquipo = new PdfPCell(new Paragraph("EQUIPO OUT",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellEquipo.setFixedHeight(15f);
        cellEquipo.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellEquipo);

        PdfPCell cellPatron = new PdfPCell(new Paragraph("PATRON",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellPatron.setFixedHeight(15f);
        cellPatron.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellPatron);

        PdfPCell cellError = new PdfPCell(new Paragraph("ERROR",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellError.setFixedHeight(15f);
        cellError.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellError);

        for (int i = 0; i < temOut.getMeasures().length; i++){
            equipos[i] = temOut.getMeasures()[i].getEquipoH();
            patrones[i] = temOut.getMeasures()[i].getPatron();
            errores[i] = temOut.getMeasures()[i].getError();

            PdfPCell cell1 = new PdfPCell(new Paragraph(String.valueOf(i+1),FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
            cell1.setFixedHeight(16f);
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell1);

            PdfPCell cellGrados = new PdfPCell(new Paragraph("C°",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
            cellGrados.setFixedHeight(16f);
            cellGrados.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cellGrados);

            PdfPCell cellEquipoOut = new PdfPCell(new Paragraph(String.valueOf(equipos[i]), FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
            cellEquipoOut.setFixedHeight(16f);
            cellEquipoOut.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cellEquipoOut);

            PdfPCell patron = new PdfPCell(new Paragraph(String.valueOf(patrones[i]), FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
            patron.setFixedHeight(16f);
            patron.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(patron);

            PdfPCell error = new PdfPCell(new Paragraph(String.valueOf(errores[i]), FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
            error.setFixedHeight(16f);
            error.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(error);

        }

//        promedios
        promedioEquipo = Utils.calculateAverage(equipos);
        promedioPatron = Utils.calculateAverage(patrones);
        promedioError = Utils.calculateAverage(errores);

        PdfPCell cellPromedios = new PdfPCell(new Paragraph("PROMEDIOS",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellPromedios.setColspan(2);
        cellPromedios.setFixedHeight(20f);
        cellPromedios.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellPromedios);

        PdfPCell cellEquipoPromedio = new PdfPCell(new Paragraph(String.valueOf(promedioEquipo),FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellEquipoPromedio.setFixedHeight(20f);
        cellEquipoPromedio.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellEquipoPromedio);

        PdfPCell cellPatronPromedio = new PdfPCell(new Paragraph(String.valueOf(promedioPatron),FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellPatronPromedio.setFixedHeight(20f);
        cellPatronPromedio.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellPatronPromedio);

        PdfPCell cellErrorPromedio = new PdfPCell(new Paragraph(String.valueOf(promedioError),FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellErrorPromedio.setFixedHeight(20f);
        cellErrorPromedio.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellErrorPromedio);

        //  Desviación estandar

        standardDeviationEquipment = Utils.calculateStandardDeviation(equipos);
        standardDeviationPattern = Utils.calculateStandardDeviation(patrones);
        standardDeviationError = Utils.calculateStandardDeviation(errores);

        PdfPCell cellDesviacion = new PdfPCell(new Paragraph("DESVIACIÓN ESTÁNDAR",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellDesviacion.setColspan(2);
        cellDesviacion.setFixedHeight(20f);
        cellDesviacion.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellDesviacion);

        PdfPCell cellEquipoDesviacion = new PdfPCell(new Paragraph(String.valueOf(standardDeviationEquipment),FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellEquipoDesviacion.setFixedHeight(20f);
        cellEquipoDesviacion.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellEquipoDesviacion);

        PdfPCell cellPatronDesviacion = new PdfPCell(new Paragraph(String.valueOf(standardDeviationPattern),FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellPatronDesviacion.setFixedHeight(20f);
        cellPatronDesviacion.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellPatronDesviacion);

        PdfPCell cellErrorDesviacion = new PdfPCell(new Paragraph(String.valueOf(standardDeviationError),FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellErrorDesviacion.setFixedHeight(20f);
        cellErrorDesviacion.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellErrorDesviacion);

        patternTemOut = promedioPatron;
        equipmentTemOut = promedioEquipo;
        errorTemOut = promedioError;
        standardDeviationTemOut = standardDeviationPattern;

        return table;
    }

    public PdfPTable tableResult1(double pattern,
                                 double equipment,
                                 double error,
                                 double standardDesviation) throws DocumentException {

        PdfPTable table = new PdfPTable(4);
        table.setWidths(new float[] {30,30,30,30});
        int size = 8;


        PdfPCell cellPatron = new PdfPCell(new Paragraph("PATRON",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellPatron.setFixedHeight(15f);
        cellPatron.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellPatron);

        PdfPCell cellEquipo = new PdfPCell(new Paragraph("EQUIPO",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellEquipo.setFixedHeight(15f);
        cellEquipo.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellEquipo);

        PdfPCell cellError = new PdfPCell(new Paragraph("ERROR",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellError.setFixedHeight(15f);
        cellError.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellError);

        PdfPCell cellDesStand = new PdfPCell(new Paragraph("DES STAND",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellDesStand.setFixedHeight(15f);
        cellDesStand.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellDesStand);

        PdfPCell cellPattern = new PdfPCell(new Paragraph(String.valueOf(pattern),FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellPattern.setFixedHeight(15f);
        cellPattern.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellPattern);

        PdfPCell cellEquipment = new PdfPCell(new Paragraph(String.valueOf(equipment),FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellEquipment.setFixedHeight(15f);
        cellEquipment.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellEquipment);

        PdfPCell cellErr = new PdfPCell(new Paragraph(String.valueOf(error),FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellErr.setFixedHeight(15f);
        cellErr.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellErr);

        PdfPCell cellStandardDeviation = new PdfPCell(new Paragraph(String.valueOf(standardDesviation),FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellStandardDeviation.setFixedHeight(15f);
        cellStandardDeviation.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellStandardDeviation);

        return table;
    }

    public PdfPTable tableResult2(double standardDesviation) throws DocumentException {

        PdfPTable table = new PdfPTable(6);
        table.setWidths(new float[] {30,30,30,30,30,30});
        int size = 8;

        double incertA = Utils.calculateIncertA(standardDesviation);
        double incertB1 = Utils.calculateIncertB1();
        double incertB2 = Utils.calculateIncertB2();
        double k = Utils.calculateK();
        double incetConb = Utils.calculateIncertConb(incertA, incertB1, incertB2);
        double incetExpand = Utils.calculateIncetExpandida(k,incetConb);

        PdfPCell cellIncertA = new PdfPCell(new Paragraph("INCERT A",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellIncertA.setFixedHeight(15f);
        cellIncertA.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellIncertA);

        PdfPCell cellB1 = new PdfPCell(new Paragraph("INCERT B1",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellB1.setFixedHeight(15f);
        cellB1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellB1);

        PdfPCell cellB2 = new PdfPCell(new Paragraph("INCERT B2",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellB2.setFixedHeight(15f);
        cellB2.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellB2);

        PdfPCell cellK = new PdfPCell(new Paragraph("K",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellK.setFixedHeight(15f);
        cellK.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellK);

        PdfPCell cellConb = new PdfPCell(new Paragraph("INCERT CONB",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellConb.setFixedHeight(15f);
        cellConb.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellConb);

        PdfPCell cellExpand= new PdfPCell(new Paragraph("INCERT EXPANDIDA",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellExpand.setFixedHeight(15f);
        cellExpand.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellExpand);

        PdfPCell cellIncert = new PdfPCell(new Paragraph(String.valueOf(incertA),FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellIncert.setFixedHeight(15f);
        cellIncert.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellIncert);

        PdfPCell cellIncertB1 = new PdfPCell(new Paragraph(String.valueOf(incertB1),FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellIncertB1.setFixedHeight(15f);
        cellIncertB1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellIncertB1);

        PdfPCell cellIncertB2 = new PdfPCell(new Paragraph(String.valueOf(incertB2),FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellIncertB2.setFixedHeight(15f);
        cellIncertB2.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellIncertB2);

        PdfPCell cellk = new PdfPCell(new Paragraph(String.valueOf(k),FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellk.setFixedHeight(15f);
        cellk.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellk);

        PdfPCell cellIncetConb = new PdfPCell(new Paragraph(String.valueOf(incetConb),FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellIncetConb.setFixedHeight(15f);
        cellIncetConb.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellIncetConb);

        PdfPCell cellIncetExp = new PdfPCell(new Paragraph(String.valueOf(incetExpand),FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellIncetExp.setFixedHeight(15f);
        cellIncetExp.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellIncetExp);

        return table;
    }


}


