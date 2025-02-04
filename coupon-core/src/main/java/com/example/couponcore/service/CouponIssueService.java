package com.example.couponcore.service;

import com.example.couponcore.entity.Coupon;
import com.example.couponcore.entity.CouponIssue;
import com.example.couponcore.exception.CouponIssueException;
import com.example.couponcore.repository.CouponIssueJpaRepository;
import com.example.couponcore.repository.CouponIssueRepository;
import com.example.couponcore.repository.CouponJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.couponcore.exception.ErrorCode.COUPON_NOT_EXIST;
import static com.example.couponcore.exception.ErrorCode.DUPLICATED_COUPON_ISSUE;

@Service
@RequiredArgsConstructor
@Transactional
public class CouponIssueService {

    private final CouponIssueRepository couponIssueRepository;
    private final CouponIssueJpaRepository couponIssueJpaRepository;
    private final CouponJpaRepository couponJpaRepository;

    //쿠폰 발급
    public void issue(long couponId, long userId){
        Coupon coupon = findCoupon(couponId);   //쿠폰 검증
        coupon.couponIssue();   //쿠폰 발급 (수량/일자 검증, 발급 수량 +1)
        saveCouponIssue(couponId, userId);
    }

    public void checkAlreadyUserCouponIssue(long couponId, long userId){

        CouponIssue userCouponIssue = couponIssueRepository.findUserCouponIssue(couponId, userId);

        //해당 유저에게 이미 발급된 쿠폰이 존재
        if(userCouponIssue != null){
            throw new CouponIssueException(DUPLICATED_COUPON_ISSUE, "이미 발급된 쿠폰이 존재합니다. userId : %s, couponId : %s".formatted(userId, couponId));
        }
    }

    private CouponIssue saveCouponIssue(long couponId, long userId) {

        checkAlreadyUserCouponIssue(couponId, userId);  //해당 유저 쿠폰 발급 체크

        CouponIssue issue = CouponIssue.builder()
                .couponId(couponId)
                .userId(userId)
                .build();

        return couponIssueJpaRepository.save(issue);
    }

    @Transactional(readOnly = true)
    public Coupon findCoupon(long couponId) {
        return couponJpaRepository.findById(couponId)
                .orElseThrow(() -> new CouponIssueException(COUPON_NOT_EXIST, "쿠폰 정책이 존재하지 않습니다. couponId : %s".formatted(couponId)));

    }
}
