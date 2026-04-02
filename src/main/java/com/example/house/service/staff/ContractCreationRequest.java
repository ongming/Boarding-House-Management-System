package com.example.house.service.staff;
import java.math.BigDecimal;
import java.time.LocalDate;
public record ContractCreationRequest(
        String roomNumber,
        String tenantName,
        String tenantCccd,
        String tenantPhone,
        BigDecimal deposit,
        BigDecimal roomFee,
        LocalDate startDate,
        Integer occupantCount,
        String vehicleType,
        String vehiclePlate
) {
}