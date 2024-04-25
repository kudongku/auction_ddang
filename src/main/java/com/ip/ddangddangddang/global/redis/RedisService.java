package com.ip.ddangddangddang.global.redis;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Slf4j(topic = "RedisService")
@RequiredArgsConstructor
@Component
public class RedisService implements CacheService {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void setAuctionExpiredKey(Long auctionId) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        String redisKey = "auctionId:" + auctionId;
        operations.set(redisKey, "1");
        redisTemplate.expire(redisKey, 5, TimeUnit.MINUTES);
        log.info("경매 등록, " + redisKey);
    }

    @Override
    public void publishEvent(String eventTopic, Long auctionId, String event) {
        redisTemplate.convertAndSend(eventTopic + auctionId, event);
    }
}
