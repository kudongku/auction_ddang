package com.ip.ddangddangddang.domain.bid.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.ip.ddangddangddang.domain.bid.values.BidTestValues;
import com.ip.ddangddangddang.global.redis.CacheService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BidEventPublisherTest implements BidTestValues {

    @InjectMocks
    private BidEventPublisher bidEventPublisher;
    @Mock
    private CacheService cacheService;

    @Nested
    @DisplayName("이벤트 발생 테스크")
    public class publishEventTest {

        @Test
        void 성공_테스트() {
            //when
            bidEventPublisher.publishEvent(TEST_AUCTION1_ID, TEST_EVENT);

            //then
            then(cacheService).should(times(1))
                .publishEvent(anyString(), anyLong(), anyString());
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
