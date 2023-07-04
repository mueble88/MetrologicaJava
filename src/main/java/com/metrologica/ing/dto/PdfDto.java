package com.metrologica.ing.dto;

import org.w3c.dom.ls.LSOutput;

public class PdfDto {

    private String nameClient;
    private String nit;
    private String nameEquipmentInfo;
    private String brand;
    private String model;
    private String serialNumber;
    private String location;
    private String plate;
    private long receptionTimestamp;
    private long calibrationTimestamp;
    private String measure;
    private String unity;
    private String measureRange;
    private String resolution;
    private String nameTraceInfo;
    private String modelTrace;
    private String serialNumberTrace;
    private long calibrationTimestampTrace;
    private String certificate;
    private String temperature;
    private String humity;

    public PdfDto() {
    }

    public PdfDto(String nameClient, String nit, String nameEquipmentInfo, String brand, String model, String serialNumber, String location, String plate, long receptionTimestamp, long calibrationTimestamp, String measure, String unity, String measureRange, String resolution, String nameTraceInfo, String modelTrace, String serialNumberTrace, long calibrationTimestampTrace, String certificate, String temperature, String humity) {
        this.nameClient = nameClient;
        this.nit = nit;
        this.nameEquipmentInfo = nameEquipmentInfo;
        this.brand = brand;
        this.model = model;
        this.serialNumber = serialNumber;
        this.location = location;
        this.plate = plate;
        this.receptionTimestamp = receptionTimestamp;
        this.calibrationTimestamp = calibrationTimestamp;
        this.measure = measure;
        this.unity = unity;
        this.measureRange = measureRange;
        this.resolution = resolution;
        this.nameTraceInfo = nameTraceInfo;
        this.modelTrace = modelTrace;
        this.serialNumberTrace = serialNumberTrace;
        this.calibrationTimestampTrace = calibrationTimestampTrace;
        this.certificate = certificate;
        this.temperature = temperature;
        this.humity = humity;
    }

    public String getNameClient() {
        return nameClient;
    }

    public void setNameClient(String nameClient) {
        this.nameClient = nameClient;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getNameEquipmentInfo() {
        return nameEquipmentInfo;
    }

    public void setNameEquipmentInfo(String nameEquipmentInfo) {
        this.nameEquipmentInfo = nameEquipmentInfo;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public long getReceptionTimestamp() {
        return receptionTimestamp;
    }

    public void setReceptionTimestamp(long receptionTimestamp) {
        this.receptionTimestamp = receptionTimestamp;
    }

    public long getCalibrationTimestamp() {
        return calibrationTimestamp;
    }

    public void setCalibrationTimestamp(long calibrationTimestamp) {
        this.calibrationTimestamp = calibrationTimestamp;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getUnity() {
        return unity;
    }

    public void setUnity(String unity) {
        this.unity = unity;
    }

    public String getMeasureRange() {
        return measureRange;
    }

    public void setMeasureRange(String measureRange) {
        this.measureRange = measureRange;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getNameTraceInfo() {
        return nameTraceInfo;
    }

    public void setNameTraceInfo(String nameTraceInfo) {
        this.nameTraceInfo = nameTraceInfo;
    }

    public String getModelTrace() {
        return modelTrace;
    }

    public void setModelTrace(String modelTrace) {
        this.modelTrace = modelTrace;
    }

    public String getSerialNumberTrace() {
        return serialNumberTrace;
    }

    public void setSerialNumberTrace(String serialNumberTrace) {
        this.serialNumberTrace = serialNumberTrace;
    }

    public long getCalibrationTimestampTrace() {
        return calibrationTimestampTrace;
    }

    public void setCalibrationTimestampTrace(long calibrationTimestampTrace) {
        this.calibrationTimestampTrace = calibrationTimestampTrace;
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
