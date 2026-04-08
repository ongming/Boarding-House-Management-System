package com.example.house.service.impl.staff.support;

import com.example.house.model.entity.Contract;
import com.example.house.model.entity.Room;
import com.example.house.model.entity.Tenant;
import com.example.house.model.entity.UtilityReading;
import com.example.house.model.entity.Vehicle;
import com.example.house.model.enums.ContractStatus;
import com.example.house.model.enums.RoomStatus;
import com.example.house.repository.staff.ContractRepository;
import com.example.house.repository.staff.RoomRepository;
import com.example.house.repository.staff.TenantRepository;
import com.example.house.repository.staff.UtilityReadingRepository;
import com.example.house.repository.staff.VehicleRepository;
import com.example.house.model.dto.staff.ContractCreationRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;

public class ContractHandler {
    private final RoomRepository roomRepository;
    private final TenantRepository tenantRepository;
    private final ContractRepository contractRepository;
    private final VehicleRepository vehicleRepository;
    private final UtilityReadingRepository utilityReadingRepository;

    public ContractHandler(RoomRepository roomRepository,
                           TenantRepository tenantRepository,
                           ContractRepository contractRepository,
                           VehicleRepository vehicleRepository,
                           UtilityReadingRepository utilityReadingRepository) {
        this.roomRepository = roomRepository;
        this.tenantRepository = tenantRepository;
        this.contractRepository = contractRepository;
        this.vehicleRepository = vehicleRepository;
        this.utilityReadingRepository = utilityReadingRepository;
    }

    public boolean isRoomAvailable(Integer roomId) {
        return roomRepository.findById(roomId)
                .map(Room::getStatus)
                .map(status -> status == RoomStatus.TRONG)
                .orElse(false);
    }

    public Contract processContractCreation(ContractCreationRequest request) {
        Room room = roomRepository.findByRoomNumber(request.roomNumber())
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        if (room.getStatus() != RoomStatus.TRONG) {
            throw new IllegalArgumentException("Room is not available");
        }

        if (request.endDate() != null && request.startDate() != null
                && request.endDate().isBefore(request.startDate())) {
            throw new IllegalArgumentException("End date must be after or equal start date");
        }

        Tenant tenant = new Tenant();
        tenant.setFullName(request.tenantName());
        tenant.setCccdNumber(request.tenantCccd());
        tenant.setPhoneNumber(request.tenantPhone());
        tenant = tenantRepository.save(tenant);

        Contract contract = new Contract();
        contract.setRoom(room);
        contract.setTenant(tenant);
        contract.setStartDate(request.startDate());
        contract.setMoveInDate(request.moveInDate() != null ? request.moveInDate() : request.startDate());
        contract.setEndDate(request.endDate());
        contract.setContractImageUrl(blankToNull(request.contractImageUrl()));
        contract.setDeposit(defaultBigDecimal(request.deposit()));
        contract.setOccupantCount(request.occupantCount() == null ? 1 : request.occupantCount());
        contract.setStatus(ContractStatus.HIEU_LUC);
        contract = contractRepository.save(contract);

        if (request.vehicleType() != null && !request.vehicleType().isBlank()
                && request.vehiclePlate() != null && !request.vehiclePlate().isBlank()) {
            Vehicle vehicle = new Vehicle();
            vehicle.setContract(contract);
            vehicle.setVehicleType(request.vehicleType());
            vehicle.setLicensePlate(request.vehiclePlate());
            vehicleRepository.save(vehicle);
        }

        if (request.roomFee() != null) {
            room.setBaseRent(request.roomFee());
        }
        room.setStatus(RoomStatus.DA_THUE);
        roomRepository.save(room);
        return contract;
    }

    public UtilityReading saveUtilityReading(String roomNumber, YearMonth period, int oldElectric, int newElectric,
                                             int oldWater, int newWater) {
        Room room = roomRepository.findByRoomNumber(roomNumber)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        UtilityReading reading = new UtilityReading();
        reading.setRoom(room);
        reading.setMonth(period.getMonthValue());
        reading.setYear(period.getYear());
        reading.setOldElectricNumber(oldElectric);
        reading.setNewElectricNumber(newElectric);
        reading.setOldWaterNumber(oldWater);
        reading.setNewWaterNumber(newWater);
        reading.setRecordedAt(LocalDateTime.now());
        return utilityReadingRepository.save(reading);
    }

    public Contract updateOccupancy(String roomNumber, int peopleCount) {
        Room room = roomRepository.findByRoomNumber(roomNumber)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));
        Contract contract = contractRepository.findActiveByRoomId(room.getId())
                .orElseThrow(() -> new IllegalArgumentException("Active contract not found"));
        contract.setOccupantCount(peopleCount);
        return contractRepository.save(contract);
    }

    public Vehicle registerVehicle(String roomNumber, String vehicleType, String plateNumber) {
        Room room = roomRepository.findByRoomNumber(roomNumber)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));
        Contract contract = contractRepository.findActiveByRoomId(room.getId())
                .orElseThrow(() -> new IllegalArgumentException("Active contract not found"));

        Vehicle vehicle = new Vehicle();
        vehicle.setContract(contract);
        vehicle.setVehicleType(vehicleType);
        vehicle.setLicensePlate(plateNumber);
        return vehicleRepository.save(vehicle);
    }

    public Contract updateContractMoveInDate(Integer contractId, java.time.LocalDate moveInDate) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new IllegalArgumentException("Contract not found"));
        contract.setMoveInDate(moveInDate);
        return contractRepository.save(contract);
    }

    private BigDecimal defaultBigDecimal(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
