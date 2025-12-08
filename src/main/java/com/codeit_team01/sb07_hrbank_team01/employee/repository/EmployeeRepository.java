package com.codeit_team01.sb07_hrbank_team01.employee.repository;

import com.codeit_team01.sb07_hrbank_team01.employee.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public interface EmployeeRepository extends JpaRepository<Employee, Long>, EmployeeRepositoryCustom {
    int countByDepartmentId(Long departmentId);
    boolean existsByEmailIgnoreCase(String email);

  @Query(value = "SELECT e FROM Employee e")
  Stream<Employee> streamAll();

  boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

    @Query("""
    select e.department.id, count(e)
    from Employee e
    where e.department.id in :departmentId
    group by e.department.id
""")
    List<Object[]> countByDepartmentIds(Collection<Long> departmentId);

    boolean existsByName(String name);
}