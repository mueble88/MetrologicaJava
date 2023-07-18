package com.metrologica.ing.dto;

import com.metrologica.ing.model.BasicReport;
import com.metrologica.ing.model.ReportFile;

public class BasicReportAndFilesDto {

    private ReportFile reportFile;
    private BasicReport basicReport;

    public BasicReportAndFilesDto() {
    }

    public BasicReportAndFilesDto(ReportFile reportFile, BasicReport basicReport) {
        this.reportFile = reportFile;
        this.basicReport = basicReport;
    }

    public ReportFile getReportFile() {
        return reportFile;
    }

    public void setReportFile(ReportFile reportFile) {
        this.reportFile = reportFile;
    }

    public BasicReport getBasicReport() {
        return basicReport;
    }

    public void setBasicReport(BasicReport basicReport) {
        this.basicReport = basicReport;
    }
}
