package com.example.couponcore;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableAspectJAutoProxy(exposeProxy = true)
@EnableCaching
@ComponentScan
@EnableJpaAuditing  //Spring Boot의 메인 애플리케이션 클래스에서 선언 ??
@EnableAutoConfiguration
public class CouponCoreConfiguration {
}
