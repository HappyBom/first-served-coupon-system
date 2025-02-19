package com.example.couponapi.controller;

import com.example.couponapi.controller.dto.CouponIssueRequestDto;
import com.example.couponapi.controller.dto.CouponIssueResponseDto;
import com.example.couponapi.service.CouponIssueRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CouponIssueController {

    private final CouponIssueRequestService couponIssueRequestService;

    @PostMapping("/v1/issue")
    public CouponIssueResponseDto issue_V1(@RequestBody CouponIssueRequestDto requestDto){
        couponIssueRequestService.issueRequestV1(requestDto);  //동기식
        return new CouponIssueResponseDto(true, null);
    }

    @PostMapping("/v1/issue-async")
    public CouponIssueResponseDto asuncIssue_V1(@RequestBody CouponIssueRequestDto requestDto){
        couponIssueRequestService.asyncIssueRequestV1(requestDto);
        return new CouponIssueResponseDto(true, null);
    }

    @PostMapping("/v2/issue-async")
    public CouponIssueResponseDto asuncIssue_V2(@RequestBody CouponIssueRequestDto requestDto){
        couponIssueRequestService.asyncIssueRequestV2(requestDto);
        return new CouponIssueResponseDto(true, null);
    }

}
