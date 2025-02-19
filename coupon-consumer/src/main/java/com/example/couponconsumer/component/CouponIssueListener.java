package com.example.couponconsumer.component;

import com.example.couponcore.repository.redis.RedisRepository;
import com.example.couponcore.repository.redis.dto.CouponIssueRequest;
import com.example.couponcore.service.CouponIssueService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.example.couponcore.util.CouponRedisUtil.getIssueRequestQueueKey;

@Slf4j
@RequiredArgsConstructor
@EnableScheduling
@Component
public class CouponIssueListener {

    private final RedisRepository redisRepository;
    private final CouponIssueService couponIssueService;

    private final String issueRequestQueueKey = getIssueRequestQueueKey();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Scheduled(fixedDelay = 1000L)  //이전 작업 종료 후 1초 뒤 실행
    public void issue() throws JsonProcessingException {

        log.info("listen!!!");

        while(existCouponIssueTarget()){    //발급 대상 확인

            CouponIssueRequest target = getIssueTarget();

            log.info("쿠폰 발급 START target : %s".formatted(target));
            couponIssueService.issue(target.couponId(), target.userId());
            log.info("쿠폰 발급 END target : %s".formatted(target));

            removeIssuedTarget();    //읽었으니 pop!
        }
    }

    private boolean existCouponIssueTarget(){
        return redisRepository.lSize(issueRequestQueueKey) > 0;
    }

    private CouponIssueRequest getIssueTarget() throws JsonProcessingException {
        return objectMapper.readValue(redisRepository.lIndex(issueRequestQueueKey, 0), CouponIssueRequest.class);
    }

    private void removeIssuedTarget() {
        redisRepository.lPop(issueRequestQueueKey);
    }

}
