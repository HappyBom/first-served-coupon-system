package com.example.couponcore.repository.redis.dto;

import com.example.couponcore.entity.Coupon;
import com.example.couponcore.entity.CouponGubun;
import com.example.couponcore.exception.CouponIssueException;
import com.example.couponcore.exception.ErrorCode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;

public record CouponRedisEntity(

        Long id,
        CouponGubun couponGubun,
        Integer totalCnt,
        Boolean availableIssueCnt,

        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        LocalDateTime issueSt,

        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        LocalDateTime issueEnd

) {

    public CouponRedisEntity(Coupon coupon){
        this(
                coupon.getId(),
                coupon.getCouponGubun(),
                coupon.getTotalCnt(),
                coupon.availableIssueCoupon(),
                coupon.getIssueSt(),
                coupon.getIssueEnd()
        );
    }

    private boolean availableIssueDate(){
        LocalDateTime now = LocalDateTime.now();
        return issueSt.isBefore(now) && issueEnd().isAfter(now);
    }

    public void checkIssueAbleCoupon(){

        if(!availableIssueCnt){
            throw new CouponIssueException(ErrorCode.INVALID_COUPON_ISSUE_DATE, "발급 가능한 쿠폰 수량이 모두 소진되었습니다.. couponId : %s".formatted(id));
        }

        if(!availableIssueDate()){
            throw new CouponIssueException(ErrorCode.INVALID_COUPON_ISSUE_DATE, "발급 가능한 일자가 아닙니다. couponId : %s, issueSt : %s, issueEnd : %s".formatted(id, issueSt, issueEnd));
        }

    }

}
