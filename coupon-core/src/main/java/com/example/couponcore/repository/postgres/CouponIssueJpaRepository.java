package com.example.couponcore.repository.postgres;

import com.example.couponcore.entity.CouponIssue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponIssueJpaRepository extends JpaRepository<CouponIssue, Long> {
}
