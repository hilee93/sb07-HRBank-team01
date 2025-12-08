package com.codeit_team01.sb07_hrbank_team01.department.repository;

import com.codeit_team01.sb07_hrbank_team01.department.entity.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;


public interface DepartmentRepository extends JpaRepository<Department, Long> , DepartmentRepositoryCustom {
    boolean existsByName(String name);
}
