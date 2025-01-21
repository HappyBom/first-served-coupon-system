package com.example.couponapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }

    /**
     * RPS : 초당 처리하는 건 수
     *
     * Thread.sleep(500) 을 추가하면 API는 초당 2건 처리
     * (한 요청당 처리 시간이 0.5초 -> 1초 동안 처리 할 수 있는 건 수 2건)
     *
     * 초당 2건을 처리 * N (서버에서 동시에 처리 할 수 있는 수 = 톰캣 맥스 스레드 풀 기본 200) = 대략 RPS 400 유추 가능 함.
     *
     * application-api.yml
     * server
     *   port: 8080
     *   tomcat:
     *     threads:
     *       max: 400 으로 설정하면 -> 초당 2건을 처리 * 400 = RPS 800 으롤 유추 가능 함.
     *
     * 요청 유저수가 계속해서 증가해도 처리량이 늘어나지 않는다. 늘어나는 것은 응답시간이 늘어난다.
     * 서버에서 처리 할 수 있는 트래픽을 받았을 때, 응답 속도가 계속해서 길어지게 되고 결국에는 실패를 응답한다.
     * (이 것에 대한것은 앞으로 개발하면서..)
     * */

}
