package com.example.house.controller.staff;

import com.example.house.controller.shared.ControllerInputParser;
import com.example.house.service.staff.StaffDataStore;
import com.example.house.service.staff.StaffService;
import javafx.collections.ObservableList;

import java.time.LocalDate;

public class ContractController {
    private final StaffService service;

    public ContractController(StaffService service) {
        this.service = service;
    }

    public ObservableList<StaffDataStore.ContractItem> contracts() {
        return service.contracts();
    }

    public void createContract(String roomCode,
                               String tenantName,
                               String tenantCccd,
                               String tenantPhone,
                               LocalDate startDate,
                               LocalDate moveInDate,
                               LocalDate endDate,
                               String contractImageUrl,
                               String occupantCount,
                               String rent,
                               String deposit) {
        LocalDate start = ControllerInputParser.requireDate(startDate, "Ngay bat dau");
        LocalDate moveIn = moveInDate != null ? moveInDate : start;
        if (endDate != null && endDate.isBefore(start)) {
            throw new IllegalArgumentException("Ngay ket thuc phai lon hon hoac bang ngay bat dau");
        }

        service.addContractFull(
                ControllerInputParser.required(roomCode, "Phong"),
                ControllerInputParser.required(tenantName, "Ten khach thue"),
                ControllerInputParser.required(tenantCccd, "CCCD"),
                ControllerInputParser.required(tenantPhone, "So dien thoai"),
                start,
                moveIn,
                endDate,
                ControllerInputParser.nullable(contractImageUrl),
                ControllerInputParser.parseInt(occupantCount, "So nguoi o"),
                ControllerInputParser.parseDouble(rent, "Tien phong"),
                ControllerInputParser.parseDouble(deposit, "Tien coc")
        );
    }

    public void updateContractMoveInDate(int contractId, LocalDate moveInDate) {
        service.updateContractMoveInDate(contractId, ControllerInputParser.requireDate(moveInDate, "Ngay don vao"));
    }
}
