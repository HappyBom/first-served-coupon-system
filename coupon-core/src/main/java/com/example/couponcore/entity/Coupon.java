package com.example.couponcore.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table(name="cupons")
@Getter
public class Cupon extends BaseTimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CuponGubun cuponGubun;

    private Integer totalCnt;

    @Column(nullable = false)
    private Integer issueCnt;

    @Column(nullable = false)
    private Integer discountAmt;

    @Column(nullable = false)
    private Integer minAvailAmt;

    @Column(nullable = false)
    private LocalDateTime issueSt;

    @Column(nullable = false)
    private LocalDateTime issueEnd;


    //쿠폰 발급이 가능한지 수량 확인
    public boolean availableIssueCoupon(){
        if(issueCnt == null){
            return true;
        }
        return totalCnt > issueCnt;
    }

    //쿠폰 발급 기간 검증
    public boolean availableIssueDate(){
        LocalDateTime now = LocalDateTime.now();
        return issueSt.isBefore(now) && issueEnd.isAfter(now);  //now 보다 이전 이고, 이후 인지
    }


    public void couponIssue(){

        if(!availableIssueCoupon()){
            throw new RuntimeException("쿠폰 발급 수량 검증 오류");   //추후, customException 정의하기!
        }

        if(!availableIssueDate()){
            throw new RuntimeException("쿠폰 발급 기간 검증 오류");
        }

        issueCnt++;
    }

}
