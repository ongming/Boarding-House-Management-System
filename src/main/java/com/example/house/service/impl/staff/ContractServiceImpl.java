package com.example.house.service.impl.staff;

import com.example.house.model.dto.staff.ContractCreationRequest;
import com.example.house.model.entity.Contract;
import com.example.house.service.staff.ContractService;
import com.example.house.service.staff.StaffDomainService;
import java.time.LocalDate;

public class ContractServiceImpl implements ContractService {
    private final StaffDomainService workflow;

    public ContractServiceImpl() {
        this(new StaffDomainServiceImpl());
    }

    public ContractServiceImpl(StaffDomainService workflow) {
        this.workflow = workflow;
    }

    @Override
    public boolean isRoomAvailable(Integer roomId) {
        return workflow.isRoomAvailable(roomId);
    }

    @Override
    public Contract processContractCreation(ContractCreationRequest request) {
        return workflow.processContractCreation(request);
    }

    @Override
    public Contract updateContractMoveInDate(Integer contractId, LocalDate moveInDate) {
        return workflow.updateContractMoveInDate(contractId, moveInDate);
    }
}
