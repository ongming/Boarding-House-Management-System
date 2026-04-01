package com.example.house.model.entity;

import com.example.house.model.enums.AccountType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "TAI_KHOAN")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ten_dang_nhap", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "mat_khau", nullable = false, length = 255)
    private String password;

    @Column(name = "ho_ten", length = 100)
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(name = "loai_tai_khoan", nullable = false)
    private AccountType accountType;

    @OneToOne(mappedBy = "account")
    private Employee employee;

    public Account() {
    }

    public Account(Integer id, String username, String password, String fullName, AccountType accountType) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.accountType = accountType;
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
