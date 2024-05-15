package com.ip.ddangddangddang.domain.comment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ip.ddangddangddang.domain.auction.service.AuctionService;
import com.ip.ddangddangddang.domain.comment.dto.response.CommentReadResponseDto;
import com.ip.ddangddangddang.domain.comment.repository.CommentRepository;
import com.ip.ddangddangddang.domain.comment.values.CommentTestValues;
import com.ip.ddangddangddang.domain.user.service.UserService;
import com.ip.ddangddangddang.global.exception.customedExceptions.InvalidAuthorityException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest implements CommentTestValues {

    @InjectMocks
    private CommentService commentService;
    @Mock
    private AuctionService auctionService;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private UserService userService;

    @Nested
    @DisplayName("댓글 생성 테스크")
    public class CommentCreateTest {

        @Test
        void 성공_테스트_작성자가_판매자() {
            //given
            when(userService.findUserById(TEST_SELLER_ID))
                .thenReturn(TEST_SELLER_USER);
            when(auctionService.findAuctionById(TEST_AUCTION1_ID))
                .thenReturn(TEST_AUCTION);

            //when
            commentService.createComment(TEST_AUCTION1_ID, TEST_REQUEST_DTO, TEST_SELLER_ID);

            //then
            verify(userService, times(1)).findUserById(TEST_SELLER_ID);
            verify(auctionService, times(1)).findAuctionById(TEST_AUCTION1_ID);
        }

        @Test
        void 성공_테스트_작성자가_낙찰자() {
            //given
            when(userService.findUserById(TEST_BUYER_ID))
                .thenReturn(TEST_BUYER_USER);
            when(auctionService.findAuctionById(TEST_AUCTION1_ID))
                .thenReturn(TEST_AUCTION);

            //when
            commentService.createComment(TEST_AUCTION1_ID, TEST_REQUEST_DTO, TEST_BUYER_ID);

            //then
            verify(userService, times(1)).findUserById(TEST_BUYER_ID);
            verify(auctionService, times(1)).findAuctionById(TEST_AUCTION1_ID);
        }

        @Test
        void 실패_테스트_작성자가_판매자X_그리고_낙찰자X() {
            //given
            when(userService.findUserById(TEST_STRANGER_ID))
                .thenReturn(TEST_STRANGER_USER);
            when(auctionService.findAuctionById(TEST_AUCTION1_ID))
                .thenReturn(TEST_AUCTION);

            //when, then
            assertThrows(
                InvalidAuthorityException.class,
                () -> commentService.createComment(
                    TEST_AUCTION1_ID,
                    TEST_REQUEST_DTO,
                    TEST_STRANGER_ID
                ),
                "댓글 권한이 없습니다."
            );
        }

    }

    @Nested
    @DisplayName("댓글 조회 테스크")
    public class GetCommentTest {

        @Test
        void 성공_테스트_유저가_판매자() {
            //given
            when(auctionService.findAuctionById(TEST_AUCTION1_ID))
                .thenReturn(TEST_AUCTION);
            when(commentRepository.findAllByAuctionId(TEST_AUCTION1_ID))
                .thenReturn(TEST_COMMENT_LIST);

            //when
            List<CommentReadResponseDto> result = commentService.getComments(TEST_AUCTION1_ID,
                TEST_SELLER_ID);

            //then
            assertEquals(TEST_COMMENT_LIST.size(), result.size());
            assertEquals(TEST_COMMENT_LIST.get(0).getContent(), result.get(0).getContent());
            assertEquals(TEST_COMMENT_LIST.get(1).getContent(), result.get(1).getContent());
        }

        @Test
        void 성공_테스트_유저가_낙찰자() {
            //given
            when(auctionService.findAuctionById(TEST_AUCTION1_ID))
                .thenReturn(TEST_AUCTION);
            when(commentRepository.findAllByAuctionId(TEST_AUCTION1_ID))
                .thenReturn(TEST_COMMENT_LIST);

            //when
            List<CommentReadResponseDto> result = commentService.getComments(TEST_AUCTION1_ID,
                TEST_BUYER_ID);

            //then
            assertEquals(TEST_COMMENT_LIST.size(), result.size());
            assertEquals(TEST_COMMENT_LIST.get(0).getContent(), result.get(0).getContent());
            assertEquals(TEST_COMMENT_LIST.get(1).getContent(), result.get(1).getContent());
        }

        @Test
        void 실패_테스트_유저가_판매자X_그리고_낙찰자X() {
            //given
            when(auctionService.findAuctionById(TEST_AUCTION1_ID))
                .thenReturn(TEST_AUCTION);

            //when, then
            assertThrows(
                InvalidAuthorityException.class,
                () -> commentService.getComments(
                    TEST_AUCTION1_ID,
                    TEST_STRANGER_ID
                ),
                "댓글 권한이 없습니다"
            );
        }

    }

}