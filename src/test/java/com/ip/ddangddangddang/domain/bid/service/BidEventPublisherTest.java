package com.ip.ddangddangddang.domain.bid.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

import com.ip.ddangddangddang.domain.bid.values.BidServiceTestValues;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;

@ExtendWith(MockitoExtension.class)
class BidEventPublisherTest implements BidServiceTestValues {

    @InjectMocks
    private BidEventPublisher bidEventPublisher;
    @Mock
    private RedisTemplate<String, Object> redisTemplate;
    //    @Captor
//    private
//    @Captor
//    private
    private final ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
    private final ArgumentCaptor<String> eventCaptor = ArgumentCaptor.forClass(String.class);

    @Nested
    @DisplayName("이벤트 발생 테스크")
    public class publishEventTest {

        @Test
        void 성공_테스트() {
            //when
            bidEventPublisher.publishEvent(TEST_AUCTION1_ID, TEST_EVENT);

            //then
            verify(redisTemplate).convertAndSend(topicCaptor.capture(), eventCaptor.capture());
            assertEquals(TEST_TOPIC, topicCaptor.getValue());
            assertEquals(TEST_EVENT, eventCaptor.getValue());
        }

        @Test
        void 실패_테스트_옥션아이디가_NULL() {
            //when
            bidEventPublisher.publishEvent(null, TEST_EVENT);

            //then
            verify(redisTemplate).convertAndSend(topicCaptor.capture(), eventCaptor.capture());
            assertEquals("auction-price:null", topicCaptor.getValue());
        }

        @Test
        void 실패_테스트_이벤트가_NULL() {
            //when
            bidEventPublisher.publishEvent(TEST_AUCTION1_ID, null);

            //then
            verify(redisTemplate).convertAndSend(topicCaptor.capture(), eventCaptor.capture());
            assertNull(eventCaptor.getValue());
        }

        @Test
        void 실패_테스트_레디스가_NULL() {
            BidEventPublisher nullBidEventPublisher = new BidEventPublisher(null);

            //then
            assertThrows(NullPointerException.class,
                () -> nullBidEventPublisher.publishEvent(TEST_AUCTION1_ID, TEST_EVENT));
        }

    }

}
