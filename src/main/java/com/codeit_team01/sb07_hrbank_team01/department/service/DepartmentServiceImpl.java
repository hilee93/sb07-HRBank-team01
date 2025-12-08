package com.codeit_team01.sb07_hrbank_team01.department.service;

import com.codeit_team01.sb07_hrbank_team01.common.dto.response.PageResponseDto;
import com.codeit_team01.sb07_hrbank_team01.common.exception.CustomException;
import com.codeit_team01.sb07_hrbank_team01.common.exception.ErrorCode;
import com.codeit_team01.sb07_hrbank_team01.common.mapper.PageResponseMapper;
import com.codeit_team01.sb07_hrbank_team01.department.entity.Department;
import com.codeit_team01.sb07_hrbank_team01.department.mapper.DepartmentResponseMapper;
import com.codeit_team01.sb07_hrbank_team01.department.repository.DepartmentRepository;
import com.codeit_team01.sb07_hrbank_team01.department.request.DepartmentCreateRequestDto;
import com.codeit_team01.sb07_hrbank_team01.department.request.DepartmentSearchRequestDto;
import com.codeit_team01.sb07_hrbank_team01.department.request.DepartmentUpdateRequestDto;
import com.codeit_team01.sb07_hrbank_team01.department.response.DepartmentResponseDto;
import com.codeit_team01.sb07_hrbank_team01.employee.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.stream.Collectors;

import java.util.Collections;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final PageResponseMapper pageResponseMapper;
    private final DepartmentResponseMapper departmentResponseMapper;

    @Override
    @Transactional
    public DepartmentResponseDto createDepartment(DepartmentCreateRequestDto request) {
        boolean exist = departmentRepository.existsByName(request.name());
        if(exist){
            throw new CustomException(ErrorCode.DEP_DUPLICATE_NAME,
                    "작성하신 부서명 :" + request.name());
        }
        Department department = Department.of(request.name(), request.description(), request.establishedDate());

        Department save = departmentRepository.save(department);


        return departmentResponseMapper.toDto(save, 0);
    }

    @Override
    @Transactional
    public DepartmentResponseDto updateDepartment(Long departmentId, DepartmentUpdateRequestDto request) {

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new CustomException(ErrorCode.DEP_NOT_FOUND,
                        "부서 ID :" + departmentId));

        if (!department.getName().equals(request.name())
                && departmentRepository.existsByName(request.name())) {
            throw new CustomException(ErrorCode.DEP_DUPLICATE_NAME,
                    "작성하신 부서명 :" + request.name());
        }

        department.update(request.name(), request.description(), request.establishedDate());

        int employeeCount =  employeeRepository.countByDepartmentId(department.getId());

        return departmentResponseMapper.toDto(department, employeeCount);
    }


    @Override
    @Transactional
    public void deleteDepartment(Long departmentId) {
        if (!departmentRepository.existsById(departmentId)) {
            throw new CustomException(ErrorCode.DEP_NOT_FOUND,
                    " 부서Id :" + departmentId);
        }

        departmentRepository.deleteById(departmentId);

    }


    @Override
    @Transactional(readOnly = true)
    public DepartmentResponseDto getDepartment(Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() ->  new CustomException(ErrorCode.DEP_NOT_FOUND,
                        "부서 ID :" + departmentId));

        int employeeCount = (int) employeeRepository.countByDepartmentId(department.getId());

        return departmentResponseMapper.toDto(department, employeeCount);
    }

    @Transactional(readOnly = true)
    @Override
    public PageResponseDto<DepartmentResponseDto> searchDepartment(DepartmentSearchRequestDto req) {


        //  페이지 설정
        Pageable pageable = PageRequest.of(0,req.size());

        //  레포 호출 (QueryDSL 단일 메서드)
        Page<Department> page = departmentRepository.search(
               req.nameOrDescription(),
               req.sortField(),
               req.sortDirection(),
               req.cursor(),
               req.idAfter(),
               pageable
        );
        //부서넘버가있고
        List<Department> departments = page.getContent();
        //각넘버에 인원이 있으니
        List<Long>departmentIds = departments.stream().map(Department::getId).toList();
        //그넘버뭉텅이를 가지고와서
        Map<Long, Long> departmentIdAndEmployeeCount = departmentIds.isEmpty()
                ? Collections.emptyMap()
                : employeeRepository.countByDepartmentIds(departmentIds).stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],  // 부서id
                        row -> (Long) row[1]   // 인원숫자
                ));
        List<DepartmentResponseDto> contents = departments.stream()
                .map(d->{
                   int employeeCount = Math.toIntExact(departmentIdAndEmployeeCount.getOrDefault(d.getId(),0L));
                   return departmentResponseMapper.toDto(d,employeeCount);
                }).toList();

        //  nextCursor / nextIdAfter 계산
        String nextCursor = null;
        Long nextIdAfter = null;
        if (!page.isEmpty()) {
            //page.getContent().get()은 0~~n 넘버링이니
            //마지막 조회를 가지고 와야하는기 반  실제조회갯수가 page.getNumberOfElements()
            //찐 마지막넘버는 -1 로  5개를가지고왔으면 0~4니까
            Department last = page.getContent().get(page.getNumberOfElements() - 1);
            nextCursor = "establishedDate".equals(req.sortField())
                    ? last.getEstablishedDate().toString()
                    : last.getName();
            nextIdAfter = last.getId();
        }


        int size = req.size();
        int numberOfElements = page.getNumberOfElements();
        boolean hasNext = (numberOfElements == size);
        long totalElements = page.getTotalElements();

        return new PageResponseDto<>(
                contents,
                nextCursor,
                nextIdAfter,
                size,
                totalElements,
                hasNext
        );
    }




}
