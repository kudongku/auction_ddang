package com.ip.ddangddangddang.domain.auction.handler;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.ip.ddangddangddang.domain.auction.service.AuctionService;
import com.ip.ddangddangddang.domain.auction.values.AuctionServiceTestValues;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuctionEventHandlerTest implements AuctionServiceTestValues {

    @InjectMocks
    private AuctionEventHandler auctionEventHandler;

    @Mock
    private AuctionService auctionService;

    @Test
    void 이벤트_생성_테스트() {
        //given, when
        auctionEventHandler.AuctionKeyExpiredEvent(EVENT);
        //then
        then(auctionService).should(times(1))
            .updateStatusToHold(anyLong());
    }
}