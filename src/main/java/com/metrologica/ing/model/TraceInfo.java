package com.metrologica.ing.model;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name="trace_info")
@EntityListeners(AuditingEntityListener.class)
public class TraceInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String model;

    @Column(name="serial_number",nullable = false)
    private String serialNumber;

    @Column(name="calibration_timestamp",nullable = false)
    private long calibrationTimestamp;

    @Column(nullable = false)
    private String certificate;

    @Column(nullable = false)
    private String temperature;

    @Column(nullable = false)
    private String humity;

    public TraceInfo() {
    }

    public TraceInfo(long id, String name, String model, String serialNumber, long calibrationTimestamp, String certificate, String temperature, String humity) {
        this.id = id;
        this.name = name;
        this.model = model;
        this.serialNumber = serialNumber;
        this.calibrationTimestamp = calibrationTimestamp;
        this.certificate = certificate;
        this.temperature = temperature;
        this.humity = humity;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public long getCalibrationTimestamp() {
        return calibrationTimestamp;
    }

    public void setCalibrationTimestamp(long calibrationTimestamp) {
        this.calibrationTimestamp = calibrationTimestamp;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHumity() {
        return humity;
    }

    public void setHumity(String humity) {
        this.humity = humity;
    }
}
