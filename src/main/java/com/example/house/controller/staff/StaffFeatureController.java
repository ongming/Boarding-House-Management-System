package com.example.house.controller.staff;

import com.example.house.service.staff.StaffDataStore;
import com.example.house.service.staff.StaffService;
import javafx.collections.ObservableList;

import java.time.LocalDate;

public class StaffFeatureController {
    private final StaffRoomCatalogController roomCatalogController;
    private final ContractController contractController;
    private final VehicleController vehicleController;
    private final OccupancyController occupancyController;
    private final MeterReadingController meterReadingController;
    private final BillingController billingController;
    private final PaymentController paymentController;
    private final StaffInvoiceLookupController invoiceLookupController;
    private final StaffFeedbackController feedbackController;

    public StaffFeatureController(StaffService service) {
        this.roomCatalogController = new StaffRoomCatalogController(service);
        this.contractController = new ContractController(service);
        this.vehicleController = new VehicleController(service);
        this.occupancyController = new OccupancyController(service);
        this.meterReadingController = new MeterReadingController(service);
        this.billingController = new BillingController(service);
        this.paymentController = new PaymentController(service);
        this.invoiceLookupController = new StaffInvoiceLookupController(service);
        this.feedbackController = new StaffFeedbackController(service);
    }

    public ObservableList<String> roomCodes() { return roomCatalogController.roomCodes(); }
    public ObservableList<StaffDataStore.ContractItem> contracts() { return contractController.contracts(); }
    public ObservableList<StaffDataStore.VehicleItem> vehicles() { return vehicleController.vehicles(); }
    public ObservableList<StaffDataStore.OccupancyItem> occupancies() { return occupancyController.occupancies(); }
    public ObservableList<StaffDataStore.MeterReadingItem> meterReadings() { return meterReadingController.meterReadings(); }
    public ObservableList<StaffDataStore.InvoiceItem> invoices() { return invoiceLookupController.invoices(); }
    public ObservableList<StaffDataStore.FeedbackItem> feedbacks() { return feedbackController.feedbacks(); }

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
        contractController.createContract(
                roomCode,
                tenantName,
                tenantCccd,
                tenantPhone,
                startDate,
                moveInDate,
                endDate,
                contractImageUrl,
                occupantCount,
                rent,
                deposit
        );
    }

    public void updateContractMoveInDate(int contractId, LocalDate moveInDate) {
        contractController.updateContractMoveInDate(contractId, moveInDate);
    }

    public void addVehicle(String roomCode, String vehicleType, String plateNumber, String monthlyFee) {
        vehicleController.addVehicle(roomCode, vehicleType, plateNumber, monthlyFee);
    }

    public void updateOccupancy(String roomCode, String peopleCount) {
        occupancyController.updateOccupancy(roomCode, peopleCount);
    }

    public void saveMeterReading(String roomCode,
                                 String month,
                                 String oldElectric,
                                 String newElectric,
                                 String oldWater,
                                 String newWater) {
        meterReadingController.saveMeterReading(roomCode, month, oldElectric, newElectric, oldWater, newWater);
    }

    public int generateInvoices(String month, String electricRate, String waterRate, String garbageFee) {
        return billingController.generateInvoices(month, electricRate, waterRate, garbageFee);
    }

    public void markInvoicePaid(StaffDataStore.InvoiceItem invoice, String paymentMethod) {
        paymentController.markInvoicePaid(invoice, paymentMethod);
    }

    public void addFeedback(String roomCode, String title, String description, String priority) {
        feedbackController.addFeedback(roomCode, title, description, priority);
    }
}
