package com.ip.ddangddangddang.domain.auction.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ip.ddangddangddang.common.ControllerTest;
import com.ip.ddangddangddang.domain.auction.dto.request.AuctionRequestDto;
import com.ip.ddangddangddang.domain.auction.service.AuctionService;
import com.ip.ddangddangddang.domain.auction.values.AuctionServiceTestValues;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest(controllers = AuctionController.class)
class AuctionControllerTest extends ControllerTest implements AuctionServiceTestValues {

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
                .createAuction(any(AuctionRequestDto.class), eq(
                    AuctionServiceTestValues.TEST_USER1_ID));

        }
    }

    @Nested
    @DisplayName("옥션 삭제 테스트")
    class DeleteAuctionTest {

        @Test
        void 옥션_삭제_테스트() throws Exception {
            mockMvc.perform(delete("/v1/auctions/{auctionId}", TEST_TOWN1_AUCTION1_ID))
                .andExpect(status().isOk())
                .andDo(print());
        }
    }

    @Nested
    @DisplayName("옥션 전체 조회 테스트")
    class GetAuctionsTest {

        @Test
        void 옥션_전체_조회_테스트() throws Exception {
            mockMvc.perform(get("/v1/auctions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("page", String.valueOf(PAGE_NUMBER))
                    .param("size", String.valueOf(PAGE_SIZE)))
                .andExpect(status().isOk())
                .andDo(print());
        }
    }

    @Nested
    @DisplayName("옥션 단건 조회 테스트")
    class GetAuctionsByIdTest {

        @Test
        void 옥션_단건_조회_테스트() throws Exception {
            mockMvc.perform(get("/v1/auctions/{auctionId}", TEST_TOWN1_AUCTION1_ID))
                .andExpect(status().isOk())
                .andDo(print());
        }
    }


}