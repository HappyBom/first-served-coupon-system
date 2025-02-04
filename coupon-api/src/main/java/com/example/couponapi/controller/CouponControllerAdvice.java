package com.example.couponapi.controller;

import com.example.couponapi.controller.dto.CouponIssueResponseDto;
import com.example.couponcore.exception.CouponIssueException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class CouponControllerAdvice {

    //해당 예외가 발생 했을 때, 예외를 잡아서 CouponIssueResponseDto 형태로 클라이언트에게 응답
    @ExceptionHandler(CouponIssueException.class)
    public CouponIssueResponseDto couponIssueExceptionHandler(CouponIssueException exception){
        log.error("[ExceptionHandler] {}", exception.getMessage());
        return new CouponIssueResponseDto(false, exception.getErrorCode().message);
    }

}
