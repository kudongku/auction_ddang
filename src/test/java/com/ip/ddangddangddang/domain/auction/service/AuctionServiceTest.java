package com.ip.ddangddangddang.domain.auction.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.ip.ddangddangddang.domain.auction.dto.request.AuctionRequestDto;
import com.ip.ddangddangddang.domain.auction.dto.response.AuctionListResponseDto;
import com.ip.ddangddangddang.domain.auction.dto.response.AuctionResponseDto;
import com.ip.ddangddangddang.domain.auction.dto.response.AuctionUpdateResponseDto;
import com.ip.ddangddangddang.domain.auction.entity.Auction;
import com.ip.ddangddangddang.domain.auction.entity.StatusEnum;
import com.ip.ddangddangddang.domain.auction.repository.AuctionRepository;
import com.ip.ddangddangddang.domain.auction.values.AuctionServiceTestValues;
import com.ip.ddangddangddang.domain.file.service.FileService;
import com.ip.ddangddangddang.domain.town.service.TownService;
import com.ip.ddangddangddang.domain.user.service.UserService;
import com.ip.ddangddangddang.global.exception.custom.AuctionNotFoundException;
import com.ip.ddangddangddang.global.exception.custom.FileNotFoundException;
import com.ip.ddangddangddang.global.exception.custom.UserHasNotAuthorityToAuctionException;
import com.ip.ddangddangddang.global.exception.custom.UserHasNotAuthorityToFileException;
import com.ip.ddangddangddang.global.exception.custom.UserNotFoundException;
import com.ip.ddangddangddang.global.redis.CacheService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuctionServiceTest implements AuctionServiceTestValues {

    @InjectMocks
    private AuctionService auctionService;

    @Mock
    private AuctionRepository auctionRepository;

    @Mock
    private UserService userService;

    @Mock
    private FileService fileService;

    @Mock
    private TownService townService;

    @Mock
    private CacheService cacheService;

    @Nested
    @DisplayName("옥션 생성 테스크")
    public class AuctionCreateTest {

        @Test
        void 옥션_생성_성공_테스트() {
            //given
            AuctionRequestDto auctionRequestDto = TEST_AUCTION_REQUEST_DTO1;
            given(userService.getUserById(anyLong())).willReturn(Optional.ofNullable(TEST_USER1));
            given(fileService.getFileById(anyLong())).willReturn(Optional.ofNullable(TEST_FILE1));
            given(auctionRepository.save(any(Auction.class))).willReturn(TEST_AUCTION1);
            //when
            auctionService.createAuction(auctionRequestDto, TEST_USER1_ID);
            //then
            then(cacheService).should(times(1))
                .setAuctionExpiredKey(anyLong());
        }

        @Test
        void 옥션_생성_실패_테스트_파일에_대한_권한_없음() {
            //given
            AuctionRequestDto auctionRequestDto = TEST_AUCTION_REQUEST_DTO1;
            given(userService.getUserById(anyLong())).willReturn(Optional.ofNullable(TEST_USER1));
            given(fileService.getFileById(anyLong())).willReturn(Optional.ofNullable(TEST_FILE2));
            //when, then
            assertThrows(UserHasNotAuthorityToFileException.class,
                () -> auctionService.createAuction(auctionRequestDto, TEST_USER1_ID));

        }

        //todo
        @Test
        void 옥션_생성_실패_테스트_유저가_존재하지_않음() {
            //given
            AuctionRequestDto auctionRequestDto = TEST_AUCTION_REQUEST_DTO1;
            given(userService.getUserById(anyLong())).willReturn(Optional.empty());
            //when, then
            assertThrows(UserNotFoundException.class,
                () -> auctionService.createAuction(auctionRequestDto, TEST_USER1_ID));
        }

        //todo
        @Test
        void 옥션_생성_실패_테스트_이미지가_없음() {
            //given
            AuctionRequestDto auctionRequestDto = TEST_AUCTION_REQUEST_DTO1;
            given(userService.getUserById(anyLong())).willReturn(Optional.ofNullable(TEST_USER1));
            given(fileService.getFileById(anyLong())).willReturn(Optional.empty());
            //when, then
            assertThrows(FileNotFoundException.class,
                () -> auctionService.createAuction(auctionRequestDto, TEST_USER1_ID));
        }
    }

    @Nested
    @DisplayName("옥션 삭제 테스트")
    public class AuctionDeleteTest {

        @Test
        void 옥션_삭제_성공_테스트() {
            //given
            given(auctionRepository.findById(anyLong())).willReturn(
                Optional.ofNullable(TEST_AUCTION1)); // findById 반환타입 optional이라 이렇게 써야 함
            //when
            auctionService.deleteAuction(TEST_TOWN1_AUCTION1_ID, TEST_USER1_ID);
            //then
            then(auctionRepository).should(times(1)).delete(any(Auction.class));
        }

        @Test
        void 옥션_삭제_실패_테스트_작성자_아님() {
            //given
            given(auctionRepository.findById(anyLong())).willReturn(
                Optional.ofNullable(TEST_AUCTION1));
            //when, then
            assertThrows(UserHasNotAuthorityToAuctionException.class,
                () -> auctionService.deleteAuction(TEST_TOWN1_AUCTION1_ID, TEST_USER2_ID));
        }
    }

//    @Nested
//    @DisplayName("옥션 상태 hold로 변경")
//    public class AuctionStatusHoldTest {
//
//        @Test
//        void 옥션_HOLD로_상태_변경_성공_테스트() {
//            //given
//            given(auctionRepository.findById())
//            //when
//            //then
//        }
//    }

    @Nested
    @DisplayName("옥션 상태 completed로 변경 테스트")
    public class AuctionStatusCompleteTest {

        @Test
        void 옥션_상태_COMPLETED로_변경_성공_테스트() {
            //given
            given(auctionRepository.findById(anyLong())).willReturn(
                Optional.ofNullable(TEST_AUCTION1));
            //when
            AuctionUpdateResponseDto auctionUpdateResponseDto = auctionService.updateStatusToComplete(
                TEST_TOWN1_AUCTION1_ID, TEST_USER1_ID);
            //then
            assertEquals(StatusEnum.COMPLETED, auctionUpdateResponseDto.getStatusEnum());
        }

        @Test
        void 옥션_상태_COMPLETED로_변경_실패_테스트_옥션_없음() {
            //given
            given(auctionRepository.findById(anyLong())).willReturn(Optional.empty());
            //when, then
            assertThrows(AuctionNotFoundException.class,
                () -> auctionService.updateStatusToComplete(TEST_TOWN1_AUCTION1_ID, TEST_USER2_ID));

        }

        @Test
        void 옥션_상태_COMPLETED로_변경_실패_테스트_사용자가_불일치() {
            //given
            given(auctionRepository.findById(anyLong())).willReturn(
                Optional.ofNullable(TEST_AUCTION1));
            //when, then
            assertThrows(UserHasNotAuthorityToAuctionException.class,
                () -> auctionService.updateStatusToComplete(TEST_TOWN1_AUCTION1_ID, TEST_USER2_ID));

        }

    }

    @Nested
    @DisplayName("옥션의 입찰가 업데이트 테스트")
    public class AuctionUpdateBidTest {

        @Test
        void 옥션_입찰가_업데이트_성공_테스트() {
            //given
            given(auctionRepository.findById(anyLong())).willReturn(
                Optional.ofNullable(TEST_AUCTION1));
            //when
            AuctionUpdateResponseDto auctionUpdateResponseDto = auctionService.updateBid(
                TEST_TOWN1_AUCTION1_ID, TEST_AUCTION_UPDATE_PRICE, TEST_ANOTHER_USER1_ID);
            //then
            assertEquals(TEST_AUCTION_UPDATE_PRICE, auctionUpdateResponseDto.getPrice());
            assertEquals(TEST_ANOTHER_USER1_ID, auctionUpdateResponseDto.getBuyerId());
        }

        @Test
        void 옥션_입찰가_업데이트_실패_테스트_게시글이_존재_X() {
            //given
            given(auctionRepository.findById(anyLong())).willReturn(
                Optional.empty());
            //when, then
            assertThrows(AuctionNotFoundException.class,
                () -> auctionService.updateBid(TEST_TOWN1_AUCTION1_ID, TEST_AUCTION_UPDATE_PRICE,
                    TEST_ANOTHER_USER1_ID));
        }
    }

    @Nested
    @DisplayName("옥션 전체 조회 테스트")
    public class AuctionsGetAllTest {

        @Test
        void 옥션_전체_조회_성공_테스트() {
            //given
            given(userService.getUserById(anyLong())).willReturn(Optional.ofNullable(TEST_USER1));
            given(auctionRepository.findAllByFilters(any(), any(), any())).willReturn(
                List.of(TEST_AUCTION1, TEST_AUCTION2));
            //when
            List<AuctionListResponseDto> auctionListResponseDtoList = auctionService.getAuctions(
                TEST_USER1_ID, StatusEnum.ON_SALE, null); // assertTrue
            //then
            assertEquals(StatusEnum.ON_SALE, auctionListResponseDtoList.get(0).getStatus());
            assertEquals(StatusEnum.ON_SALE, auctionListResponseDtoList.get(1).getStatus());

        }

        //todo
        @Test
        void 옥션_전체_조회_실패_테스트_유저가_없음() {
            //given
            given(userService.getUserById(anyLong())).willReturn(Optional.empty());
            //when,then
            assertThrows(UserNotFoundException.class,
                () -> auctionService.getAuctions(TEST_USER1_ID, StatusEnum.ON_SALE, null));
        }

    }

    @Nested
    @DisplayName("옥션 상세 조회 테스트")
    public class AuctionGetTest {

        @Test
        void 옥션_상세_조회_성공_테스트() {
            //given
            given(auctionRepository.findById(anyLong())).willReturn(
                Optional.ofNullable(TEST_AUCTION1));
            given(townService.findNameByIdOrElseThrow(anyLong())).willReturn(TEST_TOWN1_NAME);
            given(userService.getUserByIdOrElseThrow(any())).willReturn(TEST_BUYER_USER1);
            //when
            AuctionResponseDto auctionResponseDto = auctionService.getAuction(
                TEST_TOWN1_AUCTION1_ID);
            //then
            assertEquals(TEST_BUYER_USER_NICKNAME, auctionResponseDto.getBuyerNickname());

        }

        @Test
        void 옥션_상세_조회_실패_테스트_게시글_존재X() {
            //given
            given(auctionRepository.findById(anyLong())).willReturn(
                Optional.empty());
            //when, then
            assertThrows(AuctionNotFoundException.class,
                () -> auctionService.getAuction(TEST_TOWN1_AUCTION1_ID));
        }
    }

    @Nested
    @DisplayName("자신이 올린 옥션 리스트")
    public class AuctionsGetByMe {

        @Test
        void 자신이_올린_옥션_전체_조회_성공_테스트() {
            //given
            given(auctionRepository.findAuctionsByUserId(anyLong(), any())).willReturn(
                TEST_SLICE_AUCTION_WRITTEN_BY_ME);
            //when
            List<AuctionListResponseDto> auctionListResponseDtoList = auctionService.getMyAuctions(
                TEST_USER1_ID, TEST_PAGEABLE).stream().toList();
            //then
            assertEquals(TEST_USER_NICKNAME, auctionListResponseDtoList.get(0).getNickname());
        }

    }

    @Nested
    @DisplayName("자신이 입찰한 게시글 리스트")
    public class AuctionsBoughtByMe {

        @Test
        void 자신이_입찰한_옥션_전체_조회_성공_테스트() {
            //given
            given(auctionRepository.findBidsByBuyerId(anyLong(), any())).willReturn(
                TEST_SLICE_AUCTION_BID_BY_ME);
            //when
            List<AuctionListResponseDto> auctionListResponseDtoList = auctionService.getMyBids(
                TEST_USER1_ID, TEST_PAGEABLE).stream().toList();
            //then
            assertEquals(TEST_USER_NICKNAME, auctionListResponseDtoList.get(0).getNickname());
            assertEquals(StatusEnum.HOLD, auctionListResponseDtoList.get(0).getStatus());
        }
    }

    @Nested
    @DisplayName("아이디로 옥션 조회 테스트")
    public class AuctionFindTest {

        @Test
        void 아이디로_옥션_조회_성공_테스트() {
            //given
            given(auctionRepository.findById(anyLong())).willReturn(
                Optional.ofNullable(TEST_AUCTION1));
            //when
            Auction auction = auctionService.getAuctionById(TEST_TOWN1_AUCTION1_ID).orElse(null);
            //then
            assert TEST_AUCTION1 != null;
            assert auction != null;
            assertEquals(TEST_AUCTION1.getId(), auction.getId());
        }
    }
}