package com.ip.ddangddangddang.domain.file.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.ip.ddangddangddang.domain.file.dto.response.FileCreateResponseDto;
import com.ip.ddangddangddang.domain.file.dto.response.FileReadResponseDto;
import com.ip.ddangddangddang.domain.file.entity.File;
import com.ip.ddangddangddang.domain.file.repository.FileRepository;
import com.ip.ddangddangddang.domain.file.values.FileValues;
import com.ip.ddangddangddang.domain.user.service.UserService;
import com.ip.ddangddangddang.global.exception.custom.EmptyImageException;
import com.ip.ddangddangddang.global.s3.FileUploadService;
import java.io.IOException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FileServiceTest implements FileValues {

    @InjectMocks
    private FileService fileService;

    @Mock
    private FileRepository fileRepository;

    @Mock
    private UserService userService;

    @Mock
    FileUploadService fileUploadService;

    @Nested
    @DisplayName("옥션 생성 테스크")
    public class testUpload {

        @Test
        @DisplayName("upload 성공시")
        public void testUpload_success() throws IOException {
            given(userService.getUserByIdOrElseThrow(USER_ID))
                .willReturn(USER);
            given(fileUploadService.upload(any(), anyString()))
                .willReturn(FILE_PATH);
            given(fileRepository.save(any(File.class)))
                .willReturn(FILE);

            FileCreateResponseDto responseDto = fileService.upload(MOCK_MULTIPART_FILE, OBJECT_NAME,
                USER_ID);

            assertNotNull(responseDto);
            assert responseDto.getFileId().equals(FILE_ID);
        }

        @Test
        @DisplayName("upload시 empty fileImage로 실패시")
        public void testUpload_EmptyImageException() {
            assertThrows(
                EmptyImageException.class,
                () -> fileService.upload(MOCK_MULTIPART_FILE_EMPTY, OBJECT_NAME, USER_ID)
            );
        }

        @Test
        @DisplayName("upload시 empty fileImage로 실패시")
        public void testUpload_IllegalArgumentException() throws IOException {
            given(userService.getUserByIdOrElseThrow(USER_ID))
                .willReturn(USER);
            given(fileUploadService.upload(any(), anyString()))
                .willThrow(IOException.class);

            assertThrows(
                IllegalArgumentException.class,
                () -> fileService.upload(MOCK_MULTIPART_FILE, OBJECT_NAME, USER_ID)
            );
        }
    }

    @Test
    @DisplayName("delete 테스트")
    void testDelete() {
        fileService.delete(FILE);
    }

    @Nested
    @DisplayName("미리 서명 URL")
    public class testGetPresignedURL {

        @Test
        @DisplayName("미리 서명 URL 성공")
        public void testGetPresignedURL_success() {
            given(fileRepository.findById(FILE_ID))
                .willReturn(java.util.Optional.of(FILE));
            given(fileUploadService.getPresignedURL(KEY_NAME))
                .willReturn(FILE_PATH);

            // Act
            FileReadResponseDto response = fileService.getPresignedURL(FILE_ID, USER_ID);

            // Assert
            assertNotNull(response);
            assertEquals(FILE_ID, response.getFileId());
            assertEquals(FILE_PATH, response.getPreSignedUrl());
        }

        @Test
        @DisplayName("작성자가 아닐 경우")
        public void testGetPresignedURL_fail() {
            given(fileRepository.findById(FILE_ID))
                .willReturn(java.util.Optional.of(FILE));

            assertThrows(
                IllegalArgumentException.class,
                () -> fileService.getPresignedURL(FILE_ID, INVALID_USER_ID)
            );
        }

    }

    @Nested
    @DisplayName("getFileOrElseThrow")
    public class testGetFileOrElseThrow {
        @Test
        @DisplayName("성공")
        public void testGetFileOrElseThrow_success() {
            given(fileRepository.findById(FILE_ID))
                .willReturn(null);

            assertThrows(
                NullPointerException.class,
                () -> fileService.findFileOrElseThrow(FILE_ID)
            );
        }

        @Test
        @DisplayName("실패")
        public void testGetFileOrElseThrow_NullPointerException() {
            given(fileRepository.findById(FILE_ID))
                .willReturn(Optional.empty());

            assertThrows(
                NullPointerException.class,
                () -> fileService.findFileOrElseThrow(FILE_ID)
            );
        }
    }

    @Test
    @DisplayName("getFile")
    public void testGetFile() {
        given(fileRepository.findById(FILE_ID))
            .willReturn(java.util.Optional.of(FILE));

        Optional<File> optionalFile = fileService.getFileById(FILE_ID);

        assert optionalFile.isPresent();
    }

}
