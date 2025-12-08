package com.codeit_team01.sb07_hrbank_team01.employee.dto.response;

import java.time.LocalDate;

public record EmployeeTrendResponseDto(
        LocalDate date,
        long count,
        long change,
        double changeRate
) {
}
