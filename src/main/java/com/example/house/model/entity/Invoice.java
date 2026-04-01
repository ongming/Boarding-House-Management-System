package com.example.house.model.entity;

import com.example.house.model.enums.InvoicePaymentMethod;
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
import java.time.LocalDateTime;

@Entity
@Table(name = "HOA_DON")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "hop_dong_id")
    private Contract contract;

    @ManyToOne
    @JoinColumn(name = "nhan_vien_id")
    private Employee employee;

    @Column(name = "thang", nullable = false)
    private Integer month;

    @Column(name = "nam", nullable = false)
    private Integer year;

    @Column(name = "tien_phong", precision = 15, scale = 2)
    private BigDecimal roomFee;

    @Column(name = "tien_dien", precision = 15, scale = 2)
    private BigDecimal electricFee;

    @Column(name = "tien_nuoc", precision = 15, scale = 2)
    private BigDecimal waterFee;

    @Column(name = "tien_xe", precision = 15, scale = 2)
    private BigDecimal parkingFee;

    @Column(name = "tien_rac", precision = 15, scale = 2)
    private BigDecimal garbageFee;

    @Column(name = "tong_tien", nullable = false, precision = 15, scale = 2)
    private BigDecimal total;

    @Column(name = "da_thanh_toan")
    private Boolean paid;

    @Enumerated(EnumType.STRING)
    @Column(name = "hinh_thuc_thanh_toan")
    private InvoicePaymentMethod paymentMethod;

    @Column(name = "ngay_thanh_toan")
    private LocalDateTime paidAt;

    @Column(name = "ghi_chu")
    private String note;

    @Column(name = "ngay_lap")
    private LocalDateTime createdAt;

    public Integer getId() {
        return id;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
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

    public BigDecimal getRoomFee() {
        return roomFee;
    }

    public void setRoomFee(BigDecimal roomFee) {
        this.roomFee = roomFee;
    }

    public BigDecimal getElectricFee() {
        return electricFee;
    }

    public void setElectricFee(BigDecimal electricFee) {
        this.electricFee = electricFee;
    }

    public BigDecimal getWaterFee() {
        return waterFee;
    }

    public void setWaterFee(BigDecimal waterFee) {
        this.waterFee = waterFee;
    }

    public BigDecimal getParkingFee() {
        return parkingFee;
    }

    public void setParkingFee(BigDecimal parkingFee) {
        this.parkingFee = parkingFee;
    }

    public BigDecimal getGarbageFee() {
        return garbageFee;
    }

    public void setGarbageFee(BigDecimal garbageFee) {
        this.garbageFee = garbageFee;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public InvoicePaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(InvoicePaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
