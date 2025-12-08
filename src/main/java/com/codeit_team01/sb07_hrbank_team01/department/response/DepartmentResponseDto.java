package com.codeit_team01.sb07_hrbank_team01.department.response;

import java.time.LocalDate;


public record DepartmentResponseDto(
        Long id,
        String name,
        String description,
        LocalDate establishedDate,
        int employeeCount
) {

}
