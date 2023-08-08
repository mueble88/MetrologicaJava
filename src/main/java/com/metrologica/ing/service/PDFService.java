package com.metrologica.ing.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import com.metrologica.ing.dto.HumedInDto;
import com.metrologica.ing.dto.TemInDto;
import com.metrologica.ing.dto.TemOutDto;
import com.metrologica.ing.model.*;
import com.metrologica.ing.repository.ReportFileRepository;
import com.metrologica.ing.util.FooterEvent;
import com.metrologica.ing.util.Utils;
import org.apache.commons.io.IOUtils;
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
import java.util.Date;
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

    @Autowired
    private ResourceLoader resourceLoader;

    @Value( "${pdfDirectory}" )
    private String reportDirectory;

    @Value( "${PDFDirectory}" )
    private String pdfDirectory;


    public ReportFile savePDF( BasicReport basicReport, HumedInDto humedIn, TemInDto temIn, TemOutDto temOut) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        ReportFile reportFile = new ReportFile();

        try {
            File file = new File(pdfDirectory + File.separator + basicReport.getId() + ".pdf");
            if(!file.exists()){
                file.createNewFile();
            }

            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(file));
            FooterEvent event = new FooterEvent();
            pdfWriter.setPageEvent(event);
            // Opens the document to add content and margin is created.
            document.open();
            document.setMargins(50,50,50,50);

            PdfPTable header = header();
            document.add(header);

            Paragraph lineBreakOne = new Paragraph();
            lineBreakOne.add("\n");

            Paragraph lineBreak = new Paragraph();
            lineBreak.add("\n\n");
            document.add(lineBreak);

            Paragraph lineBreaktwo = new Paragraph();
            lineBreaktwo.add("\n\n\n\n\n");

            PdfPTable nameReport = nameReport(basicReport.getReportName());
            document.add(nameReport);

            Paragraph title  = new Paragraph("Informe De Calibración Trazable",
                    FontFactory.getFont("arial",14,Font.BOLD,BaseColor.BLACK)
            );
            title .setAlignment(Chunk.ALIGN_CENTER);
            document.add(title );
            document.add(lineBreakOne);

            PdfPTable tableClient = infCliente(basicReport.getClient());
            document.add(tableClient);
            document.add(lineBreakOne);

            PdfPTable tableEquipment = infEquipment(basicReport.getEquipmentInfo());
            document.add(tableEquipment);
            document.add(lineBreakOne);

            PdfPTable tableTrace = infTrace(basicReport.getTraceInfo());
            document.add(tableTrace);
            document.add(lineBreakOne);

            String nameImg =  "parrafo.png";
            Image paragraph = loadFiel(nameImg);
            paragraph.scaleToFit(500, 100);
            paragraph.setAlignment(Chunk.ALIGN_CENTER);
            document.add(paragraph);
            document.add(lineBreakOne);
            PdfPTable firmas = firma();
            document.add(firmas);

            // 2da page --------------------------------------------------------------->
            document.newPage();
            document.add(header);
            document.add(lineBreak);
            document.add(nameReport);

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
            PdfPTable tableCalTempIn = temperatureCalibrationTableIn(basicReport.getEquipmentInfo() , basicReport.getTraceInfo());
            document.add(tableCalTempIn);
            document.add(lineBreakTable);
            PdfPTable tableCalHumedad = calibrationHumedTable(basicReport.getEquipmentInfo() , basicReport.getTraceInfo());
            document.add(tableCalHumedad);
            document.add(lineBreakTable);
            PdfPTable tableCalTemOut = temperatureCalibrationTableOut(basicReport.getEquipmentInfo() , basicReport.getTraceInfo());
            document.add(tableCalTemOut);
            document.add(lineBreaktwo);

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

            // Cierra el documento
            document.close();

            System.out.println("Archivo PDF creado exitosamente.");
        } catch (Exception e) {
            System.out.println("Error al crear el archivo PDF: " + e.getMessage());
            e.printStackTrace();
        }
        return reportFile;
    }
