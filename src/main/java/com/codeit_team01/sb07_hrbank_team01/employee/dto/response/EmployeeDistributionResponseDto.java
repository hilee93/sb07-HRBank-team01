package com.codeit_team01.sb07_hrbank_team01.employee.dto.response;

public record EmployeeDistributionResponseDto(
        String groupKey,
        long count,
        double percentage
) {
}
