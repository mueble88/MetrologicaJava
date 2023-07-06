package com.metrologica.ing.model;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Blob;
import java.util.UUID;

@Entity
@Table(name="report_files")
@EntityListeners(AuditingEntityListener.class)
public class ReportFiles {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name= "report_id",nullable = false)
    private long reportId;

    @Column(nullable = false)
    private String file;

    @Column(nullable = false)
    private String filename;


    public ReportFiles() {
    }

    public ReportFiles(UUID id, long reportId, String file, String filename) {
        this.id = id;
        this.reportId = reportId;
        this.file = file;
        this.filename = filename;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public long getReportId() {
        return reportId;
    }

    public void setReportId(long reportId) {
        this.reportId = reportId;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