/*
    public ReportFile getReportFile(UUID id) {
        return reportFilesRepository.findById(id).get();
    }

    public List<ReportFile> getAllReportFile(){
        List<ReportFile> pdfs = reportFilesRepository.findAll();
        System.out.println("lista de pdf:"+pdfs);
        return pdfs;
    }*/

    public Image loadFiel(String nameImg) throws IOException, BadElementException {
        Resource resource = resourceLoader.getResource("classpath:images/"+nameImg);
        Image image = Image.getInstance( IOUtils.toByteArray( resource.getInputStream() ) );
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

    public PdfPTable nameReport(String reportName) throws DocumentException{
        PdfPTable table = new PdfPTable(2);
        table.setWidths(new float[] {50,50});

        PdfPCell cellEmpty = new PdfPCell(new Paragraph("",
                FontFactory.getFont("arial",9,Font.BOLD,BaseColor.BLACK))
        );
        cellEmpty.setBorder(0);
        table.addCell(cellEmpty);
        PdfPCell title = new PdfPCell(new Paragraph(reportName,
                FontFactory.getFont("arial",9,Font.BOLD,BaseColor.BLACK))
        );
        title.setHorizontalAlignment(Element.ALIGN_RIGHT);
        title.setBorder(0);
        table.addCell(title);

        return table;
    }

    public PdfPTable infCliente(Client client) throws DocumentException {

        int size = 8;
        Font fontBold = FontFactory.getFont("arial",size,Font.BOLD,BaseColor.BLACK);
        Font fontNormal = FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK);
        PdfPTable table = new PdfPTable(4);
        table.setWidths(new float[] {18, 27, 18, 27});
        table.setWidthPercentage(90);
        table.getDefaultCell().setBorder(0);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell title = new PdfPCell(new Paragraph("INFORMACIÓN DEL CLIENTE",
                FontFactory.getFont("arial",9,Font.BOLD,BaseColor.BLACK)));
        title.setColspan(4);
        title.setFixedHeight(14f);
        title.setBorder(0);
        title.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(title);

        if(client != null) {
            PdfPCell name = new PdfPCell(new Paragraph("Nombre Solicitante:", fontBold));
            name.setHorizontalAlignment(Element.ALIGN_LEFT);
            name.setBorder(0);
            table.addCell(name);

            PdfPCell getName = new PdfPCell(new Paragraph(client.getName(), fontNormal));
            getName.setHorizontalAlignment(Element.ALIGN_LEFT);
            getName.setBorder(0);
            table.addCell(getName);

            PdfPCell phone = new PdfPCell(new Paragraph("Teléfono:", fontBold));
            phone.setHorizontalAlignment(Element.ALIGN_LEFT);
            phone.setBorder(0);
            table.addCell(phone);

            PdfPCell getPhone = new PdfPCell(new Paragraph(client.getPhone(), fontNormal));
            getPhone.setHorizontalAlignment(Element.ALIGN_LEFT);
            getPhone.setBorder(0);
            table.addCell(getPhone);

            PdfPCell city = new PdfPCell(new Paragraph("Ciudad:", fontBold));
            city.setHorizontalAlignment(Element.ALIGN_LEFT);
            city.setBorder(0);
            table.addCell(city);

            PdfPCell getCity = new PdfPCell(new Paragraph(client.getCityName(), fontNormal));
            getCity.setHorizontalAlignment(Element.ALIGN_LEFT);
            getCity.setBorder(0);
            table.addCell(getCity);

            PdfPCell email = new PdfPCell(new Paragraph("Correo:", fontBold));
            email.setHorizontalAlignment(Element.ALIGN_LEFT);
            email.setBorder(0);
            table.addCell(email);

            PdfPCell getEmail = new PdfPCell(new Paragraph(client.getEmail(), fontNormal));
            getEmail.setHorizontalAlignment(Element.ALIGN_LEFT);
            getEmail.setBorder(0);
            table.addCell(getEmail);

            PdfPCell address = new PdfPCell(new Paragraph("Dirección:", fontBold));
            address.setHorizontalAlignment(Element.ALIGN_LEFT);
            address.setBorder(0);
            table.addCell(address);

            PdfPCell getAddress = new PdfPCell(new Paragraph(client.getAddress(), fontNormal));
            getAddress.setHorizontalAlignment(Element.ALIGN_LEFT);
            getAddress.setBorder(0);
            table.addCell(getAddress);

            PdfPCell nit = new PdfPCell(new Paragraph("NIT:", fontBold));
            nit.setHorizontalAlignment(Element.ALIGN_LEFT);
            nit.setBorder(0);
            table.addCell(nit);

            PdfPCell getNit = new PdfPCell(new Paragraph(client.getNit(), fontNormal));
            getNit.setHorizontalAlignment(Element.ALIGN_LEFT);
            getNit.setBorder(0);
            table.addCell(getNit);
        }
        return  table;
    }

    public PdfPTable infEquipment(EquipmentInfo equipmentInfo) throws DocumentException {

        int size = 8;
        Font fontBold = FontFactory.getFont("arial",size,Font.BOLD,BaseColor.BLACK);
        Font fontNormal = FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK);
        PdfPTable table = new PdfPTable(4);
        table.setWidths(new float[] {18, 27, 18, 27});
        table.setWidthPercentage(90);
        table.getDefaultCell().setBorder(0);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);

        long epoch2 = equipmentInfo.getReceptionTimestamp();
        String receptionTS = Utils.pdfFormat.format(epoch2);
        long epoch1 = equipmentInfo.getCalibrationTimestamp();
        String calibrationTS = Utils.pdfFormat.format(epoch1);

        PdfPCell title = new PdfPCell(new Paragraph("INFORMACIÓN DEL EQUIPO",
                FontFactory.getFont("arial",9,Font.BOLD,BaseColor.BLACK)));
        title.setColspan(4);
        title.setFixedHeight(14f);
        title.setBorder(0);
        title.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(title);

        PdfPCell equipo = new PdfPCell(new Paragraph("Equipo:", fontBold));
        equipo.setHorizontalAlignment(Element.ALIGN_LEFT);
        equipo.setBorder(0);
        table.addCell(equipo);

        PdfPCell getEquipo = new PdfPCell(new Paragraph(equipmentInfo.getName(), fontNormal));
        getEquipo.setHorizontalAlignment(Element.ALIGN_LEFT);
        getEquipo.setBorder(0);
        table.addCell(getEquipo);

        PdfPCell dateR = new PdfPCell(new Paragraph("Fecha De Recepción:", fontBold));
        dateR.setHorizontalAlignment(Element.ALIGN_LEFT);
        dateR.setBorder(0);
        table.addCell(dateR);

        PdfPCell getDateR = new PdfPCell(new Paragraph(receptionTS, fontNormal));
        getDateR.setHorizontalAlignment(Element.ALIGN_LEFT);
        getDateR.setBorder(0);
        table.addCell(getDateR);

        PdfPCell marca = new PdfPCell(new Paragraph("Marca:", fontBold));
        marca.setHorizontalAlignment(Element.ALIGN_LEFT);
        marca.setBorder(0);
        table.addCell(marca);

        PdfPCell getBrand = new PdfPCell(new Paragraph(equipmentInfo.getBrand(), fontNormal));
        getBrand.setHorizontalAlignment(Element.ALIGN_LEFT);
        getBrand.setBorder(0);
        table.addCell(getBrand);

        PdfPCell dateC = new PdfPCell(new Paragraph("Fecha De Calibración:", fontBold));
        dateC.setHorizontalAlignment(Element.ALIGN_LEFT);
        dateC.setBorder(0);
        table.addCell(dateC);

        PdfPCell getDateC = new PdfPCell(new Paragraph(calibrationTS, fontNormal));
        getDateC.setHorizontalAlignment(Element.ALIGN_LEFT);
        getDateC.setBorder(0);
        table.addCell(getDateC);

        PdfPCell modelo = new PdfPCell(new Paragraph("Modelo:", fontBold));
        modelo.setHorizontalAlignment(Element.ALIGN_LEFT);
        modelo.setBorder(0);
        table.addCell(modelo);

        PdfPCell getModelo = new PdfPCell(new Paragraph(equipmentInfo.getModel(), fontNormal));
        getModelo.setHorizontalAlignment(Element.ALIGN_LEFT);
        getModelo.setBorder(0);
        table.addCell(getModelo);

        PdfPCell measure = new PdfPCell(new Paragraph("Magnitud A Medir:", fontBold));
        measure.setHorizontalAlignment(Element.ALIGN_LEFT);
        measure.setBorder(0);
        table.addCell(measure);

        PdfPCell getMeasure = new PdfPCell(new Paragraph(equipmentInfo.getMeasure(), fontNormal));
        getMeasure.setHorizontalAlignment(Element.ALIGN_LEFT);
        getMeasure.setBorder(0);
        table.addCell(getMeasure);

        PdfPCell serie = new PdfPCell(new Paragraph("Serie:", fontBold));
        serie.setHorizontalAlignment(Element.ALIGN_LEFT);
        serie.setBorder(0);
        table.addCell(serie);

        PdfPCell getSerial = new PdfPCell(new Paragraph(equipmentInfo.getSerialNumber(), fontNormal));
        getSerial.setHorizontalAlignment(Element.ALIGN_LEFT);
        getSerial.setBorder(0);
        table.addCell(getSerial);

        PdfPCell unity = new PdfPCell(new Paragraph("Unidad:", fontBold));
        unity.setHorizontalAlignment(Element.ALIGN_LEFT);
        unity.setBorder(0);
        table.addCell(unity);

        PdfPCell getUnity = new PdfPCell(new Paragraph(equipmentInfo.getUnity(), fontNormal));
        getUnity.setHorizontalAlignment(Element.ALIGN_LEFT);
        getUnity.setBorder(0);
        table.addCell(getUnity);

        PdfPCell placa = new PdfPCell(new Paragraph("Placa:", fontBold));
        placa.setHorizontalAlignment(Element.ALIGN_LEFT);
        placa.setBorder(0);
        table.addCell(placa);

        PdfPCell getPlate = new PdfPCell(new Paragraph(equipmentInfo.getPlate(), fontNormal));
        getPlate.setHorizontalAlignment(Element.ALIGN_LEFT);
        getPlate.setBorder(0);
        table.addCell(getPlate);

        PdfPCell range = new PdfPCell(new Paragraph("Rango De Medición:", fontBold));
        range.setHorizontalAlignment(Element.ALIGN_LEFT);
        range.setBorder(0);
        table.addCell(range);

        PdfPCell getRange = new PdfPCell(new Paragraph(equipmentInfo.getMeasureRange(), fontNormal));
        getRange.setHorizontalAlignment(Element.ALIGN_LEFT);
        getRange.setBorder(0);
        table.addCell(getRange);

        PdfPCell location = new PdfPCell(new Paragraph("Ubicación:", fontBold));
        location.setHorizontalAlignment(Element.ALIGN_LEFT);
        location.setBorder(0);
        table.addCell(location);

        PdfPCell getLocation = new PdfPCell(new Paragraph(equipmentInfo.getLocation(), fontNormal));
        getLocation.setHorizontalAlignment(Element.ALIGN_LEFT);
        getLocation.setBorder(0);
        table.addCell(getLocation);

        PdfPCell resolution = new PdfPCell(new Paragraph("Resolución", fontBold));
        resolution.setHorizontalAlignment(Element.ALIGN_LEFT);
        resolution.setBorder(0);
        table.addCell(resolution);

        PdfPCell getResolution = new PdfPCell(new Paragraph(equipmentInfo.getResolution(), fontNormal));
        getResolution.setHorizontalAlignment(Element.ALIGN_LEFT);
        getResolution.setBorder(0);
        table.addCell(getResolution);

        return  table;
    }

    public PdfPTable infTrace(TraceInfo traceInfo) throws DocumentException {

        int size = 8;
        Font fontBold = FontFactory.getFont("arial",size,Font.BOLD,BaseColor.BLACK);
        Font fontNormal = FontFactory.getFont("arial",size,Font.NORMAL,BaseColor.BLACK);
        PdfPTable table = new PdfPTable(4);
        table.setWidths(new float[] {18, 27, 18, 27});
        table.setWidthPercentage(90);
        table.getDefaultCell().setBorder(0);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);

        long epoch1 = traceInfo.getCalibrationTimestamp();
        String calibrationTS = Utils.pdfFormat.format(epoch1);

        PdfPCell title = new PdfPCell(new Paragraph("INFORMACIÓN TRAZABILIDAD",
                FontFactory.getFont("arial",9,Font.BOLD,BaseColor.BLACK)));
        title.setColspan(4);
        title.setFixedHeight(14f);
        title.setBorder(0);
        title.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(title);

        PdfPCell name = new PdfPCell(new Paragraph("Equipo:", fontBold));
        name.setHorizontalAlignment(Element.ALIGN_LEFT);
        name.setBorder(0);
        table.addCell(name);

        PdfPCell getName = new PdfPCell(new Paragraph(traceInfo.getName(), fontNormal));
        getName.setHorizontalAlignment(Element.ALIGN_LEFT);
        getName.setBorder(0);
        table.addCell(getName);

        PdfPCell empty = new PdfPCell(new Paragraph("", fontBold));
        empty.setHorizontalAlignment(Element.ALIGN_LEFT);
        empty.setBorder(0);
        table.addCell(empty);
        table.addCell(empty);

        PdfPCell model = new PdfPCell(new Paragraph("Modelo:", fontBold));
        model.setHorizontalAlignment(Element.ALIGN_LEFT);
        model.setBorder(0);
        table.addCell(model);

        PdfPCell getModel = new PdfPCell(new Paragraph(traceInfo.getModel(), fontNormal));
        getModel.setHorizontalAlignment(Element.ALIGN_LEFT);
        getModel.setBorder(0);
        table.addCell(getModel);
        table.addCell(empty);
        table.addCell(empty);

        PdfPCell serial = new PdfPCell(new Paragraph("Serie:", fontBold));
        serial.setHorizontalAlignment(Element.ALIGN_LEFT);
        serial.setBorder(0);
        table.addCell(serial);

        PdfPCell getSerial = new PdfPCell(new Paragraph(traceInfo.getSerialNumber(), fontNormal));
        getSerial.setHorizontalAlignment(Element.ALIGN_LEFT);
        getSerial.setBorder(0);
        table.addCell(getSerial);
        table.addCell(empty);
        table.addCell(empty);

        PdfPCell date = new PdfPCell(new Paragraph("Fecha de Calibración Patrón:", fontBold));
        date.setHorizontalAlignment(Element.ALIGN_LEFT);
        date.setBorder(0);
        table.addCell(date);

        PdfPCell getCalibrationTS = new PdfPCell(new Paragraph(calibrationTS, fontNormal));
        getCalibrationTS.setHorizontalAlignment(Element.ALIGN_LEFT);
        getCalibrationTS.setBorder(0);
        table.addCell(getCalibrationTS);
        table.addCell(empty);
        table.addCell(empty);

        PdfPCell certificate = new PdfPCell(new Paragraph("Certificado:", fontBold));
        certificate.setHorizontalAlignment(Element.ALIGN_LEFT);
        certificate.setBorder(0);
        table.addCell(certificate);

        PdfPCell getCertificate = new PdfPCell(new Paragraph(traceInfo.getCertificate(), fontNormal));
        getCertificate.setHorizontalAlignment(Element.ALIGN_LEFT);
        getCertificate.setBorder(0);
        table.addCell(getCertificate);
        table.addCell(empty);
        table.addCell(empty);

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


