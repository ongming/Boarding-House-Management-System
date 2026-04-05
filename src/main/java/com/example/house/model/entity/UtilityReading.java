package com.example.house.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "CHI_SO_DIEN_NUOC")
public class UtilityReading {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "phong_id")
    private Room room;

    @Column(name = "thang", nullable = false)
    private Integer month;

    @Column(name = "nam", nullable = false)
    private Integer year;

    @Column(name = "so_dien_cu", nullable = false)
    private Integer oldElectricNumber;

    @Column(name = "so_dien_moi", nullable = false)
    private Integer newElectricNumber;

    @Column(name = "so_nuoc_cu", nullable = false)
    private Integer oldWaterNumber;

    @Column(name = "so_nuoc_moi", nullable = false)
    private Integer newWaterNumber;

    @Column(name = "ngay_ghi")
    private LocalDateTime recordedAt;

    public Integer getId() {
        return id;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getOldElectricNumber() {
        return oldElectricNumber;
    }

    public void setOldElectricNumber(Integer oldElectricNumber) {
        this.oldElectricNumber = oldElectricNumber;
    }

    public Integer getNewElectricNumber() {
        return newElectricNumber;
    }

    public void setNewElectricNumber(Integer newElectricNumber) {
        this.newElectricNumber = newElectricNumber;
    }

    public Integer getOldWaterNumber() {
        return oldWaterNumber;
    }

    public void setOldWaterNumber(Integer oldWaterNumber) {
        this.oldWaterNumber = oldWaterNumber;
    }

    public Integer getNewWaterNumber() {
        return newWaterNumber;
    }

    public void setNewWaterNumber(Integer newWaterNumber) {
        this.newWaterNumber = newWaterNumber;
    }

    public LocalDateTime getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(LocalDateTime recordedAt) {
        this.recordedAt = recordedAt;
    }
}

