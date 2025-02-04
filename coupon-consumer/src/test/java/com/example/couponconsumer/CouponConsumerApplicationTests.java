package com.example.couponconsumer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@ActiveProfiles("test")
@TestPropertySource(properties = "spring.config.name=application-core") //이 파일을 로드함
@SpringBootTest
class CouponConsumerApplicationTests {

    @Test
    void contextLoads() {
    }

}
