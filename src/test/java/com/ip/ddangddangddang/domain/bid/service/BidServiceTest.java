package com.ip.ddangddangddang.domain.bid.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ip.ddangddangddang.domain.auction.service.AuctionService;
import com.ip.ddangddangddang.domain.bid.entity.Bid;
import com.ip.ddangddangddang.domain.bid.repository.BidRepository;
import com.ip.ddangddangddang.domain.bid.values.BidTestValues;
import com.ip.ddangddangddang.global.exception.customedExceptions.InvalidBidException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BidServiceTest implements BidTestValues {

    @InjectMocks
    private BidService bidService;
    @Mock
    private BidRepository bidRepository;
    @Mock
    private AuctionService auctionService;
    @Mock
    private BidEventPublisher bidEventPublisher;
    @Spy
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("비드 생성 테스크")
    public class BidCreateTest {

        @Test
        void 성공_테스트() throws JsonProcessingException {
            //given
            when(auctionService.findAuctionById(TEST_AUCTION1_ID))
                .thenReturn(TEST_ON_SALE_AUCTION);
            doNothing().when(bidEventPublisher).publishEvent(anyLong(), anyString());
            doReturn("json").when(objectMapper).writeValueAsString(any());

            //when
            bidService.createBid(TEST_AUCTION1_ID, TEST_PROPER_REQUEST_DTO, TEST_BUYER_ID);

            //then
            verify(auctionService, times(1)).findAuctionById(TEST_AUCTION1_ID);
            verify(bidEventPublisher, times(1)).publishEvent(TEST_AUCTION1_ID, "json");
            verify(bidRepository, times(1)).save(any(Bid.class));
        }

        @Test
        void 실패_테스트_입찰기간_종료() {
            //given
            when(auctionService.findAuctionById(TEST_AUCTION2_ID))
                .thenReturn(TEST_HOLD_AUCTION);

            //when, then
            assertThrows(
                InvalidBidException.class,
                () -> bidService.createBid(
                    TEST_AUCTION2_ID,
                    TEST_PROPER_REQUEST_DTO,
                    TEST_BUYER_ID
                ),
                "입찰 기간이 종료되었습니다."
            );
        }

        @Test
        void 실패_테스트_판매자는_입찰X() {
            //given
            when(auctionService.findAuctionById(TEST_AUCTION1_ID))
                .thenReturn(TEST_ON_SALE_AUCTION);

            //when, then
            assertThrows(
                InvalidBidException.class,
                () -> bidService.createBid(
                    TEST_AUCTION1_ID,
                    TEST_PROPER_REQUEST_DTO,
                    TEST_SELLER_ID
                ),
                "본인의 게시글은 입찰을 할 수 없습니다."
            );

        }

        @Test
        void 실패_테스트_입찰가가_낮은_경우() {
            //given
            when(auctionService.findAuctionById(TEST_AUCTION1_ID))
                .thenReturn(TEST_ON_SALE_AUCTION);

            //when, then
            assertThrows(
                InvalidBidException.class,
                () -> bidService.createBid(
                    TEST_AUCTION1_ID,
                    TEST_SAME_PRICE_REQUEST_DTO,
                    TEST_BUYER_ID
                ),
                "현재 가격보다 높은 가격을 입력해주세요."
            );

        }

    }

}
