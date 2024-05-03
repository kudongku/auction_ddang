package com.ip.ddangddangddang.global.redis;

import com.ip.ddangddangddang.domain.auction.event.AuctionKeyExpiredEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
public class RedisKeyExpiredListener extends KeyExpirationEventMessageListener {

    private final ApplicationEventPublisher applicationEventPublisher;

    public RedisKeyExpiredListener(
        RedisMessageListenerContainer listenerContainer,
        ApplicationEventPublisher applicationEventPublisher
    ) {
        super(listenerContainer);
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String messageToStr = message.toString();

        if (messageToStr.startsWith("auctionId:")) {
            Long auctionId = Long.parseLong(messageToStr.split(":")[1]);
            applicationEventPublisher.publishEvent(new AuctionKeyExpiredEvent(auctionId));
        }

    }
}

