package com.example.house.service.impl.staff.support;

import com.example.house.model.entity.Contract;
import com.example.house.model.entity.Employee;
import com.example.house.model.entity.Invoice;
import com.example.house.model.entity.RateConfig;
import com.example.house.model.entity.UtilityReading;
import com.example.house.model.enums.InvoicePaymentMethod;
import com.example.house.model.enums.RateType;
import com.example.house.repository.staff.ContractRepository;
import com.example.house.repository.staff.EmployeeRepository;
import com.example.house.repository.staff.InvoiceRepository;
import com.example.house.repository.staff.RateConfigRepository;
import com.example.house.repository.staff.UtilityReadingRepository;
import com.example.house.repository.staff.VehicleRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public class BillingHandler {
    private final ContractRepository contractRepository;
    private final VehicleRepository vehicleRepository;
    private final UtilityReadingRepository utilityReadingRepository;
    private final InvoiceRepository invoiceRepository;
    private final RateConfigRepository rateConfigRepository;
    private final EmployeeRepository employeeRepository;

    public BillingHandler(ContractRepository contractRepository,
                          VehicleRepository vehicleRepository,
                          UtilityReadingRepository utilityReadingRepository,
                          InvoiceRepository invoiceRepository,
                          RateConfigRepository rateConfigRepository,
                          EmployeeRepository employeeRepository) {
        this.contractRepository = contractRepository;
        this.vehicleRepository = vehicleRepository;
        this.utilityReadingRepository = utilityReadingRepository;
        this.invoiceRepository = invoiceRepository;
        this.rateConfigRepository = rateConfigRepository;
        this.employeeRepository = employeeRepository;
    }

    public List<Invoice> processAutoBilling(YearMonth period) {
        BigDecimal electricRate = getRate(RateType.DIEN);
        BigDecimal waterRate = getRate(RateType.NUOC);
        BigDecimal garbageRate = getRate(RateType.RAC);
        BigDecimal vehicleRate = getRate(RateType.XE_MAY);
        Optional<Employee> staffOptional = employeeRepository.findFirst();

        return contractRepository.findActiveContracts().stream()
                .filter(contract -> !invoiceRepository.existsByContractAndPeriod(
                        contract.getId(), period.getMonthValue(), period.getYear()))
                .map(contract -> buildInvoice(contract, period, electricRate, waterRate, garbageRate, vehicleRate,
                        staffOptional.orElse(null)))
                .map(invoiceRepository::save)
                .toList();
    }

    public Invoice processPayment(Integer billId, InvoicePaymentMethod paymentType) {
        Invoice invoice = invoiceRepository.findById(billId)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found"));

        invoice.setPaid(Boolean.TRUE);
        invoice.setPaymentMethod(paymentType);
        invoice.setPaidAt(LocalDateTime.now());
        return invoiceRepository.save(invoice);
    }

    private Invoice buildInvoice(Contract contract, YearMonth period, BigDecimal electricRate, BigDecimal waterRate,
                                 BigDecimal garbageRate, BigDecimal vehicleRate, Employee employee) {
        Invoice invoice = new Invoice();
        invoice.setContract(contract);
        invoice.setEmployee(employee);
        invoice.setMonth(period.getMonthValue());
        invoice.setYear(period.getYear());

        BigDecimal roomFee = defaultBigDecimal(contract.getRoom().getBaseRent());
        invoice.setRoomFee(roomFee);

        UtilityReading reading = utilityReadingRepository
                .findByRoomAndPeriod(contract.getRoom().getId(), period.getMonthValue(), period.getYear())
                .orElse(null);

        BigDecimal electricFee = BigDecimal.ZERO;
        BigDecimal waterFee = BigDecimal.ZERO;
        if (reading != null) {
            electricFee = electricRate.multiply(BigDecimal.valueOf(
                    Math.max(0, reading.getNewElectricNumber() - reading.getOldElectricNumber())));
            waterFee = waterRate.multiply(BigDecimal.valueOf(
                    Math.max(0, reading.getNewWaterNumber() - reading.getOldWaterNumber())));
        }

        long vehicleCount = vehicleRepository.countByContractId(contract.getId());
        BigDecimal parkingFee = vehicleRate.multiply(BigDecimal.valueOf(vehicleCount));

        invoice.setElectricFee(electricFee);
        invoice.setWaterFee(waterFee);
        invoice.setParkingFee(parkingFee);
        invoice.setGarbageFee(garbageRate);
        invoice.setTotal(roomFee.add(electricFee).add(waterFee).add(parkingFee).add(garbageRate));
        invoice.setPaid(Boolean.FALSE);
        invoice.setPaymentMethod(InvoicePaymentMethod.CHUA_THU);
        invoice.setCreatedAt(LocalDateTime.now());
        return invoice;
    }

    private BigDecimal getRate(RateType type) {
        return rateConfigRepository.findByType(type)
                .map(RateConfig::getUnitPrice)
                .orElse(BigDecimal.ZERO);
    }

    private BigDecimal defaultBigDecimal(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}
