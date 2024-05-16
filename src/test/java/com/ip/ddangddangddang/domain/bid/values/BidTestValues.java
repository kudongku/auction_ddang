package com.ip.ddangddangddang.domain.bid.values;

import com.ip.ddangddangddang.domain.auction.entity.Auction;
import com.ip.ddangddangddang.domain.auction.entity.StatusEnum;
import com.ip.ddangddangddang.domain.bid.dto.request.BidRequestDto;
import com.ip.ddangddangddang.domain.bid.entity.Bid;
import com.ip.ddangddangddang.domain.file.entity.File;
import com.ip.ddangddangddang.domain.town.entity.Town;
import com.ip.ddangddangddang.domain.user.entity.User;
import java.time.LocalDateTime;
import java.util.List;

public interface BidTestValues {

    //town
    Long TEST_TOWN1_ID = 1L;
    Long TEST_TOWN2_ID = 2L;
    String TEST_TOWN1_NAME = "testTownName1";
    String TEST_TOWN2_NAME = "testTownName2";

    Town TEST_TOWN1 = new Town(TEST_TOWN1_ID, TEST_TOWN1_NAME,
        List.of(TEST_TOWN1_ID, TEST_TOWN2_ID));
    Town TEST_TOWN2 = new Town(TEST_TOWN2_ID, TEST_TOWN2_NAME,
        List.of(TEST_TOWN1_ID, TEST_TOWN2_ID));

    //user
    Long TEST_SELLER_ID = 1L;
    Long TEST_BUYER_ID = 2L;

    String TEST_SELLER_USER_EMAIL = "testUserEmail";
    String TEST_BUYER_USER_EMAIL = "testUserEmail";
    String TEST_SELLER_USER_NICKNAME = "testUserNickname";
    String TEST_BUYER_USER_NICKNAME = "testBuyerNickname";
    String TEST_USER_ENCRYPTED_PASSWORD = "testEncryptedPassword";

    User TEST_SELLER_USER = User.builder()
        .id(TEST_SELLER_ID)
        .email(TEST_SELLER_USER_EMAIL)
        .nickname(TEST_SELLER_USER_NICKNAME)
        .password(TEST_USER_ENCRYPTED_PASSWORD)
        .town(TEST_TOWN1)
        .build();

    User TEST_BUYER_USER = User.builder()
        .id(TEST_BUYER_ID)
        .email(TEST_BUYER_USER_EMAIL)
        .nickname(TEST_BUYER_USER_NICKNAME)
        .password(TEST_USER_ENCRYPTED_PASSWORD)
        .town(TEST_TOWN2)
        .build();

    //file
    Long TEST_FILE1_ID = 1L;
    Long TEST_FILE2_ID = 2L;
    String TEST_OBJECT_NAME = "testObjectName";
    String TEST_KEY_NAME = "testKeyName";
    String TEST_FILE_PATH = "testFilePath";

    File TEST_FILE1 = File.builder()
        .id(TEST_FILE1_ID)
        .objectName(TEST_OBJECT_NAME)
        .keyName(TEST_KEY_NAME)
        .filePath(TEST_FILE_PATH)
        .user(TEST_SELLER_USER)
        .build();

    File TEST_FILE2 = File.builder()
        .id(TEST_FILE2_ID)
        .objectName(TEST_OBJECT_NAME)
        .keyName(TEST_KEY_NAME)
        .filePath(TEST_FILE_PATH)
        .user(TEST_BUYER_USER)
        .build();

    //auction
    Long TEST_AUCTION1_ID = 1L;
    Long TEST_AUCTION2_ID = 2L;
    String TEST_AUCTION_TITLE = "testTitle";
    String TEST_AUCTION_CONTENT = "testContent";
    Long TEST_AUCTION_PRICE = 100L;
    LocalDateTime TEST_LOCAL_DATETIME_TOMORROW = LocalDateTime.parse("2024-03-02T00:00:00");

    Auction TEST_ON_SALE_AUCTION = Auction.builder()
        .id(TEST_AUCTION1_ID)
        .townId(TEST_TOWN1_ID)
        .title(TEST_AUCTION_TITLE)
        .content(TEST_AUCTION_CONTENT)
        .price(TEST_AUCTION_PRICE)
        .buyerId(TEST_BUYER_ID)
        .status(StatusEnum.ON_SALE)
        .finishedAt(TEST_LOCAL_DATETIME_TOMORROW)
        .user(TEST_SELLER_USER)
        .file(TEST_FILE1)
        .build();

    Auction TEST_HOLD_AUCTION = Auction.builder()
        .id(TEST_AUCTION2_ID)
        .townId(TEST_TOWN1_ID)
        .title(TEST_AUCTION_TITLE)
        .content(TEST_AUCTION_CONTENT)
        .price(TEST_AUCTION_PRICE)
        .buyerId(TEST_BUYER_ID)
        .status(StatusEnum.HOLD)
        .finishedAt(TEST_LOCAL_DATETIME_TOMORROW)
        .user(TEST_SELLER_USER)
        .file(TEST_FILE2)
        .build();


    //bid
    Long TEST_ID1 = 1L;
    Long TEST_PRICE1 = 200L;
    Long TEST_PRICE2 = 100L;

    Bid TEST_BID = Bid.builder()
        .id(TEST_ID1)
        .auctionId(TEST_AUCTION1_ID)
        .userId(TEST_BUYER_ID)
        .price(TEST_PRICE1)
        .build();

    //dto
    BidRequestDto TEST_PROPER_REQUEST_DTO = new BidRequestDto(TEST_PRICE1);
    BidRequestDto TEST_SAME_PRICE_REQUEST_DTO = new BidRequestDto(TEST_PRICE2);

    //eventPublisher
    String TEST_TOPIC = "auction-price:" + TEST_AUCTION1_ID;
    String TEST_EVENT = "{\"id\":1,\"auctionId\":1,\"userId\":1,\"price\":100}";

}