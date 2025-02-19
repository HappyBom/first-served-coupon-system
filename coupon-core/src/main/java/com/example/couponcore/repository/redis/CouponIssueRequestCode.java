package com.example.couponcore.repository.redis;

import com.example.couponcore.exception.CouponIssueException;
import com.example.couponcore.exception.ErrorCode;

public enum CouponIssueRequestCode {
    SUCCESS(1),
    DUPLICATED_COUPON_ISSUE(2),
    INVALID_COUPON_ISSUE_CNT(3);

    CouponIssueRequestCode(int code){

    }

    public static CouponIssueRequestCode find(String code){
        int codeValue = Integer.parseInt(code);
        if(codeValue == 1) return SUCCESS;
        if(codeValue == 2) return DUPLICATED_COUPON_ISSUE;
        if(codeValue == 3) return INVALID_COUPON_ISSUE_CNT;
        throw new IllegalArgumentException("존재하지 않는 코드입니다. %s".formatted(code));
    }

    public static void checkRequestResult(CouponIssueRequestCode code){
        if(code == INVALID_COUPON_ISSUE_CNT){
            throw new CouponIssueException(ErrorCode.INVALID_COUPON_ISSUE_CNT, "쿠폰 발급 가능한 수량을 초과하였습니다.");
        }
        if(code == DUPLICATED_COUPON_ISSUE){
            throw new CouponIssueException(ErrorCode.DUPLICATED_COUPON_ISSUE, "이미 발급된 쿠폰입니다.");
        }
    }


}
