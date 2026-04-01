package com.example.house.model.entity;

import com.example.house.model.enums.RoomStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "PHONG")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "so_phong", nullable = false, unique = true, length = 10)
    private String roomNumber;

    @Column(name = "tang")
    private Integer floor;

    @Column(name = "kich_thuoc", precision = 10, scale = 2)
    private BigDecimal size;

    @Column(name = "gia_thue_co_ban", nullable = false, precision = 15, scale = 2)
    private BigDecimal baseRent;

    @Lob
    @Column(name = "danh_sach_noi_that")
    private String furnitureList;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai")
    private RoomStatus status;

    public Integer getId() {
        return id;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public BigDecimal getSize() {
        return size;
    }

    public void setSize(BigDecimal size) {
        this.size = size;
    }

    public BigDecimal getBaseRent() {
        return baseRent;
    }

    public void setBaseRent(BigDecimal baseRent) {
        this.baseRent = baseRent;
    }

    public String getFurnitureList() {
        return furnitureList;
    }

    public void setFurnitureList(String furnitureList) {
        this.furnitureList = furnitureList;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }
}
