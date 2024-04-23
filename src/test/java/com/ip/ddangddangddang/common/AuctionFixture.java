package com.ip.ddangddangddang.common;


import com.ip.ddangddangddang.domain.auction.entity.Auction;
import com.ip.ddangddangddang.domain.auction.entity.StatusEnum;
import java.time.LocalDateTime;

public interface AuctionFixture extends UserFixture, TownFixture, FileFixture {

    //auction
    Long TEST_TOWN1_AUCTION1_ID = 1L;
    Long TEST_TOWN2_AUCTION2_ID = 2L;
    Long TEST_TOWN3_AUCTION1_ID = 3L;
    Long TEST_TOWN4_AUCTION2_ID = 4L;
    String TEST_AUCTION_TITLE = "testTitle";
    String TEST_AUCTION_CONTENT = "testContent";
    Long TEST_AUCTION_PRICE = 100L;
    Long TEST_AUCTION_UPDATE_PRICE = 200L;
    Long TEST_AUCTION_BUYER_ID = 2L;

    LocalDateTime TEST_LOCAL_DATETIME_TODAY = LocalDateTime.parse("2024-03-01T00:00:00");
    LocalDateTime TEST_LOCAL_DATETIME_TOMORROW = LocalDateTime.parse("2024-03-02T00:00:00");

    Auction TEST_AUCTION1 = Auction.builder()
        .id(TEST_TOWN1_AUCTION1_ID)
        .townId(TEST_TOWN1_ID)
        .title(TEST_AUCTION_TITLE)
        .content(TEST_AUCTION_CONTENT)
        .price(TEST_AUCTION_PRICE)
        .buyerId(TEST_ANOTHER_USER1_ID)
        .statusEnum(StatusEnum.ON_SALE)
        .finishedAt(TEST_LOCAL_DATETIME_TOMORROW)
        .user(TEST_USER1)
        .file(TEST_FILE1)
        .build();

    Auction TEST_AUCTION2 = Auction.builder()
        .id(TEST_TOWN1_AUCTION1_ID)
        .townId(TEST_TOWN1_ID)
        .title(TEST_AUCTION_TITLE)
        .content(TEST_AUCTION_CONTENT)
        .price(TEST_AUCTION_PRICE)
        .buyerId(TEST_ANOTHER_USER1_ID)
        .statusEnum(StatusEnum.ON_SALE)
        .finishedAt(TEST_LOCAL_DATETIME_TOMORROW)
        .user(TEST_USER1)
        .file(TEST_FILE1)
        .build();


    Auction TEST_ANOTHER_TOWN_AUCTION1 = Auction.builder()
        .id(TEST_TOWN3_AUCTION1_ID)
        .townId(TEST_ANOTHER_TOWN1_ID)
        .title(TEST_AUCTION_TITLE)
        .content(TEST_AUCTION_CONTENT)
        .price(TEST_AUCTION_PRICE)
        .buyerId(TEST_USER4_ID)
        .statusEnum(StatusEnum.ON_SALE)
        .finishedAt(TEST_LOCAL_DATETIME_TOMORROW)
        .user(TEST_USER3)
        .file(TEST_FILE2)
        .build();

    Auction TEST_ANOTHER_TOWN_AUCTION2 = Auction.builder()
        .id(TEST_TOWN3_AUCTION1_ID)
        .townId(TEST_ANOTHER_TOWN1_ID)
        .title(TEST_AUCTION_TITLE)
        .content(TEST_AUCTION_CONTENT)
        .price(TEST_AUCTION_PRICE)
        .buyerId(TEST_USER4_ID)
        .statusEnum(StatusEnum.ON_SALE)
        .finishedAt(TEST_LOCAL_DATETIME_TOMORROW)
        .user(TEST_USER3)
        .file(TEST_FILE2)
        .build();

    Auction TEST_BID_AUCTION1 = Auction.builder()
        .id(TEST_TOWN1_AUCTION1_ID)
        .townId(TEST_TOWN1_ID)
        .title(TEST_AUCTION_TITLE)
        .content(TEST_AUCTION_CONTENT)
        .price(TEST_AUCTION_PRICE)
        .buyerId(TEST_USER1_ID)
        .statusEnum(StatusEnum.HOLD)
        .finishedAt(TEST_LOCAL_DATETIME_TOMORROW)
        .user(TEST_USER2)
        .file(TEST_FILE1)
        .build();

    Auction TEST_BID_AUCTION2 = Auction.builder()
        .id(TEST_TOWN2_AUCTION2_ID)
        .townId(TEST_TOWN1_ID)
        .title(TEST_AUCTION_TITLE)
        .content(TEST_AUCTION_CONTENT)
        .price(TEST_AUCTION_PRICE)
        .buyerId(TEST_USER1_ID)
        .statusEnum(StatusEnum.HOLD)
        .finishedAt(TEST_LOCAL_DATETIME_TOMORROW)
        .user(TEST_USER2)
        .file(TEST_FILE1)
        .build();

}
