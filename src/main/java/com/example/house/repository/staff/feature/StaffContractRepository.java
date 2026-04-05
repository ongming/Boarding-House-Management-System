package com.example.house.repository.staff.feature;

import com.example.house.service.staff.StaffDataStore;
import javafx.collections.ObservableList;

import java.time.LocalDate;

public interface StaffContractRepository {
    ObservableList<StaffDataStore.ContractItem> contracts();

    StaffDataStore.ContractItem addContract(String roomCode, String tenantName, double rentFee, double deposit);

    StaffDataStore.ContractItem addContractFull(String roomCode,
                                                String tenantName,
                                                String tenantCccd,
                                                String tenantPhone,
                                                LocalDate startDate,
                                                LocalDate endDate,
                                                String contractImageUrl,
                                                int occupantCount,
                                                double rentFee,
                                                double deposit);
}
