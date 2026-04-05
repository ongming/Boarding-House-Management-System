package com.example.house.service.staff;

import com.example.house.model.dto.staff.ContractCreationRequest;
import com.example.house.model.entity.Contract;

public interface ContractService {
    boolean isRoomAvailable(Integer roomId);
    Contract processContractCreation(ContractCreationRequest request);
}
