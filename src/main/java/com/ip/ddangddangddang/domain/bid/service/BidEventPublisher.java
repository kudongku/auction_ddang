package com.ip.ddangddangddang.domain.bid.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BidEventPublisher {

    private final RedisTemplate<String, String> redisTemplate;
    private final static String EVENT_TOPIC = "auction-price:";

    public void publishEvent(Long auctionId, String event) {
        redisTemplate.convertAndSend(EVENT_TOPIC + auctionId, event);
    }
}
