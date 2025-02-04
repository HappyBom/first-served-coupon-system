package com.example.couponcore.repository;

import com.example.couponcore.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CouponJpaRepository extends JpaRepository<Coupon, Long> {
}
