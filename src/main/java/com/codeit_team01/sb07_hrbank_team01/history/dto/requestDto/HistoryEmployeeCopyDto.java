package com.codeit_team01.sb07_hrbank_team01.history.dto.requestDto;

import com.codeit_team01.sb07_hrbank_team01.employee.entity.Employee;
import com.codeit_team01.sb07_hrbank_team01.employee.entity.EmployeeStatus;

import java.time.Instant;

public record HistoryEmployeeCopyDto (
        Instant hireDate,
        String name,
        String jobPosition,
        String department,
        String email,
        String employeeNo,
        EmployeeStatus status
){
    public static HistoryEmployeeCopyDto from(Employee employee){
        return new HistoryEmployeeCopyDto(
                employee.getHireDate(),
                employee.getName(),
                employee.getJobPosition(),
                employee.getDepartment().getName(),
                employee.getEmail(),
                employee.getEmployeeNo(),
                employee.getStatus()
        );
    }
}
