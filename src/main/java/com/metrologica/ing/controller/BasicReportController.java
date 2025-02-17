package com.metrologica.ing.controller;


import com.itextpdf.text.*;
import com.metrologica.ing.dto.*;
import com.metrologica.ing.model.*;
import com.metrologica.ing.repository.MeasureHolderRepository;
import com.metrologica.ing.repository.MeasuresRepository;
import com.metrologica.ing.service.*;
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
import java.util.Arrays;
import java.util.List;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:8080")
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

    @Autowired
    private MeasureHolderService measureHolderService;

    @Autowired
    private MeasuresService measuresService;

    private String EXTENSION = ".pdf";

    @Value( "${pdfDirectory}" )
    private String reportDirectory;

    @Value( "${PDFDirectory}" )
    private String pdfDirectory;


    @PostMapping("/basicReport")
    public ResponseEntity<BasicReport> saveBasicReport(@RequestBody BasicReportDto basicReportDto) throws IOException, DocumentException, ServletException {

        EquipmentInfo equipmentInfo = new EquipmentInfo();
        TraceInfo traceInfo = new TraceInfo();

        Client client = clientService.getClientById(basicReportDto.getClientId());

        if(client != null){
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

            MeasureHolder measureHolderHI = new MeasureHolder();
            measureHolderHI.setMeasures(Arrays.asList(basicReportDto.getHumedIn().getMeasures()));
            measureHolderService.save(measureHolderHI);

            MeasureHolder measureHolderTI = new MeasureHolder();
            measureHolderTI.setMeasures(Arrays.asList(basicReportDto.getTemIn().getMeasures()));
            measureHolderService.save(measureHolderTI);

            MeasureHolder measureHolderTO = new MeasureHolder();
            measureHolderTO.setMeasures(Arrays.asList(basicReportDto.getTemOut().getMeasures()));
            measureHolderService.save(measureHolderTO);

            HumedInDto humedI = new HumedInDto();
            humedI.setMeasures(basicReportDto.getHumedIn().getMeasures());
            TemInDto temI = new TemInDto();
            temI.setMeasures(basicReportDto.getTemIn().getMeasures());
            TemOutDto temO = new TemOutDto();
            temO.setMeasures(basicReportDto.getTemOut().getMeasures());

            BasicReport basicReport = new BasicReport();
            basicReport.setTraceInfo(traceInfo);
            basicReport.setEquipmentInfo(equipmentInfo);
            basicReport.setClient(client);
            basicReport.setReportName(basicReportDto.getReportName());
            basicReport.setHumedIn(measureHolderHI);
            basicReport.setTemIn(measureHolderTI);
            basicReport.setTemOut(measureHolderTO);
            basicReportService.save(basicReport);

//            String nameFile = basicReportDto.getReportName() +"-"+ Utils.sdf.format(new Date())+ " Termohigrometro(H-IN-OUT).pdf";
            pdfService.savePDF(basicReport, humedI, temI, temO);
            return new ResponseEntity<BasicReport>(basicReport, HttpStatus.OK);
        }
        return new ResponseEntity<BasicReport>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/reports")
    private List<BasicReport> getReports(@RequestParam(defaultValue = "0") int offset,
                                    @RequestParam(defaultValue = "10") int pageSize,
                                    @RequestParam(defaultValue = "name") String field,
                                    @RequestParam(defaultValue = "asc") String sort){

        return basicReportService.findAllOrderByIdDesc();
    }


/*
    @GetMapping("/reportFiles")
    private ResponseEntity<List<ReportFile>> allReportFiles(){
        List<ReportFile> pdfs = pdfService.getAllReportFile();
        return ResponseEntity
                .ok()
                .body(pdfs);
    }*/

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


    @GetMapping("/download/reportFile/{id}")
    public ResponseEntity<Resource> downloadPdf(@PathVariable("id") Long id) throws IOException {

        File file = new File(pdfDirectory + File.separator + id+".pdf");

        if(file.exists()){
            HttpHeaders header = new HttpHeaders();
            Path path = Paths.get(file.getAbsolutePath());
            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
            return ResponseEntity.ok()
                    .headers(header)
                    .contentLength(file.length())
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
        }else{
            System.out.println("File does not exist");
        }

        return null;
    }



}
