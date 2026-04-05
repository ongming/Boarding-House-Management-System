package com.example.house.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "KHACH_THUE")
public class Tenant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ho_ten", nullable = false, length = 100)
    private String fullName;

    @Column(name = "so_cccd", nullable = false, unique = true, length = 20)
    private String cccdNumber;

    @Column(name = "so_dien_thoai", length = 15)
    private String phoneNumber;

    public Integer getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCccdNumber() {
        return cccdNumber;
    }

    public void setCccdNumber(String cccdNumber) {
        this.cccdNumber = cccdNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}

