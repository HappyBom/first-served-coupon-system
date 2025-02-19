package com.example.couponcore.repository.postgres;

import com.example.couponcore.entity.CouponIssue;
import com.querydsl.jpa.JPQLQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.example.couponcore.entity.QCouponIssue.*;

@Repository
@RequiredArgsConstructor
public class CouponIssueRepository {

    private final JPQLQueryFactory queryFactory;
    public CouponIssue findUserCouponIssue(long couponId, long userId){
        return queryFactory.selectFrom(couponIssue)
                .where(couponIssue.couponId.eq(couponId))
                .where(couponIssue.userId.eq(userId))
                .fetchFirst();
    }



}
