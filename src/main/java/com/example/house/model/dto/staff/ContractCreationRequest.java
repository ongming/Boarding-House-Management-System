package com.example.house.model.dto.staff;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ContractCreationRequest {
    private final String roomNumber;
    private final String tenantName;
    private final String tenantCccd;
    private final String tenantPhone;
    private final BigDecimal deposit;
    private final BigDecimal roomFee;
    private final LocalDate startDate;
    private final LocalDate moveInDate;
    private final LocalDate endDate;
    private final String contractImageUrl;
    private final Integer occupantCount;
    private final String vehicleType;
    private final String vehiclePlate;

    public ContractCreationRequest(String roomNumber,
                                   String tenantName,
                                   String tenantCccd,
                                   String tenantPhone,
                                   BigDecimal deposit,
                                   BigDecimal roomFee,
                                   LocalDate startDate,
                                   LocalDate moveInDate,
                                   LocalDate endDate,
                                   String contractImageUrl,
                                   Integer occupantCount,
                                   String vehicleType,
                                   String vehiclePlate) {
        this.roomNumber = roomNumber;
        this.tenantName = tenantName;
        this.tenantCccd = tenantCccd;
        this.tenantPhone = tenantPhone;
        this.deposit = deposit;
        this.roomFee = roomFee;
        this.startDate = startDate;
        this.moveInDate = moveInDate;
        this.endDate = endDate;
        this.contractImageUrl = contractImageUrl;
        this.occupantCount = occupantCount;
        this.vehicleType = vehicleType;
        this.vehiclePlate = vehiclePlate;
    }

    public String roomNumber() { return roomNumber; }
    public String tenantName() { return tenantName; }
    public String tenantCccd() { return tenantCccd; }
    public String tenantPhone() { return tenantPhone; }
    public BigDecimal deposit() { return deposit; }
    public BigDecimal roomFee() { return roomFee; }
    public LocalDate startDate() { return startDate; }
    public LocalDate moveInDate() { return moveInDate; }
    public LocalDate endDate() { return endDate; }
    public String contractImageUrl() { return contractImageUrl; }
    public Integer occupantCount() { return occupantCount; }
    public String vehicleType() { return vehicleType; }
    public String vehiclePlate() { return vehiclePlate; }
}
