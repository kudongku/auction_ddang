package com.ip.ddangddangddang.domain.bid;

import com.ip.ddangddangddang.domain.auction.dto.request.AuctionRequestDto;
import com.ip.ddangddangddang.domain.auction.entity.Auction;
import com.ip.ddangddangddang.domain.auction.repository.AuctionRepository;
import com.ip.ddangddangddang.domain.bid.dto.request.BidRequestDto;
import com.ip.ddangddangddang.domain.bid.repository.BidRepository;
import com.ip.ddangddangddang.domain.bid.service.BidService;
import com.ip.ddangddangddang.domain.file.entity.File;
import com.ip.ddangddangddang.domain.file.repository.FileRepository;
import com.ip.ddangddangddang.domain.town.entity.Town;
import com.ip.ddangddangddang.domain.town.repository.TownRepository;
import com.ip.ddangddangddang.domain.user.entity.User;
import com.ip.ddangddangddang.domain.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

//@Rollback
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // 테스트 인스턴스의 생성 단위를 클래스로 변경합니다. 테그트 메소드가 필드를 공유함
@SpringBootTest
public class BidDistributedLockTest {

    @Autowired
    BidService bidService;

    @Autowired
    BidRepository bidRepository;

    @Autowired
    FileRepository fileRepository;

    @Autowired
    AuctionRepository auctionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TownRepository townRepository;

    Auction auction;
    User buyer;

    @BeforeEach
    void setUp() {
        Town town = townRepository.save(new Town("as232222351412", new ArrayList<>()));
        User user = userRepository.save(new User("as322212ew415212", "as24321we225212", "a5224s212", town));
        buyer = userRepository.save(new User("aws24445221122", "a4s5214w212225", "as456212", town));
        File file = fileRepository.save(new File("wa425s12263512", "wa1s224253612", "a546s2312", user));
        auction = auctionRepository.save(
            new Auction(new AuctionRequestDto("as52452261w312", "a42152s6w5132", town.getId()), user, file));
    }

    @Test
    void 입찰_분산락_적용_동시성100명_테스트() throws InterruptedException {
        int numberOfThreads = 20;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        for (int i=0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    // 분산락 적용 메서드 호출 (락의 key는 쿠폰의 name으로 설정)
                    bidService.createBid(auction.getId(), new BidRequestDto(3000L), buyer.getId());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
    }
}
