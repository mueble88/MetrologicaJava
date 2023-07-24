package com.metrologica.ing.model;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;

@Entity
@Table(name="basic_report")
@EntityListeners(AuditingEntityListener.class)
public class BasicReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "equipment_info_id")
    private EquipmentInfo equipmentInfo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "trace_info_id")
    private TraceInfo traceInfo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "report_file_id")
    private ReportFile reportFile;

    @Column(name="report_name")
    private String reportName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "report_humed_in_id")
    private HumedIn humedIn;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "report_tem_in_id")
    private TemIn temIn;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "report_tem_out_id")
    private TemOut temOut;

    public BasicReport() {
    }

    public BasicReport(long id, Client client, EquipmentInfo equipmentInfo, TraceInfo traceInfo, ReportFile reportFile, String reportName, HumedIn humedIn, TemIn temIn, TemOut temOut) {
        this.id = id;
        this.client = client;
        this.equipmentInfo = equipmentInfo;
        this.traceInfo = traceInfo;
        this.reportFile = reportFile;
        this.reportName = reportName;
        this.humedIn = humedIn;
        this.temIn = temIn;
        this.temOut = temOut;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public EquipmentInfo getEquipmentInfo() {
        return equipmentInfo;
    }

    public void setEquipmentInfo(EquipmentInfo equipmentInfo) {
        this.equipmentInfo = equipmentInfo;
    }

    public TraceInfo getTraceInfo() {
        return traceInfo;
    }

    public void setTraceInfo(TraceInfo traceInfo) {
        this.traceInfo = traceInfo;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public HumedIn getHumedIn() {
        return humedIn;
    }

    public void setHumedIn(HumedIn humedIn) {
        this.humedIn = humedIn;
    }

    public TemIn getTemIn() {
        return temIn;
    }

    public void setTemIn(TemIn temIn) {
        this.temIn = temIn;
    }

    public TemOut getTemOut() {
        return temOut;
    }

    public void setTemOut(TemOut temOut) {
        this.temOut = temOut;
    }
}
