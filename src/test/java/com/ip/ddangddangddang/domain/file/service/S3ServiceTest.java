package com.ip.ddangddangddang.domain.file.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.ip.ddangddangddang.domain.file.values.FileValues;
import com.ip.ddangddangddang.global.exception.custom.NotValidBucketException;
import com.ip.ddangddangddang.global.s3.S3Service;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;

@ExtendWith(MockitoExtension.class)
class S3ServiceTest implements FileValues {

    @InjectMocks
    private S3Service s3Service;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Mock
    private AmazonS3 amazonS3;

    @Nested
    @DisplayName("업로드 테스트")
    public class testUpload {

        @Test
        @DisplayName("업로드 성공")
        void upload() throws IOException {
            // Act
            String url = s3Service.upload(MOCK_MULTIPART_FILE, KEY_NAME);

            // Assert
            assertNotNull(url);
            assertEquals(CDN_PATH, url);
        }

        @Test
        @DisplayName("업로드 실패")
        void upload_NotValidBucketException()  {
            given(
                amazonS3.putObject(any(), any(), any(InputStream.class), any(ObjectMetadata.class)))
                .willThrow(AmazonS3Exception.class);

            // Act & Assert
            assertThrows(
                NotValidBucketException.class,
                () -> s3Service.upload(MOCK_MULTIPART_FILE, KEY_NAME)
            );
        }
    }

    @Nested
    @DisplayName("삭제 테스트")
    public class testDelete {

        @Test
        @DisplayName("삭제 성공")
        void delete() {
            s3Service.delete(KEY_NAME);
        }

        @Test
        @DisplayName("삭제 실패")
        void delete_NotValidBucketException() {
            doThrow(AmazonS3Exception.class).when(amazonS3).deleteObject(bucket, KEY_NAME);
            assertThrows(
                NotValidBucketException.class,
                () -> s3Service.delete(KEY_NAME)
            );
        }
    }

    @Test
    @DisplayName("미리 서명된 url 성공")
    void getPresignedURL() throws MalformedURLException {
        given(amazonS3.generatePresignedUrl(any()))
            .willReturn(new URL(PRESIGNED_URL));
        String presignedUrl = s3Service.getPresignedURL(KEY_NAME);
        assertEquals(PRESIGNED_URL, presignedUrl);
    }
}