package com.metrologica.ing.controller;


import com.itextpdf.text.*;
import com.metrologica.ing.dto.*;
import com.metrologica.ing.model.*;
import com.metrologica.ing.service.*;
import com.metrologica.ing.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.ByteArrayResource;

import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.nio.file.Path;
import java.nio.file.Paths;

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

    private String EXTENSION = ".pdf";

    @Value( "${pdfDirectory}" )
    private String reportDirectory;

    @PostMapping("/basicReport")
    public ResponseEntity<BasicReportAndFilesDto> saveBasicReport(@RequestBody BasicReportDto basicReportDto) throws IOException, DocumentException, ServletException {

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
        equipmentInfoService.save(equipmentInfo);

        traceInfo.setName(basicReportDto.getNameT());
        traceInfo.setModel(basicReportDto.getModelT());
        traceInfo.setSerialNumber(basicReportDto.getSerialNumberT());
        traceInfo.setCalibrationTimestamp(basicReportDto.getCalibrationTimestampT());
        traceInfo.setCertificate(basicReportDto.getCertificate());
        traceInfo.setTemperature(basicReportDto.getTemperature());
        traceInfo.setHumity(basicReportDto.getHumity());
        traceInfoService.save(traceInfo);

        HumedInDto humedIn = new HumedInDto();
        humedIn.setMeasures(basicReportDto.getHumedIn().getMeasures());
        TemInDto temIn = new TemInDto();
        temIn.setMeasures(basicReportDto.getTemIn().getMeasures());
        TemOutDto temOut = new TemOutDto();
        temOut.setMeasures(basicReportDto.getTemOut().getMeasures());

        String nameFile = basicReportDto.getReportName() +"-"+ Utils.sdf.format(new Date())+ " Termohigrometro(H-IN-OUT).pdf";
        BasicReport basicReport = new BasicReport();
        basicReport.setTraceInfo(traceInfo);
        basicReport.setEquipmentInfo(equipmentInfo);
        basicReport.setClient(client);
        basicReport.setReportName(nameFile);
        basicReportService.save(basicReport);

        long idBasicReport = basicReport.getId();
        System.out.println(idBasicReport);

        ReportFile reportFile = pdfService.savePDF(nameFile, client, equipmentInfo, traceInfo, humedIn, temIn, temOut, basicReport.getId());
        BasicReportAndFilesDto basicReportAndFilesDto = new BasicReportAndFilesDto();
        basicReportAndFilesDto.setBasicReport(basicReport);
        basicReportAndFilesDto.setReportFile(reportFile);
        return new ResponseEntity<BasicReportAndFilesDto>(basicReportAndFilesDto, HttpStatus.OK);

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

    @GetMapping("/reportFiles")
    private ResponseEntity<List<ReportFile>> allReportFiles(){
        List<ReportFile> pdfs = pdfService.getAllReportFile();
        return ResponseEntity
                .ok()
                .body(pdfs);
    }

    /*
    @GetMapping("/reportFile/{id}")
    private String getReportFile(@PathVariable UUID id) {
        List<ReportFile> reports = pdfService.getAllReportFile();
        for(int i=0; i < reports.size(); i++){
            if(reports.get(i).getId().compareTo(id) == 0){
                ReportFile reportFile = reports.get(i);

                return reportFile.getFile();
            }
        }
//        ReportFile reportFile = pdfService.getReportFile(id);

        return "PDF no encontrado";
    }*/


    @GetMapping("/download/reportFile/{UUID}")
    public ResponseEntity<Resource> downloadPdf(@PathVariable("UUID") UUID uuid) throws IOException {

        ReportFile reportFile = new ReportFile();
        List<ReportFile> reports = pdfService.getAllReportFile();
        String message = "";

        for (int i = 0; i < reports.size(); i++) {
            if (reports.get(i).getId().compareTo(uuid) == 0) {
                reportFile = reports.get(i);
                long namePdf = reportFile.getReportId();
                File file = new File(reportDirectory + File.separator + namePdf+".pdf");
//                File file = new File(reportDirectory + File.separator + reportFile.getFilename());
                HttpHeaders header = new HttpHeaders();

                Path path = Paths.get(file.getAbsolutePath());
                ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

                return ResponseEntity.ok()
                        .headers(header)
                        .contentLength(file.length())
                        .contentType(MediaType.APPLICATION_PDF)
                        .body(resource);
            }else{
                message = "reporte no encontrado";
                System.out.println(message);
            }
        }
        return null;
    }



}
