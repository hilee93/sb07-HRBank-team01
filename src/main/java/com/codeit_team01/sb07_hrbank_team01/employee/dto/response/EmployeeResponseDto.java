package com.codeit_team01.sb07_hrbank_team01.employee.dto.response;

import com.codeit_team01.sb07_hrbank_team01.employee.entity.EmployeeStatus;

import java.time.LocalDate;

public record EmployeeResponseDto(
        Long id,
        String name,
        String email,
        String employeeNumber,
        Long departmentId,
        String departmentName,
        String position,
        LocalDate hireDate,
        EmployeeStatus status,
        Long profileImageId
) {
}
