package com.example.house.service.admin;

import com.example.house.model.entity.Account;
import com.example.house.model.entity.Employee;

import java.util.List;

public interface StaffAccountService {
    List<Employee> getEmployees();
    Account createStaffAccount(String username, String rawPassword, String fullName, String shiftSchedule);
    Account updateStaffAccount(Integer employeeId, String username, String rawPassword, String fullName, String shiftSchedule);
    void deleteStaffAccount(Integer employeeId);
}


