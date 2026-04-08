package com.example.house.service.impl.admin;

import com.example.house.model.entity.Account;
import com.example.house.model.entity.Employee;
import com.example.house.service.admin.AdminDomainService;
import com.example.house.service.admin.StaffAccountService;
import com.example.house.service.impl.admin.AdminDomainServiceImpl;

import java.util.List;

public class StaffAccountServiceImpl implements StaffAccountService {
    private final AdminDomainService workflow;

    public StaffAccountServiceImpl() {
        this(new AdminDomainServiceImpl());
    }

    public StaffAccountServiceImpl(AdminDomainService workflow) {
        this.workflow = workflow;
    }

    @Override
    public List<Employee> getEmployees() {
        return workflow.getEmployees();
    }

    @Override
    public Account createStaffAccount(String username, String rawPassword, String fullName, String shiftSchedule) {
        return workflow.createStaffAccount(username, rawPassword, fullName, shiftSchedule);
    }

    @Override
    public Account updateStaffAccount(Integer employeeId, String username, String rawPassword, String fullName, String shiftSchedule) {
        return workflow.updateStaffAccount(employeeId, username, rawPassword, fullName, shiftSchedule);
    }

    @Override
    public void deleteStaffAccount(Integer employeeId) {
        workflow.deleteStaffAccount(employeeId);
    }
}

