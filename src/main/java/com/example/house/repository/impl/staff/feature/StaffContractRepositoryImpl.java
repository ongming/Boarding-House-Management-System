package com.example.house.repository.impl.staff.feature;

import com.example.house.repository.staff.feature.StaffContractRepository;
import com.example.house.service.staff.StaffDataStore;
import javafx.collections.ObservableList;

import java.time.LocalDate;

public class StaffContractRepositoryImpl implements StaffContractRepository {
    private final StaffDataStore dataStore;

    public StaffContractRepositoryImpl(StaffDataStore dataStore) {
        this.dataStore = dataStore;
    }

    @Override
    public ObservableList<StaffDataStore.ContractItem> contracts() {
        return dataStore.contracts();
    }

    @Override
    public StaffDataStore.ContractItem addContract(String roomCode, String tenantName, double rentFee, double deposit) {
        return dataStore.addContract(roomCode, tenantName, rentFee, deposit);
    }

    @Override
    public StaffDataStore.ContractItem addContractFull(String roomCode,
                                                       String tenantName,
                                                       String tenantCccd,
                                                       String tenantPhone,
                                                       LocalDate startDate,
                                                       LocalDate moveInDate,
                                                       LocalDate endDate,
                                                       String contractImageUrl,
                                                       int occupantCount,
                                                       double rentFee,
                                                       double deposit) {
        return dataStore.addContractFull(roomCode, tenantName, tenantCccd, tenantPhone,
                startDate, moveInDate, endDate, contractImageUrl, occupantCount, rentFee, deposit);
    }

    @Override
    public StaffDataStore.ContractItem updateContractMoveInDate(int contractId, LocalDate moveInDate) {
        return dataStore.updateContractMoveInDate(contractId, moveInDate);
    }
}
