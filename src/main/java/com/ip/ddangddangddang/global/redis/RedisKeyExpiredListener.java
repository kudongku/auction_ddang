package com.ip.ddangddangddang.global.redis;

import com.ip.ddangddangddang.domain.auction.event.AuctionKeyExpiredEvent;
import java.util.Properties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class RedisKeyExpiredListener extends KeyExpirationEventMessageListener {

    private final RedisMessageListenerContainer redisMessageListenerContainer;
    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * Creates new {@link MessageListener} for {@code __keyEvent@*__:expired} messages. must not be
     * {@literal null}.
     */
    public RedisKeyExpiredListener(
        RedisMessageListenerContainer listenerContainer,
        ApplicationEventPublisher applicationEventPublisher
    ) {
        super(listenerContainer);
        this.redisMessageListenerContainer = listenerContainer;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void init() {
        if (StringUtils.hasText("Ex")) {
            RedisConnection connection = this.redisMessageListenerContainer.getConnectionFactory().getConnection();

            try {
                Properties config = connection.getConfig("notify-keyspace-events");
                if (!StringUtils.hasText(config.getProperty("notify-keyspace-events"))) {
                    connection.setConfig("notify-keyspace-events", "Ex");
                }
            } finally {
                connection.close();
            }
        }

        this.doRegister(this.redisMessageListenerContainer);
    }

    /**
     * @param message redis key
     * @param pattern __keyEvent@*__:expired 만료시 알림을 받으면 이 메소드가 실행됨
     */
    @Override
    public void onMessage(Message message, byte[] pattern) { //message = "auctionId: 1"
        String messageToStr = message.toString();

        if (messageToStr.startsWith("auctionId:")) {
            // messageToStr = "auctionId: 1", string
            // message.split(" ") = {"auctionId:", "1"}, Array<String>
            // message.split(" ")[1] = "1", string
            // Long.parseLong(message.split(" ")[1]) = 1L, Long
            Long auctionId = Long.parseLong(messageToStr.split(":")[1]);
            applicationEventPublisher.publishEvent(new AuctionKeyExpiredEvent(auctionId));
        }

    }
}

