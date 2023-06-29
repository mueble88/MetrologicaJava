package com.metrologica.ing.dto;

import com.metrologica.ing.model.Measures;
import com.sun.xml.bind.v2.model.core.ID;

public class BasicReportDto {

//    private String nameClient;
//    private String address;
//    private String email;
//    private String nit;

    private long clientId;
    private String nameE;
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
    private String nameT;
    private String modelT;
    private String serialNumberT;
    private long calibrationTimestampT;
    private String certificate;
    private String temperature;
    private String humity;
    private HumedInDto humedIn;
    private TemInDto temIn;
    private TemOutDto temOut;


    public BasicReportDto() {
    }

    public BasicReportDto(long clientId, String nameE, String brand, String model, String serialNumber, String location, String plate, long receptionTimestamp, long calibrationTimestamp, String measure, String unity, String measureRange, String resolution, String nameT, String modelT, String serialNumberT, long calibrationTimestampT, String certificate, String temperature, String humity, HumedInDto humedIn, TemInDto temIn, TemOutDto temOut) {
        this.clientId = clientId;
        this.nameE = nameE;
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
        this.nameT = nameT;
        this.modelT = modelT;
        this.serialNumberT = serialNumberT;
        this.calibrationTimestampT = calibrationTimestampT;
        this.certificate = certificate;
        this.temperature = temperature;
        this.humity = humity;
        this.humedIn = humedIn;
        this.temIn = temIn;
        this.temOut = temOut;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public String getNameE() {
        return nameE;
    }

    public void setNameE(String nameE) {
        this.nameE = nameE;
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

    public String getNameT() {
        return nameT;
    }

    public void setNameT(String nameT) {
        this.nameT = nameT;
    }

    public String getModelT() {
        return modelT;
    }

    public void setModelT(String modelT) {
        this.modelT = modelT;
    }

    public String getSerialNumberT() {
        return serialNumberT;
    }

    public void setSerialNumberT(String serialNumberT) {
        this.serialNumberT = serialNumberT;
    }

    public long getCalibrationTimestampT() {
        return calibrationTimestampT;
    }

    public void setCalibrationTimestampT(long calibrationTimestampT) {
        this.calibrationTimestampT = calibrationTimestampT;
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

    public HumedInDto getHumedIn() {
        return humedIn;
    }

    public void setHumedIn(HumedInDto humedIn) {
        this.humedIn = humedIn;
    }

    public TemInDto getTemIn() {
        return temIn;
    }

    public void setTemIn(TemInDto temIn) {
        this.temIn = temIn;
    }

    public TemOutDto getTemOut() {
        return temOut;
    }

    public void setTemOut(TemOutDto temOut) {
        this.temOut = temOut;
    }
}
