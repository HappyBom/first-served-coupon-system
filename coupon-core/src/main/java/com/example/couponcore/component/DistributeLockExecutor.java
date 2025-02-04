package com.example.couponcore.component;

import io.lettuce.core.RedisClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DistributeLockExecutor {

    private final RedisClient redisClient;

    public void execute(Runnable logic){
        logic.run();
    }

}
