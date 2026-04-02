package com.example.house.view.staff.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryStaffDataStore implements StaffDataStore {
    private final ObservableList<ContractItem> contracts = FXCollections.observableArrayList();
    private final ObservableList<VehicleItem> vehicles = FXCollections.observableArrayList();
    private final ObservableList<OccupancyItem> occupancies = FXCollections.observableArrayList();
    private final ObservableList<MeterReadingItem> meterReadings = FXCollections.observableArrayList();
    private final ObservableList<InvoiceItem> invoices = FXCollections.observableArrayList();
    private final ObservableList<FeedbackItem> feedbacks = FXCollections.observableArrayList();

    private final AtomicInteger contractId = new AtomicInteger(1);
    private final AtomicInteger vehicleId = new AtomicInteger(1);
    private final AtomicInteger meterId = new AtomicInteger(1);
    private final AtomicInteger invoiceId = new AtomicInteger(1);
    private final AtomicInteger feedbackId = new AtomicInteger(1);

    @Override
    public ObservableList<ContractItem> contracts() { return contracts; }

    @Override
    public ObservableList<VehicleItem> vehicles() { return vehicles; }

    @Override
    public ObservableList<OccupancyItem> occupancies() { return occupancies; }

    @Override
    public ObservableList<MeterReadingItem> meterReadings() { return meterReadings; }

    @Override
    public ObservableList<InvoiceItem> invoices() { return invoices; }

    @Override
    public ObservableList<FeedbackItem> feedbacks() { return feedbacks; }

    @Override
    public ContractItem addContract(String roomCode, String tenantName, double rentFee, double deposit) {
        ContractItem item = new ContractItem(contractId.getAndIncrement(), roomCode, tenantName, rentFee, deposit, LocalDateTime.now());
        contracts.add(item);
        return item;
    }

    @Override
    public VehicleItem addVehicle(String roomCode, String vehicleType, String plateNumber, double monthlyFee) {
        VehicleItem item = new VehicleItem(vehicleId.getAndIncrement(), roomCode, vehicleType, plateNumber, monthlyFee);
        vehicles.add(item);
        return item;
    }

    @Override
    public OccupancyItem upsertOccupancy(String roomCode, int peopleCount) {
        OccupancyItem updated = new OccupancyItem(roomCode, peopleCount, LocalDateTime.now());
        int index = -1;
        for (int i = 0; i < occupancies.size(); i++) {
            if (occupancies.get(i).roomCode().equalsIgnoreCase(roomCode)) {
                index = i;
                break;
            }
        }

        if (index >= 0) {
            occupancies.set(index, updated);
        } else {
            occupancies.add(updated);
        }
        return updated;
    }

    @Override
    public MeterReadingItem addMeterReading(String roomCode, YearMonth month, int oldElectric, int newElectric, int oldWater, int newWater) {
        MeterReadingItem item = new MeterReadingItem(meterId.getAndIncrement(), roomCode, month, oldElectric, newElectric, oldWater, newWater);
        meterReadings.add(item);
        return item;
    }

    @Override
    public int generateInvoices(YearMonth month, double electricRate, double waterRate, double garbageFee) {
        int before = invoices.size();
        meterReadings.stream().filter(reading -> reading.month().equals(month)).forEach(reading -> {
            if (hasInvoice(reading.roomCode(), month)) {
                return;
            }

            double roomFee = findRoomFee(reading.roomCode());
            double electricFee = Math.max(0, reading.newElectric() - reading.oldElectric()) * electricRate;
            double waterFee = Math.max(0, reading.newWater() - reading.oldWater()) * waterRate;
            double vehicleFee = findVehicleFee(reading.roomCode());
            double total = roomFee + electricFee + waterFee + vehicleFee + garbageFee;

            invoices.add(new InvoiceItem(
                    invoiceId.getAndIncrement(),
                    reading.roomCode(),
                    month,
                    roomFee,
                    electricFee,
                    waterFee,
                    vehicleFee,
                    garbageFee,
                    total,
                    false,
                    "Chưa thanh toán",
                    LocalDateTime.now()
            ));
        });

        return invoices.size() - before;
    }

    @Override
    public InvoiceItem markInvoicePaid(int targetInvoiceId, String paymentMethod) {
        for (int i = 0; i < invoices.size(); i++) {
            InvoiceItem invoice = invoices.get(i);
            if (invoice.id() == targetInvoiceId) {
                InvoiceItem updated = new InvoiceItem(
                        invoice.id(),
                        invoice.roomCode(),
                        invoice.month(),
                        invoice.roomFee(),
                        invoice.electricFee(),
                        invoice.waterFee(),
                        invoice.vehicleFee(),
                        invoice.garbageFee(),
                        invoice.totalAmount(),
                        true,
                        paymentMethod,
                        invoice.createdAt()
                );
                invoices.set(i, updated);
                return updated;
            }
        }

        throw new IllegalArgumentException("Không tìm thấy hóa đơn");
    }

    @Override
    public FeedbackItem addFeedback(String roomCode, String title, String description, String priority) {
        FeedbackItem item = new FeedbackItem(feedbackId.getAndIncrement(), roomCode, title, description, priority, "Mới", LocalDateTime.now());
        feedbacks.add(item);
        return item;
    }

    private boolean hasInvoice(String roomCode, YearMonth month) {
        return invoices.stream().anyMatch(invoice -> invoice.roomCode().equalsIgnoreCase(roomCode) && invoice.month().equals(month));
    }

    private double findRoomFee(String roomCode) {
        return contracts.stream()
                .filter(contract -> contract.roomCode().equalsIgnoreCase(roomCode))
                .mapToDouble(ContractItem::rentFee)
                .findFirst()
                .orElse(0.0);
    }

    private double findVehicleFee(String roomCode) {
        return vehicles.stream()
                .filter(vehicle -> vehicle.roomCode().equalsIgnoreCase(roomCode))
                .mapToDouble(VehicleItem::monthlyFee)
                .sum();
    }
}

