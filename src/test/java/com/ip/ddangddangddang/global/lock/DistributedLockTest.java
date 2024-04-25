//package com.ip.ddangddangddang.global.lock;
//
//import static jodd.util.ThreadUtil.sleep;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.ip.ddangddangddang.domain.auction.entity.Auction;
//import com.ip.ddangddangddang.domain.auction.entity.StatusEnum;
//import com.ip.ddangddangddang.domain.auction.repository.AuctionRepository;
//import com.ip.ddangddangddang.domain.bid.dto.request.BidRequestDto;
//import com.ip.ddangddangddang.domain.bid.service.BidService;
//import com.ip.ddangddangddang.domain.bid.values.BidTestValues;
//import com.ip.ddangddangddang.domain.file.entity.File;
//import com.ip.ddangddangddang.domain.file.repository.FileRepository;
//import com.ip.ddangddangddang.global.aop.RedisLockAspect;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Import;
//
//
//@Disabled
//@Import(RedisLockAspect.class)
//@DisplayName("분산락 테스트")
//@SpringBootTest
//class DistributedLockTest implements BidTestValues {
//
//    @Autowired
//    private BidService bidService;
//    @Autowired
//    private AuctionRepository auctionRepository;
//    @Autowired
//    private FileRepository fileRepository;
//    private Long lockAuctionId;
//    private Long nonLockAuctionId;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        File TEST_FILE1 = File.builder()
//            .objectName(TEST_OBJECT_NAME)
//            .keyName(TEST_KEY_NAME)
//            .filePath(TEST_FILE_PATH)
//            .user(TEST_SELLER_USER)
//            .build();
//        fileRepository.save(TEST_FILE1);
//
//        File TEST_FILE2 = File.builder()
//            .objectName(TEST_OBJECT_NAME)
//            .keyName(TEST_KEY_NAME)
//            .filePath(TEST_FILE_PATH)
//            .user(TEST_SELLER_USER)
//            .build();
//        fileRepository.save(TEST_FILE2);
//
//        Auction TEST_LOCK_AUCTION = Auction.builder()
//            .townId(TEST_TOWN1_ID)
//            .title(TEST_AUCTION_TITLE)
//            .content(TEST_AUCTION_CONTENT)
//            .price(TEST_AUCTION_PRICE)
//            .statusEnum(StatusEnum.ON_SALE)
//            .finishedAt(TEST_LOCAL_DATETIME_TOMORROW)
//            .user(TEST_SELLER_USER)
//            .file(TEST_FILE1)
//            .build();
//        auctionRepository.save(TEST_LOCK_AUCTION);
//        lockAuctionId = TEST_LOCK_AUCTION.getId();
//
//        Auction TEST_NONLOCK_AUCTION = Auction.builder()
//            .townId(TEST_TOWN2_ID)
//            .title(TEST_AUCTION_TITLE)
//            .content(TEST_AUCTION_CONTENT)
//            .price(TEST_AUCTION_PRICE)
//            .statusEnum(StatusEnum.ON_SALE)
//            .finishedAt(TEST_LOCAL_DATETIME_TOMORROW)
//            .user(TEST_SELLER_USER)
//            .file(TEST_FILE2)
//            .build();
//        auctionRepository.save(TEST_NONLOCK_AUCTION);
//        nonLockAuctionId = TEST_NONLOCK_AUCTION.getId();
//    }
//
//    @Test
//    void 성공_테스트() throws InterruptedException {
//        //given
//        int numberOfThreads = 100;
//        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
//        CountDownLatch countDownLatch = new CountDownLatch(numberOfThreads);
//
//        //when
//        for (int i = 0; i < numberOfThreads; i++) {
//            BidRequestDto requestDto = new BidRequestDto(101L + i);
//
//            executorService.submit(() -> {
//                try {
//                    bidService.createBid(lockAuctionId, requestDto, TEST_BUYER_ID);
//                } catch (JsonProcessingException e) {
//                    throw new RuntimeException(e);
//                } finally {
//                    countDownLatch.countDown();
//                }
//            });
//        }
//
//        //then
//        countDownLatch.await();
//        sleep(5);
//        Long price = auctionRepository.findById(lockAuctionId).orElseThrow().getPrice();
//        System.out.println("high price: " + price);
//        assertEquals(200L, price);
//
//    }
//
//    @Test
//    void 실패_테스트_분산락_적용X() throws InterruptedException {
//        //given
//        int numberOfThreads = 100;
//        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
//        CountDownLatch countDownLatch = new CountDownLatch(numberOfThreads);
//
//        //when
//        for (int i = 0; i < numberOfThreads; i++) {
//            BidRequestDto requestDto = new BidRequestDto(101L + i);
//
//            executorService.submit(() -> {
//                try {
//                    bidService.createBidNonLock(nonLockAuctionId, requestDto, TEST_BUYER_ID);
//                } catch (JsonProcessingException e) {
//                    throw new RuntimeException(e);
//                } finally {
//                    countDownLatch.countDown();
//                }
//            });
//        }
//
//        //when
//        countDownLatch.await();
//        Long price = auctionRepository.findById(nonLockAuctionId).orElseThrow().getPrice();
//        System.out.println("high price: " + price);
//        assert (price <= 200L);
//    }
//
//}
