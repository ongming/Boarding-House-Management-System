package com.example.house.controller.staff;

import com.example.house.controller.shared.ControllerInputParser;
import com.example.house.service.staff.StaffService;

public class BillingController {
    private final StaffService service;

    public BillingController(StaffService service) {
        this.service = service;
    }

    public int generateInvoices(String month, String electricRate, String waterRate, String garbageFee) {
        return service.generateInvoices(
                ControllerInputParser.parseMonth(month),
                ControllerInputParser.parseDouble(electricRate, "Don gia dien"),
                ControllerInputParser.parseDouble(waterRate, "Don gia nuoc"),
                ControllerInputParser.parseDouble(garbageFee, "Phi rac")
        );
    }
}

