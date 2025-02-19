package com.example.couponcore.service;

import com.example.couponcore.exception.CouponIssueException;
import com.example.couponcore.exception.ErrorCode;
import com.example.couponcore.repository.redis.dto.CouponRedisEntity;
import com.example.couponcore.repository.redis.RedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.couponcore.util.CouponRedisUtil.getIssueRequestKey;

@RequiredArgsConstructor
@Service
public class CouponIssueRedisService {

    private final RedisRepository redisRepository;

    //수량 검증
    public boolean availableTotalIssueQuantity(Integer totalQuantity, long couponId){
        if(totalQuantity == null){
            return true;
        }

        String key = getIssueRequestKey(couponId);
        return totalQuantity > redisRepository.sCard(key);  //구조 set | 크기 반환
    }

    //중복 요청 검증
    public boolean availableUserIssueQuantity(long couponId, long userId){
        String key = getIssueRequestKey(couponId);
        return !redisRepository.sIsMember(key, String.valueOf(userId));     //구조 set | 특정 값 존재 확인
    }

    public void checkCouponIssueQuantity(CouponRedisEntity couponRedisEntity, long userId){
        if(!availableTotalIssueQuantity(couponRedisEntity.totalCnt(), couponRedisEntity.id())){
            throw new CouponIssueException(ErrorCode.INVALID_COUPON_ISSUE_CNT, "발급 가능한 쿠폰 수량을 초과하였습니다. couponId : %s, userId : %s".formatted(couponRedisEntity.id(), userId));
        }

        if(!availableUserIssueQuantity(couponRedisEntity.id(), userId)){
            throw new CouponIssueException(ErrorCode.DUPLICATED_COUPON_ISSUE, "이미 쿠폰 발급 요청이 처리되었습니다. couponId : %s, userId : %s".formatted(couponRedisEntity.id(), userId));
        }
    }
}
