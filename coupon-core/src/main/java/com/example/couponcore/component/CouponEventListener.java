package com.example.couponcore.component;

import com.example.couponcore.entity.event.CouponIssueCompleteEvent;
import com.example.couponcore.service.CouponCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@RequiredArgsConstructor
@Component
public class CouponEventListener {

    private final CouponCacheService couponCacheService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void issueComplete(CouponIssueCompleteEvent event){

        log.info("issue complete! cache refresh start! couponId : %s".formatted(event.couponId()));
        couponCacheService.putCouponCache(event.couponId());
        couponCacheService.putLocalCouponCache(event.couponId());
        log.info("issue complete! cache refresh end! couponId : %s".formatted(event.couponId()));
    }


}
