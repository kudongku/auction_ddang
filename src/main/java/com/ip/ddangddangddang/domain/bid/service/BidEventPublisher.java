package com.ip.ddangddangddang.domain.bid.service;

import com.ip.ddangddangddang.global.redis.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BidEventPublisher {

    private final CacheService cacheService;
    private final static String EVENT_TOPIC = "auction-price:";

    public void publishEvent(Long auctionId, String event) {
        cacheService.publishEvent(EVENT_TOPIC, auctionId, event);
    }
}
