package com.example.couponcore.entity;

import com.example.couponcore.exception.CouponIssueException;
import com.example.couponcore.exception.ErrorCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;


@SpringBootTest
class CouponTest {

    @Test
    @DisplayName("발급 수량이 남아 있다면 true를 반환")
    void availableIssueCoupon_true(){
        //given
        Coupon coupon = Coupon.builder()
                .totalCnt(1000)
                .issueCnt(900)
                .build();

        //when
        boolean result = coupon.availableIssueCoupon();

        //then
        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("발급 수량이 소진되었다면 false를 반환")
    void availableIssueCoupon_false(){
        //given
        Coupon coupon = Coupon.builder()
                .totalCnt(800)
                .issueCnt(900)
                .build();

        //when
        boolean result = coupon.availableIssueCoupon();

        //then
        Assertions.assertFalse(result);
    }

    @Test
    @DisplayName("발급 수량이 설정되지 않았다면 true를 반환")
    void availableIssueCoupon_null(){
        //given
        Coupon coupon = Coupon.builder()
                .totalCnt(null)
                .issueCnt(900)
                .build();

        //when
        boolean result = coupon.availableIssueCoupon();

        //then
        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("쿠폰 발급 기간에 해당 할 때")
    void availableIssueDate_true(){

        //given
        Coupon coupon = Coupon.builder()
                .issueSt(LocalDateTime.now().minusDays(1))
                .issueEnd(LocalDateTime.now().plusDays(2))
                .build();

        //when
        boolean result = coupon.availableIssueDate();

        //then
        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("쿠폰 발급 기간이 도래하지 않았을 때")
    void availableIssueDate_false(){

        //given
        Coupon coupon = Coupon.builder()
                .issueSt(LocalDateTime.now().plusDays(1))
                .issueEnd(LocalDateTime.now().plusDays(2))
                .build();

        //when
        boolean result = coupon.availableIssueDate();

        //then
        Assertions.assertFalse(result);
    }

    @Test
    @DisplayName("쿠폰 발급 기간이 지났을 때")
    void availableIssueDate_false2(){

        //given
        Coupon coupon = Coupon.builder()
                .issueSt(LocalDateTime.now().minusDays(2))
                .issueEnd(LocalDateTime.now().minusDays(1))
                .build();

        //when
        boolean result = coupon.availableIssueDate();

        //then
        Assertions.assertFalse(result);
    }

    @Test
    @DisplayName("검증 로직을 통과 한 후, 쿠폰 발급이 된다.")
    void couponIssue_1(){
        //given
        Coupon coupon = Coupon.builder()
                .totalCnt(100)
                .issueCnt(99)
                .issueSt(LocalDateTime.now().minusDays(1))
                .issueEnd(LocalDateTime.now().plusDays(2))
                .build();

        //when
        coupon.couponIssue();

        //then
        Assertions.assertEquals(coupon.getIssueCnt(), 100);
    }

    @Test
    @DisplayName("발급수량을 초과하면 예외 발생")
    void couponIssue_2(){
        //given
        Coupon coupon = Coupon.builder()
                .totalCnt(100)
                .issueCnt(100)
                .issueSt(LocalDateTime.now().minusDays(1))
                .issueEnd(LocalDateTime.now().plusDays(2))
                .build();

        //when & then
        CouponIssueException exception = Assertions.assertThrows(CouponIssueException.class, () -> coupon.couponIssue());
        Assertions.assertEquals(exception.getErrorCode(), ErrorCode.INVALID_COUPON_ISSUE_CNT);
    }


    @Test
    @DisplayName("발급기한이 아니면 예외 발생")
    void couponIssue_3(){
        //given
        Coupon coupon = Coupon.builder()
                .totalCnt(100)
                .issueCnt(99)
                .issueSt(LocalDateTime.now().plusDays(1))
                .issueEnd(LocalDateTime.now().plusDays(2))
                .build();

        //when & then
        CouponIssueException exception = Assertions.assertThrows(CouponIssueException.class, () -> coupon.couponIssue());
        Assertions.assertEquals(exception.getErrorCode(), ErrorCode.INVALID_COUPON_ISSUE_DATE);
    }
}