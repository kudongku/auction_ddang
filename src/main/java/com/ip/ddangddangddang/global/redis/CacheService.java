package com.ip.ddangddangddang.global.redis;

public interface CacheService {
    void setAuctionExpiredKey(Long auctionId);

    void publishEvent(String eventTopic, Long auctionId, String event);
}
