package com.ip.ddangddangddang.domain.comment.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ip.ddangddangddang.common.ControllerTest;
import com.ip.ddangddangddang.domain.comment.dto.request.CommentCreateRequestDto;
import com.ip.ddangddangddang.domain.comment.service.CommentService;
import com.ip.ddangddangddang.domain.comment.values.CommentTestValues;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest(CommentController.class)
public class CommentControllerTest extends ControllerTest implements CommentTestValues {

    @MockBean
    private CommentService commentService;

    @Test
    void 댓글_생성_테스트() throws Exception {
        //given, when
        var action = mockMvc.perform(post("/v1/auctions/{auctionId}/comments", TEST_AUCTION1_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(TEST_REQUEST_DTO)));

        //then
        action.andExpect(status().isOk());
        verify(commentService, times(1))
            .createComment(eq(TEST_AUCTION1_ID), any(CommentCreateRequestDto.class),
                eq(TEST_SELLER_ID));
    }

    @Test
    void 댓글_조회_테스트() throws Exception {
        //given
        when(commentService.getComments(TEST_AUCTION1_ID, TEST_SELLER_ID))
            .thenReturn(TEST_COMMENT_RESPONSE_DTO_LIST);

        //when
        var action = mockMvc.perform(get("/v1/auctions/{auctionId}/comments", TEST_AUCTION1_ID)
            .contentType(MediaType.APPLICATION_JSON));

        //then
        action.andExpect(status().isOk())
            .andExpect(jsonPath("$.data").exists())
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.data").isNotEmpty())
            .andExpect(jsonPath("$.data.length()").value(TEST_COMMENT_RESPONSE_DTO_LIST.size()))
            .andExpect(jsonPath("$.data[0].nickname").value(TEST_SELLER_USER_NICKNAME))
            .andExpect(jsonPath("$.data[0].content").value(TEST_COMMENT1_TEXT));

        verify(commentService, times(1))
            .getComments(eq(TEST_AUCTION1_ID), eq(TEST_SELLER_ID));
    }

}