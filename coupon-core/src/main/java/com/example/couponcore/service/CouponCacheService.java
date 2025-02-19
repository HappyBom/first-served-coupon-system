package com.example.couponcore.service;

import com.example.couponcore.entity.Coupon;
import com.example.couponcore.repository.redis.dto.CouponRedisEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class CouponCacheService {

    private final CouponIssueService couponIssueService;

    @Cacheable(cacheNames = "coupon")
    public CouponRedisEntity getCouponCache(long couponId){
        Coupon coupon = couponIssueService.findCoupon(couponId);
        log.info("쿠폰 캐시 저장 됨 {}", coupon.getId());
        return new CouponRedisEntity(coupon);
    }

    @Cacheable(cacheNames = "coupon", cacheManager = "localCacheManager")
    public CouponRedisEntity getLocalCouponCache(long couponId){
        return proxy().getCouponCache(couponId);    //로컬 캐시 있으면 로컬 캐시 반환, 없으면 레디스 캐시 데이터를 읽어와 반환하도록
    }

    private CouponCacheService proxy(){
        return ((CouponCacheService) AopContext.currentProxy());
    }

    @CachePut(cacheNames = "coupon")
    public CouponRedisEntity putCouponCache(long couponId){
        return getCouponCache(couponId);
    }

    @CachePut(cacheNames = "coupon", cacheManager = "localCacheManager")
    public CouponRedisEntity putLocalCouponCache(long couponId){
        return getLocalCouponCache(couponId);
    }
}
