package com.example.couponcore.util;

public class CouponRedisUtil {

    //역할 : 쿠폰 수량/중복 발급 체크
    public static String getIssueRequestKey(long couponId){
        return "issue.request.couponId=%s".formatted(couponId);
    }

    //역할 : 쿠폰 발급 요청 저장
    public static String getIssueRequestQueueKey(){
        return "issue.request";
    }

}
