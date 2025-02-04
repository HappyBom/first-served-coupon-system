package com.example.couponcore.entity;

import com.example.couponcore.exception.CouponIssueException;
import com.example.couponcore.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name="coupons")
@Getter
@Builder           //생성자나 setter를 사용하지 않고 객체 생성, NoArgsConstructor 와 함께 쓸때는 AllArgsConstructor 필요
@NoArgsConstructor      //기본생성자. JPA 기본생성자 필요 <-> AllArgsConstructor 모든 필드를 포함한 생성자 생성
@AllArgsConstructor
public class Coupon extends BaseTimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CouponGubun couponGubun;

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
        if(totalCnt == null){
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
            throw new CouponIssueException(ErrorCode.INVALID_COUPON_ISSUE_CNT, "발급 가능한 쿠폰 수량을 초과하였습니다. totoal : %s, issued : %s".formatted(totalCnt, issueCnt));
        }

        if(!availableIssueDate()){
            throw new CouponIssueException(ErrorCode.INVALID_COUPON_ISSUE_DATE, "발급 가능한 일자가 아닙니다. request : %s, issueSt : %s, issueEnd : %s".formatted(LocalDateTime.now(), issueSt, issueEnd));
        }

        issueCnt++;
    }

}
