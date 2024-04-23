package com.ip.ddangddangddang.domain.comment.values;

import com.ip.ddangddangddang.domain.auction.entity.Auction;
import com.ip.ddangddangddang.domain.auction.entity.StatusEnum;
import com.ip.ddangddangddang.domain.comment.dto.request.CommentCreateRequestDto;
import com.ip.ddangddangddang.domain.comment.dto.response.CommentReadResponseDto;
import com.ip.ddangddangddang.domain.comment.entity.Comment;
import com.ip.ddangddangddang.domain.file.entity.File;
import com.ip.ddangddangddang.domain.town.entity.Town;
import com.ip.ddangddangddang.domain.user.entity.User;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public interface CommentTestValues {

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
    Long TEST_STRANGER_ID = 3L;

    String TEST_SELLER_USER_EMAIL = "testUserEmail";
    String TEST_BUYER_USER_EMAIL = "testUserEmail2";
    String TEST_STRANGER_USER_EMAIL = "testUserEmail3";

    String TEST_SELLER_USER_NICKNAME = "testUserNickname";
    String TEST_BUYER_USER_NICKNAME = "testBuyerNickname2";
    String TEST_STRANGER_USER_NICKNAME = "testBuyerNickname3";

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

    User TEST_STRANGER_USER = User.builder()
        .id(TEST_STRANGER_ID)
        .email(TEST_STRANGER_USER_EMAIL)
        .nickname(TEST_STRANGER_USER_NICKNAME)
        .password(TEST_USER_ENCRYPTED_PASSWORD)
        .town(TEST_TOWN2)
        .build();

    //file
    Long TEST_FILE1_ID = 1L;
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

    //auction
    Long TEST_AUCTION1_ID = 1L;
    String TEST_AUCTION_TITLE = "testTitle";
    String TEST_AUCTION_CONTENT = "testContent";
    Long TEST_AUCTION_PRICE = 100L;
    LocalDateTime TEST_LOCAL_DATETIME_TOMORROW = LocalDateTime.parse("2024-03-02T00:00:00");

    Auction TEST_AUCTION = Auction.builder()
        .id(TEST_AUCTION1_ID)
        .townId(TEST_TOWN1_ID)
        .title(TEST_AUCTION_TITLE)
        .content(TEST_AUCTION_CONTENT)
        .price(TEST_AUCTION_PRICE)
        .buyerId(TEST_BUYER_ID)
        .statusEnum(StatusEnum.ON_SALE)
        .finishedAt(TEST_LOCAL_DATETIME_TOMORROW)
        .user(TEST_SELLER_USER)
        .file(TEST_FILE1)
        .build();

    //comment
    Long TEST_COMMENT1_ID = 1L;
    Long TEST_COMMENT2_ID = 2L;
    String TEST_COMMENT1_TEXT = "testCommentText";
    String TEST_COMMENT2_TEXT = "testComment2Text";

    Comment TEST_COMMENT = Comment.builder()
        .id(TEST_COMMENT1_ID)
        .content(TEST_COMMENT1_TEXT)
        .user(TEST_SELLER_USER)
        .auction(TEST_AUCTION)
        .build();

    Comment TEST_COMMENT2 = Comment.builder()
        .id(TEST_COMMENT2_ID)
        .content(TEST_COMMENT2_TEXT)
        .user(TEST_BUYER_USER)
        .auction(TEST_AUCTION)
        .build();


    //dto
    CommentCreateRequestDto TEST_REQUEST_DTO = new CommentCreateRequestDto(TEST_COMMENT1_TEXT);
    CommentReadResponseDto TEST_RESPONSE_DTO = new CommentReadResponseDto(TEST_COMMENT);
    CommentReadResponseDto TEST_RESPONSE2_DTO = new CommentReadResponseDto(TEST_COMMENT2);

    List<CommentReadResponseDto> TEST_COMMENT_RESPONSE_DTO_LIST = Arrays.asList(TEST_RESPONSE_DTO,
        TEST_RESPONSE2_DTO);
    List<Comment> TEST_COMMENT_LIST = Arrays.asList(TEST_COMMENT, TEST_COMMENT2);


}
