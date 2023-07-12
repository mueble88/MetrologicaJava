package com.metrologica.ing.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import com.metrologica.ing.dto.HumedInDto;
import com.metrologica.ing.dto.TemInDto;
import com.metrologica.ing.dto.TemOutDto;
import com.metrologica.ing.model.*;
import com.metrologica.ing.repository.ReportFileRepository;
import com.metrologica.ing.util.Utils;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

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

//    @Value("classpath:resources/images/LogoIngM1.png")
//    Resource resourceFile;

    @Autowired
    private ResourceLoader resourceLoader;

    @Value( "${pdfDirectory}" )
    private String reportDirectory;

    @Autowired
    private ReportFileRepository reportFilesRepository;


    public void savePDF(String nameFile, Client client, EquipmentInfo equipmentInfo, TraceInfo traceInfo,HumedInDto humedIn, TemInDto temIn, TemOutDto temOut, long idReport) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        ReportFile reportFile = new ReportFile();

        try {
//            PdfWriter.getInstance(document, outputStream);
//            Resource resource = resourceLoader.getResource("classpath:reports/");
//            InputStream inputStream = resource.getInputStream();
            //Resource resource = resourceLoader.getResource ("classpath:reports/"+nameFile);
            //String fileName = resource.getFilename();
            File file = new File(reportDirectory + File.separator + nameFile);
            if(!file.exists()){
                file.createNewFile();
            }

            PdfWriter.getInstance(document, new FileOutputStream(file));
            // Opens the document to add content and margin is created.
            document.open();
            document.setMargins(50,50,50,50);

            PdfPTable header = header();
            document.add(header);

            Paragraph title  = new Paragraph("\n\nInforme De Calibración Trazable\n\n",
                    FontFactory.getFont("arial",14,Font.BOLD,BaseColor.BLACK)
                    );
            title .setAlignment(Chunk.ALIGN_CENTER);
            document.add(title );

            PdfPTable tableClient = infCliente(client);
            document.add(tableClient);

            PdfPTable tableEquipment = infEquipment(equipmentInfo);
            document.add(tableEquipment);

            PdfPTable tableTrace = infTrace(traceInfo);
            document.add(tableTrace);

            Paragraph lineBreak = new Paragraph();
            lineBreak.add("\n\n");
            document.add(lineBreak);

            String nameImg =  "parrafo.png";
            Image paragraph = loadFiel(nameImg);
            paragraph.scaleToFit(500, 100);
            paragraph.setAlignment(Chunk.ALIGN_CENTER);
            document.add(paragraph);
            document.add(lineBreak);
            PdfPTable firmas = firma();
            document.add(firmas);

            Paragraph lineBreak1 = new Paragraph();
            lineBreak1.add("\n\n\n\n\n");
            document.add(lineBreak1);

            PdfPTable footer = footer();
            document.add(footer);

            // 2da page --------------------------------------------------------------->
            document.newPage();
            document.add(header);
            document.add(lineBreak);

            Paragraph infCalibracion = new Paragraph("Método De Calibración\n\n",
                    FontFactory.getFont("arial",11,Font.BOLD,BaseColor.BLACK)
            );
            infCalibracion.setAlignment(Chunk.ALIGN_LEFT);

            Paragraph paragraphCalibration1 = new Paragraph("El presente certificado muestra errores de instrumento por medio de comparación directa.\n" +
                    "La calibración de dicho instrumento se realizó de acuerdo a los pasos descritos en la calibración\n"+
                    "de termóhigrometro.",
                    FontFactory.getFont("arial",9,Font.NORMAL,BaseColor.BLACK));
            paragraphCalibration1.setAlignment(Chunk.ALIGN_LEFT);

            Paragraph uncertainty = new Paragraph("Incertidumbre De La Medición\n",
                    FontFactory.getFont("arial",9,Font.BOLD,BaseColor.BLACK)
            );
            uncertainty.setAlignment(Chunk.ALIGN_LEFT);

            Paragraph paragraphCalibration2 = new Paragraph("La incertidumbre expandida de la medida se ha obtenido multiplicando la incertidunbre combinada\n"+
                    "(fuentes de incertidunbre) por el factor de cobertura.\n",
                    FontFactory.getFont("arial",9,Font.NORMAL,BaseColor.BLACK));
            paragraphCalibration2.setAlignment(Chunk.ALIGN_LEFT);

            Paragraph sourceOfUncertainty = new Paragraph("Fuentes De Incertidumbre\n",
                    FontFactory.getFont("arial",9,Font.BOLD,BaseColor.BLACK)
            );
            sourceOfUncertainty.setAlignment(Chunk.ALIGN_LEFT);

            Paragraph paragraphCalibration3 = new Paragraph("Resolución del instrumento, resolución del equipo patrón, desviación estándar de la mediciones,\n"+
                    "trazabilidad de los patrones utilizados.",
                    FontFactory.getFont("arial",9,Font.NORMAL,BaseColor.BLACK));
            paragraphCalibration3.setAlignment(Chunk.ALIGN_LEFT);

            Paragraph lineBreakTable = new Paragraph();
            lineBreakTable.add("\n");

            document.add(infCalibracion);
            document.add(paragraphCalibration1);
            document.add(uncertainty);
            document.add(paragraphCalibration2);
            document.add(sourceOfUncertainty);
            document.add(paragraphCalibration3);
            document.add(lineBreak);
            PdfPTable tableCalTempIn = temperatureCalibrationTableIn(equipmentInfo, traceInfo);
            document.add(tableCalTempIn);
            document.add(lineBreakTable);
            PdfPTable tableCalHumedad = calibrationHumedTable(equipmentInfo, traceInfo);
            document.add(tableCalHumedad);
            document.add(lineBreakTable);
            PdfPTable tableCalTemOut = temperatureCalibrationTableOut(equipmentInfo, traceInfo);
            document.add(tableCalTemOut);
            document.add(lineBreak1);
            document.add(footer);

            // 3er page--------------------------------------------------------------->
            Paragraph lineBreakFooter = new Paragraph();
            lineBreakFooter.add("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
            document.newPage();
            Paragraph titleHumedadIn = new Paragraph("TOMA DE DATOS HUMEDAD IN \n",
                    FontFactory.getFont("arial",12,Font.NORMAL,BaseColor.BLACK)
            );
            titleHumedadIn.setAlignment(Chunk.ALIGN_CENTER);
            PdfPTable tableHumedIn = tableHumedIn(humedIn);
            PdfPTable tableResultHumedIn =  tableResult1(patternHumedIn, equipmentHumedIn, errorHumedIn, standardDeviationHumedIn);
            PdfPTable tableResultHumedIn2 =  tableResult2(standardDeviationHumedIn);

            document.add(titleHumedadIn);
            document.add(lineBreakTable);
            document.add(tableHumedIn);
            document.add(lineBreakTable);
            document.add(tableResultHumedIn);
            document.add(lineBreakTable);
            document.add(tableResultHumedIn2);
            document.add(lineBreakFooter);
            document.add(footer);

            // 4ta page--------------------------------------------------------------->
            document.newPage();
            Paragraph titleTemIn = new Paragraph("TOMA DE DATOS TEMPERATURA IN \n",
                    FontFactory.getFont("arial",12,Font.NORMAL,BaseColor.BLACK)
            );
            titleTemIn.setAlignment(Chunk.ALIGN_CENTER);
            PdfPTable tableTemIn = tableTemIn(temIn);
            PdfPTable tableResultTemIn =  tableResult1(patternTemIn, equipmentTemIn, errorTemIn, standardDeviationTemIn);
            PdfPTable tableResultTemIn2 =  tableResult2(standardDeviationTemIn);

            document.add(titleTemIn);
            document.add(lineBreakTable);
            document.add(tableTemIn);
            document.add(lineBreakTable);
            document.add(tableResultTemIn);
            document.add(lineBreakTable);
            document.add(tableResultTemIn2);
            document.add(lineBreakFooter);
            document.add(footer);

            // 5ta page--------------------------------------------------------------->
            document.newPage();

            Paragraph tituloTemOut = new Paragraph("TOMA DE DATOS TEMPERATURA OUT \n",
                    FontFactory.getFont("arial",12,Font.NORMAL,BaseColor.BLACK)
            );
            tituloTemOut.setAlignment(Chunk.ALIGN_CENTER);
            PdfPTable tableTemOut = tableTemOut(temOut);
            PdfPTable tableResultTemOut =  tableResult1(patternTemOut, equipmentTemOut, errorTemOut, standardDeviationTemOut);
            PdfPTable tableResultTemOut2 =  tableResult2(standardDeviationTemOut);

            document.add(tituloTemOut);
            document.add(lineBreakTable);
            document.add(tableTemOut);
            document.add(lineBreakTable);
            document.add(tableResultTemOut);
            document.add(lineBreakTable);
            document.add(tableResultTemOut2);
            document.add(lineBreakFooter);
            document.add(footer);

//            PdfContentByte cb = writer.getDirectContent();
//            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, footer(),
//                (document.right() - document.left()) / 2 + document.leftMargin(),
//                    document.top() + 10, 0);

            // Cierra el documento
            document.close();

            byte[] pdfBytes = outputStream.toByteArray();
//            String encoded = Base64Utils.encodeToString(pdfBytes);
            UUID uuid = UUID.randomUUID();

            reportFile.setId(uuid);
            reportFile.setReportId(idReport);
            reportFile.setFile(" ");
            reportFile.setFilename(nameFile);
            reportFilesRepository.save(reportFile);

//            List<ReportFile> allReportFiles= reportFilesRepository.findAll();
//            System.out.println(allReportFiles);

            System.out.println("Archivo PDF creado exitosamente.");
            System.out.println(reportFile.getFile());
        } catch (Exception e) {
            System.out.println("Error al crear el archivo PDF: " + e.getStackTrace());
        }
    }

    public ReportFile getReportFile(UUID id) {
        return reportFilesRepository.findById(id).get();
    }

    public List<ReportFile> getAllReportFile(){
        List<ReportFile> pdfs = reportFilesRepository.findAll();
        System.out.println("lista de pdf:"+pdfs);
        return pdfs;
    }

    public Image loadFiel(String nameImg) throws IOException, BadElementException {
        Resource resource = resourceLoader.getResource("classpath:images/"+nameImg);
        File file = resource.getFile();
        Image image = Image.getInstance(String.valueOf(file));

        return image;
    }

    public PdfPTable header() throws BadElementException, IOException {
        PdfPTable table = new PdfPTable(2);

        PdfPCell title = new PdfPCell(new Paragraph("Grupo IngeniarCorp SAS",
                FontFactory.getFont("arial",12,Font.BOLD,BaseColor.BLACK))
        );
        title.setColspan(2);
        title.setHorizontalAlignment(Element.ALIGN_CENTER);
        title.setFixedHeight(20f);
        title.setBorder(0);
        table.addCell(title);

        String nameImg1 =  "LogoIngM1.png";
        Image logo1 = loadFiel(nameImg1);
        PdfPCell img1 = new PdfPCell(logo1);
        img1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        img1.setFixedHeight(10f);
        img1.setBorder(0);

        String nameImg2 =  "LogoIngM2.png";
        Image logo2 = loadFiel(nameImg2);
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

        PdfPCell title = new PdfPCell(new Paragraph("INFORMACIÓN DEL CLIENTE",
                FontFactory.getFont("arial",9,Font.NORMAL,BaseColor.BLACK)));
        title.setColspan(4);
        title.setFixedHeight(14f);
        title.setBorder(0);
        title.setHorizontalAlignment(Element.ALIGN_LEFT);

        PdfPCell colum1 = new PdfPCell(new Paragraph(
                  "Nombre Solicitante: "+"\n"+
                        "Ciudad: "+"\n"+
                        "Dirección: ",
                FontFactory.getFont("arial",size,Font.BOLD,BaseColor.BLACK))
        );
        colum1.setHorizontalAlignment(Element.ALIGN_LEFT);
        colum1.setFixedHeight(36f);
        colum1.setBorder(0);

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
                client.getPhone()+"\n"
                        +client.getEmail()+"\n"
                        +client.getNit(),
                FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK))
        );
        colum4.setHorizontalAlignment(Element.ALIGN_LEFT);
        colum4.setFixedHeight(36f);
        colum4.setBorder(0);

        table.addCell(title);
        table.addCell(colum1);

        if(client != null) {
            PdfPCell colum2 = new PdfPCell(new Paragraph(
                    client.getName()+"\n"
                            +client.getCityName()+"\n"
                            +client.getAddress(),
                    FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK))
            );
            colum2.setHorizontalAlignment(Element.ALIGN_LEFT);
            colum2.setFixedHeight(36f);
            colum2.setBorder(0);
            table.addCell(colum2);
        }
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

        PdfPCell title = new PdfPCell(new Paragraph("INFORMACIÓN DEL EQUIPO",
                FontFactory.getFont("arial",9,Font.NORMAL,BaseColor.BLACK)));
        title.setColspan(4);
        title.setFixedHeight(14f);
        title.setBorder(0);
        title.setHorizontalAlignment(Element.ALIGN_LEFT);

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

        table.addCell(title);
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

        PdfPCell title = new PdfPCell(new Paragraph("INFORMACIÓN TRAZABILIDAD",
                FontFactory.getFont("arial",9,Font.NORMAL,BaseColor.BLACK)));
        title.setColspan(4);
        title.setFixedHeight(14f);
        title.setBorder(0);
        title.setHorizontalAlignment(Element.ALIGN_LEFT);

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

        table.addCell(title);
        table.addCell(colum1);
        table.addCell(colum2);
        table.addCell(colum3);
        table.addCell(colum4);

        return  table;
    }

    public PdfPTable firma() throws DocumentException, IOException {
        PdfPTable table = new PdfPTable(2);
        table.setWidths(new float[] {92, 92});
        table.getDefaultCell().setBorder(0);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell line = new PdfPCell(new Paragraph("___________________________________",
                FontFactory.getFont("arial",7,Font.BOLD,BaseColor.BLACK)));
        line.setHorizontalAlignment(Element.ALIGN_LEFT);
        line.setFixedHeight(12f);
        line.setBorder(0);

        PdfPCell title1  = new PdfPCell(new Paragraph("Revisado Por:",
                FontFactory.getFont("arial",9,Font.BOLD,BaseColor.BLACK))
        );
        title1.setHorizontalAlignment(Element.ALIGN_LEFT);
        title1.setFixedHeight(18f);
        title1.setBorder(0);
        title1.setPaddingBottom(4);

        PdfPCell title2  = new PdfPCell(new Paragraph("Calibrado Por:",
                FontFactory.getFont("arial",9,Font.BOLD,BaseColor.BLACK))
        );
        title2.setHorizontalAlignment(Element.ALIGN_LEFT);
        title2.setFixedHeight(18f);
        title2.setBorder(0);
        title2.setPaddingBottom(4);

        String nameImg1 =  "firma.png";
        Image firma1 = loadFiel(nameImg1);
        PdfPCell img1 = new PdfPCell(firma1);
        img1.setHorizontalAlignment(Element.ALIGN_LEFT);
        img1.setPaddingLeft(20);
        img1.setFixedHeight(32f);
        img1.setBorder(0);

        String nameImg2 =  "firma2.png";
        Image firma2 = loadFiel(nameImg2);
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

        PdfPCell registration1 = new PdfPCell(new Paragraph("Registro Invima RH-201304-300",
                FontFactory.getFont("arial",7,Font.NORMAL,BaseColor.BLACK))
        );
        registration1.setHorizontalAlignment(Element.ALIGN_LEFT);
        registration1.setFixedHeight(11f);
        registration1.setBorder(0);

        PdfPCell registration2 = new PdfPCell(new Paragraph("Registro Invima RH-201912-9159",
                FontFactory.getFont("arial",7,Font.NORMAL,BaseColor.BLACK))
        );
        registration2.setHorizontalAlignment(Element.ALIGN_LEFT);
        registration2.setFixedHeight(11f);
        registration2.setBorder(0);

        PdfPCell registration3 = new PdfPCell(new Paragraph("Matricula Profesional 05244-327846 ANT",
                FontFactory.getFont("arial",7,Font.NORMAL,BaseColor.BLACK))
        );
        registration3.setHorizontalAlignment(Element.ALIGN_LEFT);
        registration3.setFixedHeight(11f);
        registration3.setBorder(0);

        PdfPCell registration4 = new PdfPCell(new Paragraph("Matricula Profesional 011030-0530172 ANT",
                FontFactory.getFont("arial",7,Font.NORMAL,BaseColor.BLACK))
        );
        registration4.setHorizontalAlignment(Element.ALIGN_LEFT);
        registration4.setFixedHeight(11f);
        registration4.setBorder(0);

        table.addCell(title1);
        table.addCell(title2);
        table.addCell(img1);
        table.addCell(img2);
        table.addCell(line);
        table.addCell(line);
        table.addCell(name1);
        table.addCell(name2);
        table.addCell(registration1);
        table.addCell(registration2);
        table.addCell(registration3);
        table.addCell(registration4);

        return  table;
    }

    public PdfPTable footer(){

        PdfPTable table = new PdfPTable(1);

        PdfPCell link = new PdfPCell(new Paragraph("www.ingenieriametrologica.com",
                FontFactory.getFont("arial",7,Font.NORMAL,BaseColor.BLACK))
        );
        link.setHorizontalAlignment(Element.ALIGN_CENTER);
        link.setFixedHeight(12f);
        PdfPCell data = new PdfPCell(new Paragraph("admin@ingenieriametrologica.com Medellín-Colombia Cel.350 263 49 47,312 257 12 36",
                FontFactory.getFont("arial",7,Font.NORMAL,BaseColor.BLACK))
        );
        data.setHorizontalAlignment(Element.ALIGN_CENTER);
        data.setFixedHeight(12f);
        link.setBorder(0);
        data.setBorder(0);
        table.setWidthPercentage(100);
        table.addCell(link);
        table.addCell(data);

        return table;
    }

    public PdfPTable temperatureCalibrationTableIn(EquipmentInfo equipmentInfo, TraceInfo traceInfo) throws DocumentException {

        PdfPTable table = new PdfPTable(3);
        table.setWidths(new float[] {60, 20, 20});
        int size = 9;

        PdfPCell cellTitle = new PdfPCell(new Paragraph("CALIBRACIÓN EN TEMPERATURA IN",
                FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellTitle.setColspan(3);
        cellTitle.setFixedHeight(19f);
        cellTitle.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellTitle);

        PdfPCell degrees  = new PdfPCell(new Paragraph("C°",
                FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        degrees.setFixedHeight(19f);
        degrees.setHorizontalAlignment(Element.ALIGN_CENTER);

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
        table.addCell(degrees);

        PdfPCell cell4 = new PdfPCell(new Paragraph("EQUIPO PATRON",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cell4.setFixedHeight(19f);
        cell4.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell4.setPaddingLeft(4);
        table.addCell(cell4);
        PdfPCell cell5 = new PdfPCell(new Paragraph("23,79",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cell5.setFixedHeight(20f);
        cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell5);
        table.addCell(degrees);

        PdfPCell cell7 = new PdfPCell(new Paragraph("ERROR",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cell7.setFixedHeight(19f);
        cell7.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell7.setPaddingLeft(4);
        table.addCell(cell7);
        PdfPCell cell8 = new PdfPCell(new Paragraph("-0,49",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cell8.setFixedHeight(19f);
        cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell8);
        table.addCell(degrees);

        PdfPCell cell10 = new PdfPCell(new Paragraph("INCERTIDUMBRE EXPANDIDA",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cell10.setFixedHeight(19f);
        cell10.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell10.setPaddingLeft(4);
        table.addCell(cell10);
        PdfPCell cell11 = new PdfPCell(new Paragraph("±  0,129",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cell11.setFixedHeight(19f);
        cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell11);
        table.addCell(degrees);

        return table;
    }

    public PdfPTable calibrationHumedTable(EquipmentInfo equipmentInfo, TraceInfo traceInfo) throws DocumentException {

        PdfPTable table = new PdfPTable(3);
        table.setWidths(new float[] {60, 20, 20});
        int size = 9;

        PdfPCell cellTitle = new PdfPCell(new Paragraph("CALIBRACIÓN EN HUMEDAD",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellTitle.setColspan(3);
        cellTitle.setFixedHeight(19f);
        cellTitle.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellTitle);

        PdfPCell percentage = new PdfPCell(new Paragraph("%",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        percentage.setFixedHeight(19f);
        percentage.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell cell1 = new PdfPCell(new Paragraph("PROMEDIO DE LAS MEDICIONES",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cell1.setFixedHeight(19f);
        cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell1.setPaddingLeft(4);
        table.addCell(cell1);
        PdfPCell cell2 = new PdfPCell(new Paragraph("68,0",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cell2.setFixedHeight(19f);
        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell2);
        table.addCell(percentage);

        PdfPCell cell4 = new PdfPCell(new Paragraph("EQUIPO PATRON",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cell4.setFixedHeight(19f);
        cell4.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell4.setPaddingLeft(4);
        table.addCell(cell4);
        PdfPCell cell5 = new PdfPCell(new Paragraph("68,29",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cell5.setFixedHeight(19f);
        cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell5);
        table.addCell(percentage);

        PdfPCell cell7 = new PdfPCell(new Paragraph("ERROR",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cell7.setFixedHeight(19f);
        cell7.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell7.setPaddingLeft(4);
        table.addCell(cell7);
        PdfPCell cell8 = new PdfPCell(new Paragraph("-0,29",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cell8.setFixedHeight(19f);
        cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell8);
        table.addCell(percentage);

        PdfPCell cell10 = new PdfPCell(new Paragraph("INCERTIDUMBRE EXPANDIDA",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cell10.setFixedHeight(19f);
        cell10.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell10.setPaddingLeft(4);
        table.addCell(cell10);
        PdfPCell cell11 = new PdfPCell(new Paragraph("±  0,211",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cell11.setFixedHeight(19f);
        cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell11);
        table.addCell(percentage);

        return table;
    }

    public PdfPTable temperatureCalibrationTableOut(EquipmentInfo equipmentInfo, TraceInfo traceInfo) throws DocumentException {

        PdfPTable table = new PdfPTable(3);
        table.setWidths(new float[] {60, 20, 20});
        int size = 9;

        PdfPCell cellTitle = new PdfPCell(new Paragraph("CALIBRACIÓN EN TEMPERATURA OUT",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellTitle.setColspan(3);
        cellTitle.setFixedHeight(19f);
        cellTitle.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellTitle);

        PdfPCell degrees = new PdfPCell(new Paragraph("C°",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        degrees.setFixedHeight(19f);
        degrees.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell cell1 = new PdfPCell(new Paragraph("PROMEDIO DE LAS MEDICIONES",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cell1.setFixedHeight(19f);
        cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell1.setPaddingLeft(4);
        table.addCell(cell1);
        PdfPCell cell2 = new PdfPCell(new Paragraph("24,0",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cell2.setFixedHeight(19f);
        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell2);
        table.addCell(degrees);

        PdfPCell cell4 = new PdfPCell(new Paragraph("EQUIPO PATRON",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cell4.setFixedHeight(19f);
        cell4.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell4.setPaddingLeft(4);
        table.addCell(cell4);
        PdfPCell cell5 = new PdfPCell(new Paragraph("23,79",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cell5.setFixedHeight(19f);
        cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell5);
        table.addCell(degrees);

        PdfPCell cell7 = new PdfPCell(new Paragraph("ERROR",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cell7.setFixedHeight(19f);
        cell7.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell7.setPaddingLeft(4);
        table.addCell(cell7);
        PdfPCell cell8 = new PdfPCell(new Paragraph("0,21",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cell8.setFixedHeight(19f);
        cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell8);
        table.addCell(degrees);

        PdfPCell cell10 = new PdfPCell(new Paragraph("INCERTIDUMBRE EXPANDIDA",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cell10.setFixedHeight(19f);
        cell10.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell10.setPaddingLeft(4);
        table.addCell(cell10);
        PdfPCell cell11 = new PdfPCell(new Paragraph("±  0,129",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cell11.setFixedHeight(19f);
        cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell11);
        table.addCell(degrees);

        return table;
    }

    public  PdfPTable tableHumedIn(HumedInDto humedIn) throws DocumentException {
        PdfPTable table = new PdfPTable(5);
        table.setWidths(new float[] {5, 20, 20, 20, 20});
        int size = 9;
        double[] equipments = new double[humedIn.getMeasures().length];
        double[] patterns  = new double[humedIn.getMeasures().length];
        double[] errors = new double[humedIn.getMeasures().length];
        double averageEquipment = 0;
        double averagePattern = 0;
        double averageError = 0;
        double standardDeviationEquipment = 0;
        double standardDeviationPattern = 0;
        double standardDeviationError= 0;

        PdfPCell cellN = new PdfPCell(new Paragraph("N#",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellN.setFixedHeight(13f);
        cellN.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellN);

        PdfPCell cellMeasuring= new PdfPCell(new Paragraph("MEDICIÓN",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellMeasuring.setFixedHeight(13f);
        cellMeasuring.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellMeasuring);

        PdfPCell cellEquipment = new PdfPCell(new Paragraph("EQUIPO H%",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellEquipment.setFixedHeight(13f);
        cellEquipment.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellEquipment);

        PdfPCell cellPattern = new PdfPCell(new Paragraph("PATRON",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellPattern.setFixedHeight(13f);
        cellPattern.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellPattern);

        PdfPCell cellError = new PdfPCell(new Paragraph("ERROR",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellError.setFixedHeight(13f);
        cellError.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellError);

        for (int i = 0; i < humedIn.getMeasures().length; i++){
            equipments[i] = humedIn.getMeasures()[i].getEquipmentH();
            patterns [i] = humedIn.getMeasures()[i].getPattern();
            errors[i] = humedIn.getMeasures()[i].getError();

            PdfPCell cell1 = new PdfPCell(new Paragraph(String.valueOf(i+1),FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
            cell1.setFixedHeight(14f);
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell1);

            PdfPCell cellDegrees = new PdfPCell(new Paragraph("%RH",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
            cellDegrees.setFixedHeight(16f);
            cellDegrees.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cellDegrees);

            PdfPCell cellEquipmentOut = new PdfPCell(new Paragraph(String.valueOf(equipments[i]), FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
            cellEquipmentOut.setFixedHeight(14f);
            cellEquipmentOut.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cellEquipmentOut);

            PdfPCell pattern = new PdfPCell(new Paragraph(String.valueOf(patterns [i]), FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
            pattern.setFixedHeight(16f);
            pattern.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(pattern);

            PdfPCell error = new PdfPCell(new Paragraph(String.valueOf(errors[i]), FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
            error.setFixedHeight(16f);
            error.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(error);

        }

        //        promedios

        averageEquipment = Utils.calculateAverage(equipments);
        averagePattern = Utils.calculateAverage(patterns);
        averageError = Utils.calculateAverage(errors);

        PdfPCell cellAverages = new PdfPCell(new Paragraph("PROMEDIOS",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellAverages.setColspan(2);
        cellAverages.setFixedHeight(20f);
        cellAverages.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellAverages);

        PdfPCell cellEquipmentAverage = new PdfPCell(new Paragraph(String.valueOf(averageEquipment),FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellEquipmentAverage.setFixedHeight(20f);
        cellEquipmentAverage.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellEquipmentAverage);

        PdfPCell cellPatternAverage = new PdfPCell(new Paragraph(String.valueOf(averagePattern),FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellPatternAverage.setFixedHeight(20f);
        cellPatternAverage.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellPatternAverage);

        PdfPCell cellErrorAverage = new PdfPCell(new Paragraph(String.valueOf(averageError),FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellErrorAverage.setFixedHeight(20f);
        cellErrorAverage.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellErrorAverage);

        //  Desviación estandar

        standardDeviationEquipment = Utils.calculateStandardDeviation(equipments);
        standardDeviationPattern = Utils.calculateStandardDeviation(patterns);
        standardDeviationError = Utils.calculateStandardDeviation(errors);

        PdfPCell cellDesviation = new PdfPCell(new Paragraph("DESVIACIÓN ESTÁNDAR",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellDesviation.setColspan(2);
        cellDesviation.setFixedHeight(20f);
        cellDesviation.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellDesviation);

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

        patternHumedIn = averagePattern;
        equipmentHumedIn = averageEquipment;
        errorHumedIn = averageError;
        standardDeviationHumedIn = standardDeviationPattern;

        return table;
    }


    public PdfPTable tableTemIn(TemInDto temIn) throws DocumentException {

        PdfPTable table = new PdfPTable(5);
        table.setWidths(new float[] {5, 20, 20, 20, 20});
        int size = 9;
        double[] equipments = new double[temIn.getMeasures().length];
        double[] patterns = new double[temIn.getMeasures().length];
        double[] errors = new double[temIn.getMeasures().length];
        double averageEquipment = 0;
        double averagePattern = 0;
        double averageError = 0;
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
            equipments[i] = temIn.getMeasures()[i].getEquipmentH();
            patterns[i] = temIn.getMeasures()[i].getPattern();
            errors[i] = temIn.getMeasures()[i].getError();

            PdfPCell cell1 = new PdfPCell(new Paragraph(String.valueOf(i+1),FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
            cell1.setFixedHeight(14f);
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell1);

            PdfPCell cellGrados = new PdfPCell(new Paragraph("C°",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
            cellGrados.setFixedHeight(16f);
            cellGrados.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cellGrados);

            PdfPCell cellEquipoOut = new PdfPCell(new Paragraph(String.valueOf(equipments[i]), FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
            cellEquipoOut.setFixedHeight(14f);
            cellEquipoOut.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cellEquipoOut);

            PdfPCell patron = new PdfPCell(new Paragraph(String.valueOf(patterns[i]), FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
            patron.setFixedHeight(16f);
            patron.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(patron);

            PdfPCell error = new PdfPCell(new Paragraph(String.valueOf(errors[i]), FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
            error.setFixedHeight(16f);
            error.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(error);
        }

        //        Promedios
        averageEquipment = Utils.calculateAverage(equipments);
        averagePattern = Utils.calculateAverage(patterns);
        averageError = Utils.calculateAverage(errors);

        PdfPCell cellPromedios = new PdfPCell(new Paragraph("PROMEDIOS",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellPromedios.setColspan(2);
        cellPromedios.setFixedHeight(20f);
        cellPromedios.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellPromedios);

        PdfPCell cellEquipoPromedio = new PdfPCell(new Paragraph(String.valueOf(averageEquipment),FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellEquipoPromedio.setFixedHeight(20f);
        cellEquipoPromedio.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellEquipoPromedio);

        PdfPCell cellPatronPromedio = new PdfPCell(new Paragraph(String.valueOf(averagePattern),FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellPatronPromedio.setFixedHeight(20f);
        cellPatronPromedio.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellPatronPromedio);

        PdfPCell cellErrorPromedio = new PdfPCell(new Paragraph(String.valueOf(averageError),FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellErrorPromedio.setFixedHeight(20f);
        cellErrorPromedio.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellErrorPromedio);

        //  Desviación estandar
        standardDeviationEquipment = Utils.calculateStandardDeviation(equipments);
        standardDeviationPattern = Utils.calculateStandardDeviation(patterns);
        standardDeviationError = Utils.calculateStandardDeviation(errors);

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

        equipmentTemIn = averageEquipment;
        patternTemIn = averagePattern;
        errorTemIn = averageError;
        standardDeviationTemIn = standardDeviationPattern;

        return table;
    }

    public PdfPTable tableTemOut(TemOutDto temOut) throws DocumentException {

        PdfPTable table = new PdfPTable(5);
        table.setWidths(new float[] {5, 20, 20, 20, 20});
        int size = 9;
        double[] equipments = new double[temOut.getMeasures().length];
        double[] patterns = new double[temOut.getMeasures().length];
        double[] errors = new double[temOut.getMeasures().length];
        double averageEquipment = 0;
        double averagePattern = 0;
        double averageError = 0;
        double standardDeviationEquipment = 0;
        double standardDeviationPattern = 0;
        double standardDeviationError= 0;


        PdfPCell cellN = new PdfPCell(new Paragraph("N#",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellN.setFixedHeight(15f);
        cellN.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellN);

        PdfPCell cellMedition = new PdfPCell(new Paragraph("MEDICIÓN",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellMedition.setFixedHeight(15f);
        cellMedition.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellMedition);

        PdfPCell cellEquipment = new PdfPCell(new Paragraph("EQUIPO OUT",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellEquipment.setFixedHeight(15f);
        cellEquipment.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellEquipment);

        PdfPCell cellPattern = new PdfPCell(new Paragraph("PATRON",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellPattern.setFixedHeight(15f);
        cellPattern.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellPattern);

        PdfPCell cellError = new PdfPCell(new Paragraph("ERROR",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellError.setFixedHeight(15f);
        cellError.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellError);

        for (int i = 0; i < temOut.getMeasures().length; i++){
            equipments[i] = temOut.getMeasures()[i].getEquipmentH();
            patterns[i] = temOut.getMeasures()[i].getPattern();
            errors[i] = temOut.getMeasures()[i].getError();

            PdfPCell cell1 = new PdfPCell(new Paragraph(String.valueOf(i+1),FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
            cell1.setFixedHeight(16f);
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell1);

            PdfPCell cellDegrees = new PdfPCell(new Paragraph("C°",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
            cellDegrees.setFixedHeight(16f);
            cellDegrees.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cellDegrees);

            PdfPCell cellEquipmentOut = new PdfPCell(new Paragraph(String.valueOf(equipments[i]), FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
            cellEquipmentOut.setFixedHeight(16f);
            cellEquipmentOut.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cellEquipmentOut);

            PdfPCell pattern = new PdfPCell(new Paragraph(String.valueOf(patterns[i]), FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
            pattern.setFixedHeight(16f);
            pattern.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(pattern);

            PdfPCell error = new PdfPCell(new Paragraph(String.valueOf(errors[i]), FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
            error.setFixedHeight(16f);
            error.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(error);

        }

//        promedios
        averageEquipment = Utils.calculateAverage(equipments);
        averagePattern = Utils.calculateAverage(patterns);
        averageError = Utils.calculateAverage(errors);

        PdfPCell cellAverages = new PdfPCell(new Paragraph("PROMEDIOS",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellAverages.setColspan(2);
        cellAverages.setFixedHeight(20f);
        cellAverages.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellAverages);

        PdfPCell cellAverageEquipment = new PdfPCell(new Paragraph(String.valueOf(averageEquipment),FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellAverageEquipment.setFixedHeight(20f);
        cellAverageEquipment.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellAverageEquipment);

        PdfPCell cellAveragePattern = new PdfPCell(new Paragraph(String.valueOf(averagePattern),FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellAveragePattern.setFixedHeight(20f);
        cellAveragePattern.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellAveragePattern);

        PdfPCell cellAverageError = new PdfPCell(new Paragraph(String.valueOf(averageError),FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellAverageError.setFixedHeight(20f);
        cellAverageError.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellAverageError);

        //  Desviación estandar

        standardDeviationEquipment = Utils.calculateStandardDeviation(equipments);
        standardDeviationPattern = Utils.calculateStandardDeviation(patterns);
        standardDeviationError = Utils.calculateStandardDeviation(errors);

        PdfPCell cellDesviation = new PdfPCell(new Paragraph("DESVIACIÓN ESTÁNDAR",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellDesviation.setColspan(2);
        cellDesviation.setFixedHeight(20f);
        cellDesviation.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellDesviation);

        PdfPCell cellDeviationEquipment = new PdfPCell(new Paragraph(String.valueOf(standardDeviationEquipment),FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellDeviationEquipment.setFixedHeight(20f);
        cellDeviationEquipment.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellDeviationEquipment);

        PdfPCell cellDeviationPattern = new PdfPCell(new Paragraph(String.valueOf(standardDeviationPattern),FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellDeviationPattern.setFixedHeight(20f);
        cellDeviationPattern.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellDeviationPattern);

        PdfPCell cellDeviationError = new PdfPCell(new Paragraph(String.valueOf(standardDeviationError),FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellDeviationError.setFixedHeight(20f);
        cellDeviationError.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellDeviationError);

        equipmentTemOut = averageEquipment;
        patternTemOut = averagePattern;
        errorTemOut = averageError;
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


        PdfPCell cellPattern = new PdfPCell(new Paragraph("PATRON",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellPattern.setFixedHeight(15f);
        cellPattern.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellPattern);

        PdfPCell cellEquipment = new PdfPCell(new Paragraph("EQUIPO",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellEquipment.setFixedHeight(15f);
        cellEquipment.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellEquipment);

        PdfPCell cellError = new PdfPCell(new Paragraph("ERROR",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellError.setFixedHeight(15f);
        cellError.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellError);

        PdfPCell cellDesStand = new PdfPCell(new Paragraph("DES STAND",FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellDesStand.setFixedHeight(15f);
        cellDesStand.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellDesStand);

        PdfPCell cellPattern2 = new PdfPCell(new Paragraph(String.valueOf(pattern),FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellPattern2.setFixedHeight(15f);
        cellPattern2.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellPattern2);

        PdfPCell cellEquipment2 = new PdfPCell(new Paragraph(String.valueOf(equipment),FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK)));
        cellEquipment2.setFixedHeight(15f);
        cellEquipment2.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellEquipment2);

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


