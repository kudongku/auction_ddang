package com.ip.ddangddangddang.domain.file.values;

import com.ip.ddangddangddang.domain.file.dto.request.FileCreateRequestDto;
import com.ip.ddangddangddang.domain.file.dto.response.FileCreateResponseDto;
import com.ip.ddangddangddang.domain.file.dto.response.FileReadResponseDto;
import com.ip.ddangddangddang.domain.file.entity.File;
import com.ip.ddangddangddang.domain.town.entity.Town;
import com.ip.ddangddangddang.domain.user.entity.User;
import com.ip.ddangddangddang.global.security.UserDetailsImpl;
import java.net.URL;
import java.util.ArrayList;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

public interface FileValues {

    // town
    String TOWN_NAME = "townName";
    Town TOWN = new Town(TOWN_NAME, new ArrayList<>());

    // user
    Long USER_ID = 0L;
    Long INVALID_USER_ID = 1L;
    String USER_EMAIL = "test@test.com";
    String USER_NICKNAME = "nickname";
    String USER_PASSWORD = "password";
    User USER = new User(USER_ID, USER_EMAIL, USER_NICKNAME, USER_PASSWORD, TOWN);
    UserDetailsImpl USER_DETAILS = new UserDetailsImpl(USER);

    // multipartFile
    String FILE_NAME = "auctionImage";
    String ORIGINAL_FILE_NAME = "file.png";
    byte[] TEST_DATA = FILE_NAME.getBytes();
    byte[] EMPTY_DATA = new byte[0];

    MockMultipartFile MOCK_MULTIPART_FILE = new MockMultipartFile(
        FILE_NAME,
        ORIGINAL_FILE_NAME,
        MediaType.IMAGE_PNG_VALUE,
        TEST_DATA
    );

    MockMultipartFile MOCK_MULTIPART_FILE_EMPTY = new MockMultipartFile(
        FILE_NAME,
        "",
        "",
        EMPTY_DATA
    );

    String KEY_NAME = "keyName";
    String FILE_PATH = "http://example.com/file-path";

    // file
    Long FILE_ID = 0L;
    String OBJECT_NAME = "requestDto";
    String PRESIGNED_URL = "http://example.com/signed-url";
    FileCreateRequestDto FILE_CREATE_REQUEST_DTO = new FileCreateRequestDto(OBJECT_NAME);
    FileCreateResponseDto FILE_CREATE_RESPONSE_DTO = new FileCreateResponseDto(FILE_ID);
    FileReadResponseDto FILE_READ_RESPONSE_DTO = new FileReadResponseDto(FILE_ID, PRESIGNED_URL);
    File FILE = new File(FILE_ID, OBJECT_NAME, KEY_NAME, FILE_PATH, USER);

    // s3
    String BUCKET = "S3Bucket";
}
