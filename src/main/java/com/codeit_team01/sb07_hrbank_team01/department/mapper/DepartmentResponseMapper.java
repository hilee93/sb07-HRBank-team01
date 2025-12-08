package com.codeit_team01.sb07_hrbank_team01.department.mapper;

import com.codeit_team01.sb07_hrbank_team01.department.entity.Department;
import com.codeit_team01.sb07_hrbank_team01.department.response.DepartmentResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DepartmentResponseMapper {


    
    DepartmentResponseDto toDto(Department department, int employeeCount);


}
