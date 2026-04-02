package com.example.house.service.staff;

import com.example.house.model.entity.Contract;
import com.example.house.model.entity.Feedback;
import com.example.house.model.entity.Invoice;
import com.example.house.model.entity.Room;
import com.example.house.model.entity.UtilityReading;
import com.example.house.model.entity.Vehicle;
import com.example.house.model.enums.FeedbackStatus;
import com.example.house.model.enums.InvoicePaymentMethod;

import java.time.YearMonth;
import java.util.List;

public interface StaffWorkflowService {
    boolean isRoomAvailable(Integer roomId);

    Contract processContractCreation(ContractCreationRequest request);

    UtilityReading saveUtilityReading(String roomNumber, YearMonth period, int oldElectric, int newElectric,
                                      int oldWater, int newWater);

    List<Invoice> processAutoBilling(YearMonth period);

    Invoice processPayment(Integer billId, InvoicePaymentMethod paymentType);

    Feedback saveIncident(Integer roomId, String content, Integer currentStaffId);

    Feedback updateIncidentStatus(Integer incidentId, FeedbackStatus status);

    Contract updateOccupancy(String roomNumber, int peopleCount);

    Vehicle registerVehicle(String roomNumber, String vehicleType, String plateNumber);

    List<Room> getRooms();

    List<Contract> getContracts();

    List<Vehicle> getVehicles();

    List<UtilityReading> getUtilityReadings();

    List<Invoice> getInvoices();

    List<Feedback> getFeedbacks();
}
