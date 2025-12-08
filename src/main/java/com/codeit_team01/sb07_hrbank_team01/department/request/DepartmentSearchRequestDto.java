package com.codeit_team01.sb07_hrbank_team01.department.request;


import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;


public record DepartmentSearchRequestDto(

        String nameOrDescription,

        @Positive(message = "idAfter는 1 이상이어야 합니다.")
        Long idAfter,

        String cursor,

        @Positive(message = "size는 1 이상이어야 합니다.")
        Integer size,


        @Pattern(regexp = "name|establishedDate", message = "정렬 필드는 name 또는 establishedDate만 가능합니다.")
        String sortField,


        @Pattern(regexp = "asc|desc", message = "정렬 방향은 asc 또는 desc만 가능합니다.")
        String sortDirection
) {

}