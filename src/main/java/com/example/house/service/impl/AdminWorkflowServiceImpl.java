package com.example.house.service.impl;

import com.example.house.config.JpaUntil;
import com.example.house.model.entity.Account;
import com.example.house.model.entity.Compensation;
import com.example.house.model.entity.Contract;
import com.example.house.model.entity.Employee;
import com.example.house.model.entity.Feedback;
import com.example.house.model.entity.Invoice;
import com.example.house.model.entity.RateConfig;
import com.example.house.model.entity.Room;
import com.example.house.model.enums.AccountType;
import com.example.house.model.enums.CompensationPaymentMethod;
import com.example.house.model.enums.ContractStatus;
import com.example.house.model.enums.FeedbackStatus;
import com.example.house.model.enums.RateType;
import com.example.house.model.enums.RoomStatus;
import com.example.house.repository.AccountRepository;
import com.example.house.repository.impl.JpaAccountRepository;
import com.example.house.repository.impl.JpaCompensationRepository;
import com.example.house.repository.impl.JpaContractRepository;
import com.example.house.repository.impl.JpaEmployeeRepository;
import com.example.house.repository.impl.JpaFeedbackRepository;
import com.example.house.repository.impl.JpaInvoiceRepository;
import com.example.house.repository.impl.JpaRateConfigRepository;
import com.example.house.repository.impl.JpaRoomRepository;
import com.example.house.repository.staff.CompensationRepository;
import com.example.house.repository.staff.ContractRepository;
import com.example.house.repository.staff.EmployeeRepository;
import com.example.house.repository.staff.FeedbackRepository;
import com.example.house.repository.staff.InvoiceRepository;
import com.example.house.repository.staff.RateConfigRepository;
import com.example.house.repository.staff.RoomRepository;
import com.example.house.service.admin.AdminCheckoutSummary;
import com.example.house.service.admin.AdminRevenuePeriod;
import com.example.house.service.admin.AdminRevenueReport;
import com.example.house.service.admin.AdminRevenueRow;
import com.example.house.service.admin.AdminWorkflowService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminWorkflowServiceImpl implements AdminWorkflowService {
    private final RateConfigRepository rateConfigRepository;
    private final RoomRepository roomRepository;
    private final EmployeeRepository employeeRepository;
    private final AccountRepository accountRepository;
    private final ContractRepository contractRepository;
    private final InvoiceRepository invoiceRepository;
    private final FeedbackRepository feedbackRepository;
    private final CompensationRepository compensationRepository;

    public AdminWorkflowServiceImpl() {
        this(new JpaRateConfigRepository(),
                new JpaRoomRepository(),
                new JpaEmployeeRepository(),
                new JpaAccountRepository(),
                new JpaContractRepository(),
                new JpaInvoiceRepository(),
                new JpaFeedbackRepository(),
                new JpaCompensationRepository());
    }

    public AdminWorkflowServiceImpl(RateConfigRepository rateConfigRepository,
                                    RoomRepository roomRepository,
                                    EmployeeRepository employeeRepository,
                                    AccountRepository accountRepository,
                                    ContractRepository contractRepository,
                                    InvoiceRepository invoiceRepository,
                                    FeedbackRepository feedbackRepository,
                                    CompensationRepository compensationRepository) {
        this.rateConfigRepository = rateConfigRepository;
        this.roomRepository = roomRepository;
        this.employeeRepository = employeeRepository;
        this.accountRepository = accountRepository;
        this.contractRepository = contractRepository;
        this.invoiceRepository = invoiceRepository;
        this.feedbackRepository = feedbackRepository;
        this.compensationRepository = compensationRepository;
    }

    @Override
    public List<RateConfig> getRateConfigs() {
        return rateConfigRepository.findAll();
    }

    @Override
    public RateConfig saveRateConfig(RateType type, BigDecimal unitPrice) {
        if (type == null) {
            throw new IllegalArgumentException("Loai don gia khong duoc de trong");
        }
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Don gia khong hop le");
        }

        RateConfig config = rateConfigRepository.findByType(type).orElseGet(RateConfig::new);
        config.setType(type);
        config.setUnitPrice(unitPrice);
        return rateConfigRepository.save(config);
    }

    @Override
    public List<Room> getRooms() {
        return roomRepository.findAllOrderByFloorAndRoomNumber();
    }

    @Override
    public Room saveRoom(Room room) {
        if (room == null || room.getRoomNumber() == null || room.getRoomNumber().isBlank()) {
            throw new IllegalArgumentException("So phong khong duoc de trong");
        }
        if (room.getBaseRent() == null || room.getBaseRent().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Gia thue khong hop le");
        }

        roomRepository.findByRoomNumber(room.getRoomNumber()).ifPresent(existing -> {
            if (room.getId() == null || !existing.getId().equals(room.getId())) {
                throw new IllegalArgumentException("So phong da ton tai");
            }
        });

        RoomStatus status = room.getStatus() == null ? RoomStatus.TRONG : room.getStatus();
        room.setStatus(status);
        return roomRepository.save(room);
    }

    @Override
    public void deleteRoom(Integer roomId) {
        if (roomId == null) {
            throw new IllegalArgumentException("Chon phong can xoa");
        }
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay phong"));
        if (room.getStatus() != null && room.getStatus() != RoomStatus.TRONG) {
            throw new IllegalArgumentException("Chi duoc xoa phong TRONG");
        }
        roomRepository.deleteById(roomId);
    }

    @Override
    public List<Employee> getEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Account createStaffAccount(String username, String rawPassword, String fullName, String shiftSchedule) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Ten dang nhap khong duoc de trong");
        }
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new IllegalArgumentException("Mat khau khong duoc de trong");
        }

        accountRepository.findByUsername(username.trim()).ifPresent(account -> {
            throw new IllegalArgumentException("Ten dang nhap da ton tai");
        });

        EntityManager em = JpaUntil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            Account account = new Account();
            account.setUsername(username.trim());
            account.setPassword(rawPassword);
            account.setFullName(fullName);
            account.setAccountType(AccountType.NHAN_VIEN);
            em.persist(account);

            Employee employee = new Employee();
            employee.setAccount(account);
            employee.setShiftSchedule(shiftSchedule);
            em.persist(employee);

            account.setEmployee(employee);

            tx.commit();
            return account;
        } catch (RuntimeException ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw ex;
        } finally {
            em.close();
        }
    }

    @Override
    public AdminRevenueReport getRevenueReport(AdminRevenuePeriod period, int year) {
        if (period == null) {
            throw new IllegalArgumentException("Chon ky thong ke");
        }
        if (year < 2000) {
            throw new IllegalArgumentException("Nam khong hop le");
        }

        Map<Integer, BigDecimal> invoiceByMonth = loadInvoiceTotals(year);
        Map<Integer, BigDecimal> compensationByMonth = loadCompensationTotals(year);

        List<AdminRevenueRow> rows = new ArrayList<>();
        if (period == AdminRevenuePeriod.YEAR) {
            BigDecimal invoiceTotal = sum(invoiceByMonth);
            BigDecimal compensationTotal = sum(compensationByMonth);
            rows.add(new AdminRevenueRow("Nam " + year, invoiceTotal, compensationTotal));
        } else if (period == AdminRevenuePeriod.QUARTER) {
            for (int quarter = 1; quarter <= 4; quarter++) {
                int start = (quarter - 1) * 3 + 1;
                int end = start + 2;
                BigDecimal invoiceTotal = sum(invoiceByMonth, start, end);
                BigDecimal compensationTotal = sum(compensationByMonth, start, end);
                rows.add(new AdminRevenueRow("Quy " + quarter, invoiceTotal, compensationTotal));
            }
        } else {
            for (int month = 1; month <= 12; month++) {
                BigDecimal invoiceTotal = invoiceByMonth.getOrDefault(month, BigDecimal.ZERO);
                BigDecimal compensationTotal = compensationByMonth.getOrDefault(month, BigDecimal.ZERO);
                rows.add(new AdminRevenueRow("Thang " + month, invoiceTotal, compensationTotal));
            }
        }

        return new AdminRevenueReport(period, year, rows);
    }

    @Override
    public List<Contract> getActiveContracts() {
        return contractRepository.findActiveContracts();
    }

    @Override
    public AdminCheckoutSummary buildCheckoutSummary(Integer contractId) {
        if (contractId == null) {
            throw new IllegalArgumentException("Chon hop dong");
        }
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay hop dong"));

        BigDecimal deposit = contract.getDeposit() == null ? BigDecimal.ZERO : contract.getDeposit();

        EntityManager em = JpaUntil.getEntityManager();
        try {
            BigDecimal unpaidInvoices = em.createQuery(
                            "SELECT COALESCE(SUM(i.total), 0) FROM Invoice i WHERE i.contract.id = :contractId AND (i.paid = FALSE OR i.paid IS NULL)",
                            BigDecimal.class)
                    .setParameter("contractId", contractId)
                    .getSingleResult();

            BigDecimal unpaidCompensations = em.createQuery(
                            "SELECT COALESCE(SUM(c.amount), 0) FROM Compensation c WHERE c.contract.id = :contractId AND (c.collected = FALSE OR c.collected IS NULL)",
                            BigDecimal.class)
                    .setParameter("contractId", contractId)
                    .getSingleResult();

            BigDecimal refund = deposit.subtract(unpaidInvoices.add(unpaidCompensations));
            return new AdminCheckoutSummary(deposit, unpaidInvoices, unpaidCompensations, refund);
        } finally {
            em.close();
        }
    }

    @Override
    public Compensation addCompensation(Integer contractId, BigDecimal amount, String reason) {
        if (contractId == null) {
            throw new IllegalArgumentException("Chon hop dong");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Tien boi thuong khong hop le");
        }

        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay hop dong"));

        Compensation compensation = new Compensation();
        compensation.setContract(contract);
        compensation.setAmount(amount);
        compensation.setReason(reason);
        compensation.setCollected(Boolean.FALSE);
        compensation.setPaymentMethod(CompensationPaymentMethod.TIEN_MAT);
        compensation.setCreatedDate(LocalDate.now());
        return compensationRepository.save(compensation);
    }

    @Override
    public void approveCheckout(Integer contractId, RoomStatus roomStatus, CompensationPaymentMethod paymentMethod) {
        if (contractId == null) {
            throw new IllegalArgumentException("Chon hop dong");
        }
        RoomStatus targetStatus = roomStatus == null ? RoomStatus.TRONG : roomStatus;
        CompensationPaymentMethod method = paymentMethod == null ? CompensationPaymentMethod.TIEN_MAT : paymentMethod;

        EntityManager em = JpaUntil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Contract contract = em.find(Contract.class, contractId);
            if (contract == null) {
                throw new IllegalArgumentException("Khong tim thay hop dong");
            }

            contract.setStatus(ContractStatus.KET_THUC);
            if (contract.getEndDate() == null) {
                contract.setEndDate(LocalDate.now());
            }

            Room room = contract.getRoom();
            if (room != null) {
                room.setStatus(targetStatus);
            }

            em.createQuery(
                            "UPDATE Compensation c SET c.collected = TRUE, c.paymentMethod = :method WHERE c.contract.id = :contractId AND (c.collected = FALSE OR c.collected IS NULL)")
                    .setParameter("method", method)
                    .setParameter("contractId", contractId)
                    .executeUpdate();

            em.createQuery("DELETE FROM Vehicle v WHERE v.contract.id = :contractId")
                    .setParameter("contractId", contractId)
                    .executeUpdate();

            tx.commit();
        } catch (RuntimeException ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw ex;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Feedback> getFeedbacks() {
        return feedbackRepository.findAll();
    }

    @Override
    public List<Feedback> getFeedbacksByStatus(FeedbackStatus status) {
        if (status == null) {
            return feedbackRepository.findAll();
        }
        return feedbackRepository.findByStatus(status);
    }

    @Override
    public Feedback updateFeedbackStatus(Integer id, FeedbackStatus status) {
        if (id == null) {
            throw new IllegalArgumentException("Chon phan hoi");
        }
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay phan hoi"));
        feedback.setStatus(status == null ? FeedbackStatus.CHO_XU_LY : status);
        return feedbackRepository.save(feedback);
    }

    @Override
    public List<Invoice> findInvoices(String roomNumber, Integer month, Integer year, Boolean paid) {
        return invoiceRepository.findByFilters(roomNumber, month, year, paid);
    }

    private Map<Integer, BigDecimal> loadInvoiceTotals(int year) {
        EntityManager em = JpaUntil.getEntityManager();
        try {
            List<Object[]> rows = em.createQuery(
                            "SELECT i.month, COALESCE(SUM(i.total), 0) FROM Invoice i WHERE i.paid = TRUE AND i.year = :year GROUP BY i.month",
                            Object[].class)
                    .setParameter("year", year)
                    .getResultList();

            Map<Integer, BigDecimal> totals = new HashMap<>();
            for (Object[] row : rows) {
                Integer month = (Integer) row[0];
                BigDecimal total = (BigDecimal) row[1];
                totals.put(month, total == null ? BigDecimal.ZERO : total);
            }
            return totals;
        } finally {
            em.close();
        }
    }

    private Map<Integer, BigDecimal> loadCompensationTotals(int year) {
        EntityManager em = JpaUntil.getEntityManager();
        try {
            List<Object[]> rows = em.createQuery(
                            "SELECT FUNCTION('MONTH', c.createdDate), COALESCE(SUM(c.amount), 0) "
                                    + "FROM Compensation c WHERE c.collected = TRUE AND FUNCTION('YEAR', c.createdDate) = :year "
                                    + "GROUP BY FUNCTION('MONTH', c.createdDate)",
                            Object[].class)
                    .setParameter("year", year)
                    .getResultList();

            Map<Integer, BigDecimal> totals = new HashMap<>();
            for (Object[] row : rows) {
                Integer month = ((Number) row[0]).intValue();
                BigDecimal total = (BigDecimal) row[1];
                totals.put(month, total == null ? BigDecimal.ZERO : total);
            }
            return totals;
        } finally {
            em.close();
        }
    }

    private BigDecimal sum(Map<Integer, BigDecimal> values) {
        return values.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal sum(Map<Integer, BigDecimal> values, int start, int end) {
        BigDecimal total = BigDecimal.ZERO;
        for (int i = start; i <= end; i++) {
            total = total.add(values.getOrDefault(i, BigDecimal.ZERO));
        }
        return total;
    }
}
