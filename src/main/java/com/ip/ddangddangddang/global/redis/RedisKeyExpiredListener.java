package com.ip.ddangddangddang.global.redis;

import com.ip.ddangddangddang.domain.auction.dto.request.AuctionKeyNotificationRequestDto;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
public class RedisKeyExpiredListener extends KeyExpirationEventMessageListener {

    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * Creates new {@link MessageListener} for {@code __keyEvent@*__:expired} messages.
     *
     * @param listenerContainer must not be {@literal null}.
     */
    public RedisKeyExpiredListener(
        RedisMessageListenerContainer listenerContainer,
        ApplicationEventPublisher applicationEventPublisher
    ) {
        super(listenerContainer);
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * @param message redis key
     * @param pattern __keyEvent@*__:expired
     * 만료시 알림을 받으면 이 메소드가 실행됨
     */
    @Override
    public void onMessage(Message message, byte[] pattern) { //message = "auctionId: 1"
        applicationEventPublisher.publishEvent(new AuctionKeyNotificationRequestDto(message.toString()));
    }
}
