package com.metrologica.ing.controller;


import com.itextpdf.text.*;
import com.metrologica.ing.dto.*;
import com.metrologica.ing.model.*;
import com.metrologica.ing.service.*;
import com.metrologica.ing.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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

        BasicReport basicReport = new BasicReport();
        basicReport.setReportName(basicReportDto.getReportName());
        basicReport.setTraceInfo(traceInfo);
        basicReport.setEquipmentInfo(equipmentInfo);
        basicReport.setClient(client);
        basicReportService.save(basicReport);

        String nameFile = basicReportDto.getReportName() +"-"+ Utils.sdf.format(new Date())+ " Termohigrometro(H-IN-OUT).pdf";
        pdfService.savePDF(nameFile, client, equipmentInfo, traceInfo, humedIn, temIn, temOut, basicReport.getId());

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

    @GetMapping("/reportFiles")
    private ResponseEntity<List<ReportFile>> allReportFiles(){
        List<ReportFile> pdfs = pdfService.getAllReportFile();
        return ResponseEntity
                .ok()
                .body(pdfs);
    }

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
    }

}
