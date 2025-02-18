package com.example.couponcore.service;

import com.example.couponcore.component.DistributeLockExecutor;
import com.example.couponcore.entity.Coupon;
import com.example.couponcore.exception.CouponIssueException;
import com.example.couponcore.exception.ErrorCode;
import com.example.couponcore.repository.redis.CouponRedisEntity;
import com.example.couponcore.repository.redis.RedisRepository;
import com.example.couponcore.repository.redis.dto.CouponIssueRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.example.couponcore.util.CouponRedisUtil.getIssueRequestKey;
import static com.example.couponcore.util.CouponRedisUtil.getIssueRequestQueueKey;

@Service
@RequiredArgsConstructor
@Slf4j
public class AsyncCouponIssueService {

    private final RedisRepository redisRepository;
    private final CouponIssueRedisService couponIssueRedisService;
    private final CouponIssueService couponIssueService;
    private final DistributeLockExecutor distributeLockExecutor;

    private final CouponCacheService couponCacheService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void issue(long couponId, long userId){

        CouponRedisEntity coupon = couponCacheService.getCouponCache(couponId);
        coupon.checkIssueAbleCoupon();

        distributeLockExecutor.execute("lock_&s".formatted(couponId), 10000, 10000, () -> {

            //쿠폰 발급이 가능한지 수량 확인
            couponIssueRedisService.checkCouponIssueQuantity(coupon, userId);

            //쿠폰 발급
            issueRequest(couponId, userId);

        });
    }
    
    //레디스 캐시 적용 전
    /*public void issue(long couponId, long userId){

        Coupon coupon = couponIssueService.findCoupon(couponId);

        //쿠폰 발급 기간 검증
        if(!coupon.availableIssueDate()){
            throw new CouponIssueException(ErrorCode.INVALID_COUPON_ISSUE_DATE, "발급 가능한 일자가 아닙니다. couponId : %s, issueSt : %s, issueEnd : %s".formatted(couponId, coupon.getIssueSt(), coupon.getIssueEnd()));
        }

        distributeLockExecutor.execute("lock_&s".formatted(couponId), 10000, 10000, () -> {


            //쿠폰 발급이 가능한지 수량 확인
            if(!couponIssueRedisService.availableTotalIssueQuantity(coupon.getTotalCnt(), couponId)){
                throw new CouponIssueException(ErrorCode.INVALID_COUPON_ISSUE_CNT, "발급 가능한 쿠폰 수량을 초과하였습니다. couponId : %s, userId : %s".formatted(couponId, userId));
            }

            if(!couponIssueRedisService.availableUserIssueQuantity(couponId, userId)){
                throw new CouponIssueException(ErrorCode.DUPLICATED_COUPON_ISSUE, "이미 쿠폰 발급 요청이 처리되었습니다. couponId : %s, userId : %s".formatted(couponId, userId));
            }

            //쿠폰 발급
            issueRequest(couponId, userId);
        });
    }*/

    public void issueRequest(long couponId, long userId){

        CouponIssueRequest issueRequest = new CouponIssueRequest(couponId, userId);

        try{

            String value = objectMapper.writeValueAsString(issueRequest);
            redisRepository.sAdd(getIssueRequestKey(couponId), String.valueOf(userId));     //검증 후, 요소 추가
            redisRepository.rPush(getIssueRequestQueueKey(), value);    //구조 : 리스트 | 데이터 추가

        }catch (JsonProcessingException e){

            throw new CouponIssueException(ErrorCode.FAIL_COUPON_ISSUE_REQUEST, "Data : %s".formatted(issueRequest));
        }
    }

}
