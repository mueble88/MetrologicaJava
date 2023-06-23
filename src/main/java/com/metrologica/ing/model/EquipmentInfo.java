package com.metrologica.ing.model;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name="equipment_info")
@EntityListeners(AuditingEntityListener.class)
public class EquipmentInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    @Column(name="serial_number",nullable = false)
    private String serialNumber;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String plate;

    @Column(name="reception_timestamp",nullable = false)
    private long receptionTimestamp;

    @Column(name="calibration_timestamp",nullable = false)
    private long calibrationTimestamp;

    @Column(nullable = false)
    private String measure;

    @Column(nullable = false)
    private String unity;

    @Column(name="measure_range",nullable = false)
    private String measureRange;

    @Column(nullable = false)
    private String resolution;

    public EquipmentInfo() {
    }

    public EquipmentInfo(long id, String name, String brand, String model, String serialNumber, String location, String plate, long receptionTimestamp, long calibrationTimestamp, String measure, String unity, String measureRange, String resolution) {
        this.id = id;
        this.name = name;
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
}
