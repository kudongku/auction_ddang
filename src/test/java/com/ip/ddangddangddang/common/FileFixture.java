package com.ip.ddangddangddang.common;

import com.ip.ddangddangddang.domain.file.entity.File;

public interface FileFixture extends UserFixture {

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
        .user(TEST_USER1)
        .build();

    File TEST_FILE2 = File.builder()
        .id(TEST_FILE2_ID)
        .objectName(TEST_OBJECT_NAME)
        .keyName(TEST_KEY_NAME)
        .filePath(TEST_FILE_PATH)
        .user(TEST_USER3)
        .build();

}
