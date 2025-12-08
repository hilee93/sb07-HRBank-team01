package com.codeit_team01.sb07_hrbank_team01.history.entity;

import com.codeit_team01.sb07_hrbank_team01.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor

@Entity
@Table(name = "employee_histories")
public class History extends BaseEntity {

    // 수정 이력 Id, 이력 등록 시간은 base에

    // 수정 유형
    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    private HistoryType type;

    // 선택적으로 화면에서 입력
    @Column(length = 255)
    private String memo;

    // ip_address : 서버 자동 추출
    @Column(name = "ip_address", length = 255)
    private String ipAddress;

    @Column(name = "employee_no", length = 50)
    private String employeeNo;

    // 변경 상세 목록 (1:N)
    // details도 테이블을 갖고 있으니 DB에 관계를 맺어서 add할 필요 없게 하는 방법을 찾아보자.
    @OneToMany(mappedBy = "history", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<HistoryDetail> details = new ArrayList<>();

    public static History createHistory(HistoryType type, String memo, String ipAddress, String employeeNo) {
        History history = new History();
        history.type = type;
        history.memo = memo != null ? memo : type.getDefaultMemo();
        history.ipAddress = ipAddress;
        history.employeeNo = employeeNo;
        return history;
    }

    public void addDetail(String propertyName, String beforeValue, String afterValue) {
        HistoryDetail detail = HistoryDetail.createDetail(propertyName, beforeValue, afterValue, this);
        this.details.add(detail);
    }
}
