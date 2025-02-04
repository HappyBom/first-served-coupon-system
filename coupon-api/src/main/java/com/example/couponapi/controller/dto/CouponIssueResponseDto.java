package com.example.couponapi.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(value = JsonInclude.Include.NON_NULL)  //null인 데이터 제외
public record CouponIssueResponseDto(boolean isSuccess, String errMsg) {
}
