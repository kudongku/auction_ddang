package com.ip.ddangddangddang.domain.auction.controller;

import static com.ip.ddangddangddang.domain.auction.values.AuctionServiceTestValues.TEST_AUCTION_REQUEST_DTO1;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ip.ddangddangddang.common.ControllerTest;
import com.ip.ddangddangddang.domain.auction.dto.request.AuctionRequestDto;
import com.ip.ddangddangddang.domain.auction.service.AuctionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest(controllers = AuctionController.class)
class AuctionControllerTest extends ControllerTest {

    @MockBean
    private AuctionService auctionService;

    @Nested
    @DisplayName("옥션 생성 테스트")
    class CreateAuctionTest {

        @Test
        void 옥션_생성_테스트() throws Exception {
            mockMvc.perform(post("/v1/auctions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(TEST_AUCTION_REQUEST_DTO1)))
                .andExpect(status().isOk())
                .andDo(print());

            then(auctionService).should(times(1))
                .createAuction(any(AuctionRequestDto.class), eq(TEST_USER1_ID));

        }
    }

}