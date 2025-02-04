package com.example.couponcore.component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


@RequiredArgsConstructor
@Slf4j
@Component  //빈으로 등록해서 boot에서 자동으로 DistributeLockExecutor 인식/관리 할 수 있도록
public class DistributeLockExecutor {

    private final RedissonClient redissonClient;

    //waitMilliSecond : 락을 획득 할 때, 최대 대기 시간(밀리초) | 1000 = 1초
    //leaseMilliSecond : 락을 획득 한 후, 얼마동안 유지할지(밀리초) | 1000 = 1초
    //Runnable  : 락을 획득한 후 실행할 비즈니스 로직을 매개변수로 받음 | 매개변수 없이 실행 가능한 작업을 정의
    public void execute(String lockName, long waitMilliSecond, long leaseMilliSecond, Runnable logic){

        //lockName을 key로 하는 락 객체(RLock)을 가져옴, Redisson에서 제공하는 분산 락 객체
        RLock lock = redissonClient.getLock(lockName);

        try{

            //waitMilliSecond 동안 락을 얻을 때까지 대기 | 획득 성공/실패 시 true/false 반환 | leaseMilliSecond 시간이 지나면 자동 락 해제
            boolean isLocked = lock.tryLock(waitMilliSecond, leaseMilliSecond, TimeUnit.MILLISECONDS);

            //락을 획득하지 못하면 예외 발생
            if(!isLocked){
                throw new IllegalStateException("[" + lockName + "] lock 획득 실패");
            }

            //락을 획득 한 후, 전달받은 비즈니스 로직 실행
            logic.run();

        }catch (InterruptedException e){

            //InterruptedException은 스레드가 실행을 중단해야 할 때 발생하는 예외 & Checked Exception으로 try-catch로 처리해야 함
            //비즈니스로직에서 체크 예외를 신경쓰고 싶지 않아 -> RuntimeException으로 감싸서 던짐 -> try-catch 없이도 예외 처리 할 수 있게 됨
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);

        }finally {

            //예외가 발생하더라도 락을 정상적으로 해제하여 다른 프로세스가 락을 사용할 수 있도록 보장해줌
            //락이 현재 스레드에 의해 유지되고 있다면, isHeldByCurrentThread(),unlock()을 호출하여 락 해제
            if(lock.isHeldByCurrentThread()){
                lock.unlock();
            }
        }
    }

}
