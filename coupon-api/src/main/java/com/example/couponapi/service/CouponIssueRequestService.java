package com.example.couponapi.service;

import com.example.couponapi.controller.dto.CouponIssueRequestDto;
import com.example.couponcore.service.CouponIssueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CouponIssueRequestService {

    private final CouponIssueService couponIssueService;

    public void issueRequest_V1(CouponIssueRequestDto requestDto){
        synchronized (this){
            couponIssueService.issue(requestDto.couponId(), requestDto.userId());
            log.info("쿠폰 발급 완료! couponId : %s, userId : %s".formatted(requestDto.couponId(), requestDto.userId()));
        }
    }


}
