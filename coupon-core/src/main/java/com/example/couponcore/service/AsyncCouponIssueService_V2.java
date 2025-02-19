package com.example.couponcore.service;

import com.example.couponcore.repository.redis.dto.CouponRedisEntity;
import com.example.couponcore.repository.redis.RedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class AsyncCouponIssueService_V2 {

    private final RedisRepository redisRepository;
    private final CouponCacheService couponCacheService;

    public void issue(long couponId, long userId){

        CouponRedisEntity coupon = couponCacheService.getLocalCouponCache(couponId);
        coupon.checkIssueAbleCoupon();
        issueRequest(couponId, userId, coupon.totalCnt());
    }

    public void issueRequest(long couponId, long userId, Integer totalCnt){

        if(totalCnt == null){
            redisRepository.issueRequest(couponId, userId, Integer.MAX_VALUE);  //검증 우회
        }
        redisRepository.issueRequest(couponId, userId, totalCnt);
    }

}
