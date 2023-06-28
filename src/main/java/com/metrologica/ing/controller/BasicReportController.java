package com.metrologica.ing.controller;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.metrologica.ing.dto.BasicReportDto;
import com.metrologica.ing.dto.PdfDto;
import com.metrologica.ing.model.BasicReport;
import com.metrologica.ing.model.Client;
import com.metrologica.ing.model.EquipmentInfo;
import com.metrologica.ing.model.TraceInfo;
import com.metrologica.ing.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.awt.*;
import java.awt.Font;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class BasicReportController {

    @Autowired
    private BasicReportService basicReportService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private EquipmentInfoService equipmentInfoService;

    @Autowired
    private TraceInfoService traceInfoService;

    @Autowired
    private PDFService pdfService;

    @PostMapping("/basicReport")
    public ResponseEntity<BasicReport> saveBasicReport(@RequestBody BasicReportDto basicReportDto) throws IOException, DocumentException, ServletException {

        EquipmentInfo equipmentInfo = new EquipmentInfo();
        TraceInfo traceInfo = new TraceInfo();

        Client client = clientService.getClientById(basicReportDto.getClientId());

        equipmentInfo.setName(basicReportDto.getNameE());
        equipmentInfo.setBrand(basicReportDto.getBrand());
        equipmentInfo.setModel(basicReportDto.getModel());
        equipmentInfo.setSerialNumber(basicReportDto.getSerialNumber());
        equipmentInfo.setLocation(basicReportDto.getLocation());
        equipmentInfo.setPlate(basicReportDto.getPlate());
        equipmentInfo.setReceptionTimestamp(basicReportDto.getReceptionTimestamp());
        equipmentInfo.setCalibrationTimestamp(basicReportDto.getCalibrationTimestamp());
        equipmentInfo.setMeasure(basicReportDto.getMeasure());
        equipmentInfo.setUnity(basicReportDto.getUnity());
        equipmentInfo.setMeasureRange(basicReportDto.getMeasureRange());
        equipmentInfo.setResolution(basicReportDto.getResolution());
//        equipmentInfoService.create(equipmentInfo);

        traceInfo.setName(basicReportDto.getNameT());
        traceInfo.setModel(basicReportDto.getModelT());
        traceInfo.setSerialNumber(basicReportDto.getSerialNumberT());
        traceInfo.setCalibrationTimestamp(basicReportDto.getCalibrationTimestampT());
        traceInfo.setCertificate(basicReportDto.getCertificate());
        traceInfo.setTemperature(basicReportDto.getTemperature());
        traceInfo.setHumity(basicReportDto.getHumity());
//        traceInfoService.create(traceInfo);

        BasicReport basicReport = new BasicReport();
        basicReport.setTraceInfo(traceInfo);
        basicReport.setEquipmentInfo(equipmentInfo);
        basicReport.setClient(client);
//        basicReportService.save(basicReport);

        String nombreArchivo = "PDF"+client.getName()+".pdf";
        pdfService.savePDF( nombreArchivo, client, equipmentInfo, traceInfo);

        return new ResponseEntity<BasicReport>(basicReport, HttpStatus.OK);

//        return new ResponseEntity<BasicReport>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/reports")
    private List<BasicReport> getClients(@RequestParam(defaultValue = "0") int offset,
                                    @RequestParam(defaultValue = "10") int pageSize,
                                    @RequestParam(defaultValue = "name") String field,
                                    @RequestParam(defaultValue = "asc") String sort){

        //Page<Client> clientWithPagination = clientService.findClientWithPaginationAndSorting(offset, pageSize, field, sort);
        //return new APIResponseDto<>(clientWithPagination.getSize(), clientWithPagination);
        return basicReportService.findAll();
    }



    public static void createPDF(String PDF) throws DocumentException, FileNotFoundException {
        //se crea el documento
        Document document = new Document();

        // el outputStream para el fichero donde crearemos el pdf
        FileOutputStream ficheroPDF = new FileOutputStream("fichero.pdf");

        //se asocia el documento de outputStream
        PdfWriter.getInstance(document, ficheroPDF).setInitialLeading(100);

        //se abre el documento
        document.open();

        //se cierra el documento
        document.close();
    }



//    HttpServletRequest request, HttpServletResponse response
    public static void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get PDF file of the form parameter
        Part filePart = request.getPart("pdfFile");
        InputStream fileContent = filePart.getInputStream();
        System.out.println("nuevo pdf");

        // Create a new PDF document using itext
        Document document = new Document();
        try {
            // Especificar la ruta donde deseas guardar el archivo PDF
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("C:/Proyectos/ing/archivo.pdf"));
            document.open();

            // Leer el contenido del archivo cargado y agregarlo al documento PDF
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fileContent.read(buffer)) != -1) {
                document.add(new Paragraph(new String(buffer, 0, bytesRead)));
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }

        // Redirigir o mostrar una respuesta al usuario
        response.sendRedirect("archivo_subido.html");
    }



}
