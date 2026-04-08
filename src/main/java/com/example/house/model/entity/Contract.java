package com.example.house.model.entity;

import com.example.house.model.enums.ContractStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "HOP_DONG")
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "khach_thue_id")
    private Tenant tenant;

    @ManyToOne
    @JoinColumn(name = "phong_id")
    private Room room;

    @Column(name = "ngay_bat_dau", nullable = false)
    private LocalDate startDate;

    @Column(name = "ngay_don_vao")
    private LocalDate moveInDate;

    @Column(name = "ngay_ket_thuc")
    private LocalDate endDate;

    @Column(name = "tien_coc", precision = 15, scale = 2)
    private BigDecimal deposit;

    @Column(name = "anh_hop_dong_url", length = 255)
    private String contractImageUrl;

    @Column(name = "so_luong_nguoi_o")
    private Integer occupantCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai")
    private ContractStatus status;

    public Integer getId() {
        return id;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getMoveInDate() {
        return moveInDate;
    }

    public void setMoveInDate(LocalDate moveInDate) {
        this.moveInDate = moveInDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getDeposit() {
        return deposit;
    }

    public void setDeposit(BigDecimal deposit) {
        this.deposit = deposit;
    }

    public String getContractImageUrl() {
        return contractImageUrl;
    }

    public void setContractImageUrl(String contractImageUrl) {
        this.contractImageUrl = contractImageUrl;
    }

    public Integer getOccupantCount() {
        return occupantCount;
    }

    public void setOccupantCount(Integer occupantCount) {
        this.occupantCount = occupantCount;
    }

    public ContractStatus getStatus() {
        return status;
    }

    public void setStatus(ContractStatus status) {
        this.status = status;
    }
}
