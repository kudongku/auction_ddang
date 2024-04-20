package com.ip.ddangddangddang.domain.auction.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.ip.ddangddangddang.domain.auction.dto.request.AuctionRequestDto;
import com.ip.ddangddangddang.domain.auction.entity.Auction;
import com.ip.ddangddangddang.domain.auction.repository.AuctionRepository;
import com.ip.ddangddangddang.domain.auction.values.AuctionServiceTestValues;
import com.ip.ddangddangddang.domain.file.service.FileService;
import com.ip.ddangddangddang.domain.town.service.TownService;
import com.ip.ddangddangddang.domain.user.service.UserService;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

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
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    //given
    //when
    //then
    @Nested
    @DisplayName("옥션 생성 테스크")
    public class AuctionCreateTest {

        @Test
        void 옥션_생성_성공_테스트() {
            //given
            AuctionRequestDto auctionRequestDto = TEST_AUCTION_REQUEST_DTO1;
            given(userService.findUserOrElseThrow(anyLong())).willReturn(TEST_USER1);
            given(fileService.findFileOrElseThrow(anyLong())).willReturn(TEST_FILE1);
            given(auctionRepository.save(any(Auction.class))).willReturn(TEST_AUCTION1);
            given(redisTemplate.opsForValue()).willReturn(valueOperations);
            //when
            auctionService.createAuction(auctionRequestDto, TEST_USER1_ID);
            //then
            then(valueOperations).should(times(1)).set(any(String.class), any(String.class));
            then(redisTemplate).should(times(1))
                .expire(anyString(), anyLong(), any(TimeUnit.class));
        }

        // 유저 없음과 파일 없음은 userService에서 테스트해야한다.
        @Test
        void 옥션_생성_실패_테스트_파일_없음() {
            //given
            AuctionRequestDto auctionRequestDto = TEST_AUCTION_REQUEST_DTO1;
            given(userService.findUserOrElseThrow(anyLong())).willReturn(TEST_USER1);
            given(fileService.findFileOrElseThrow(anyLong())).willReturn(TEST_FILE1);
            //when, then
            Assertions.assertThrows(IllegalArgumentException.class,
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
            auctionService.deleteAuction(TEST_AUCTION1_ID, TEST_USER1_ID);
            //then
            then(auctionRepository).should(times(1)).delete(any(Auction.class));
        }

        @Test
        void 옥션_삭제_실패_테스트_작성자_아님() {
            //given
            given(auctionRepository.findById(anyLong())).willReturn(
                Optional.ofNullable(TEST_AUCTION1));
            //when, then
            Assertions.assertThrows(IllegalArgumentException.class,
                () -> auctionService.deleteAuction(TEST_AUCTION1_ID, TEST_USER2_ID));
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

}