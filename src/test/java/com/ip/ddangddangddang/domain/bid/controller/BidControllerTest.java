package com.ip.ddangddangddang.domain.bid.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ip.ddangddangddang.common.ControllerTest;
import com.ip.ddangddangddang.domain.bid.dto.request.BidRequestDto;
import com.ip.ddangddangddang.domain.bid.service.BidService;
import com.ip.ddangddangddang.domain.bid.values.BidTestValues;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(controllers = BidController.class)
class BidControllerTest extends ControllerTest implements BidTestValues {

    @MockBean
    private BidService bidService;

    @Test
    public void 입찰_생성_테스트() throws Exception {
        var bid = mockMvc.perform(
            MockMvcRequestBuilders.post("/v1/auctions/{auctionId}/bids", TEST_AUCTION1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TEST_PROPER_REQUEST_DTO)));

        bid.andExpect(status().isOk());
        verify(bidService, times(1))
            .createBid(eq(TEST_AUCTION1_ID), any(BidRequestDto.class), eq(TEST_USER1_ID));
    }

}
