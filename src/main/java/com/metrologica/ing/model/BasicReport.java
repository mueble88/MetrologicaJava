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

    //    cascade = CascadeType.ALL
    public BasicReport() {
    }

    public BasicReport(long id, Client client, EquipmentInfo equipmentInfo, TraceInfo traceInfo) {
        this.id = id;
        this.client = client;
        this.equipmentInfo = equipmentInfo;
        this.traceInfo = traceInfo;
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
}
