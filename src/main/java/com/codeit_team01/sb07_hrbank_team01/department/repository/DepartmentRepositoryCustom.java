package com.codeit_team01.sb07_hrbank_team01.department.repository;

import com.codeit_team01.sb07_hrbank_team01.department.entity.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DepartmentRepositoryCustom {

    Page<Department> search(String kw,
                            String sortField,   // "name" | "establishedDate" (기타는 name)
                            String sortDirection,// "asc" | "desc" (기타는 asc)
                            String cursor,      // name이면 문자열, establishedDate면 "yyyy-MM-dd"
                            Long idAfter,       // 동률 시 기준으로
                            Pageable pageable);


}