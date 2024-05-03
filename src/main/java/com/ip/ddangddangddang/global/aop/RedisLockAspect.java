package com.ip.ddangddangddang.global.aop;

import com.ip.ddangddangddang.global.exception.custom.TimeOutLockException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Slf4j(topic = "RedisLockAspect")
@Aspect
@AllArgsConstructor
@Component
public class RedisLockAspect {

    private final RedissonClient redissonClient;
    private final AopForTransaction aopForTransaction;

    @Around("@annotation(distributedLock)")
    public Object applyLock(
        ProceedingJoinPoint joinPoint,
        DistributedLock distributedLock
    ) throws Throwable {
        RLock lock = redissonClient.getFairLock(distributedLock.value());

        try {
            boolean isLockSuccess = lock.tryLock(
                distributedLock.waitTime(),
                distributedLock.leaseTime(),
                distributedLock.timeUnit()
            );

            log.info("Lock 획득 성공");

            if (!isLockSuccess) {
                throw new TimeOutLockException("Lock 획득 실패");
            }

            return aopForTransaction.proceed(joinPoint);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new TimeOutLockException("Lock 획득 시 Interrupt 발생");
        } finally {
            log.info("Lock 해제 성공");
            lock.unlock();
        }

    }

}
