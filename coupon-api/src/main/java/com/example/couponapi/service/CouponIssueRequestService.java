package com.example.couponapi.service;

import com.example.couponapi.controller.dto.CouponIssueRequestDto;
import com.example.couponcore.component.DistributeLockExecutor;
import com.example.couponcore.service.AsyncCouponIssueService_V1;
import com.example.couponcore.service.AsyncCouponIssueService_V2;
import com.example.couponcore.service.CouponIssueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CouponIssueRequestService {

    private final CouponIssueService couponIssueService;
    private final DistributeLockExecutor distributeLockExecutor;
    private final AsyncCouponIssueService_V1 asyncCouponIssueServiceV1;
    private final AsyncCouponIssueService_V2 asyncCouponIssueServiceV2;

    //2. redis 락
    /*public void issueRequestV1(CouponIssueRequestDto requestDto){
        distributeLockExecutor.execute("lock" + requestDto.couponId(), 10000, 10000, () -> {
            couponIssueService.issue(requestDto.couponId(), requestDto.userId());
            log.info("쿠폰 발급 완료! couponId : %s, userId : %s".formatted(requestDto.couponId(), requestDto.userId()));
        });
    }*/
    //() : 매개변수 없음, ->{} : 실행할 코드 블록 정의


    //1. 비관적 락(PESSMISTIC LOCK)
    public void issueRequestV1(CouponIssueRequestDto requestDto){
        couponIssueService.issue(requestDto.couponId(), requestDto.userId());
        log.info("쿠폰 발급 완료! couponId : %s, userId : %s".formatted(requestDto.couponId(), requestDto.userId()));
    }

    public void asyncIssueRequestV1(CouponIssueRequestDto requestDto){
        asyncCouponIssueServiceV1.issue(requestDto.couponId(), requestDto.userId());
        log.info("쿠폰 발급 완료! couponId : %s, userId : %s".formatted(requestDto.couponId(), requestDto.userId()));
    }

    public void asyncIssueRequestV2(CouponIssueRequestDto requestDto){
        asyncCouponIssueServiceV2.issue(requestDto.couponId(), requestDto.userId());
        log.info("쿠폰 발급 완료! couponId : %s, userId : %s".formatted(requestDto.couponId(), requestDto.userId()));
    }
}
