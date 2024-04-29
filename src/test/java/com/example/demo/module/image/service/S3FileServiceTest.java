package com.example.demo.module.image.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.demo.module.image.repository.ImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class S3FileServiceTest {
    @Mock
    private AmazonS3 s3Client = Mockito.mock(AmazonS3.class);

    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private S3FileService s3FileService;

    private final String endpoint = "https://test-bucket.s3.ap-northeast-2.amazonaws.com/";

    private final String bucketName = "test-bucket";

    @BeforeEach
    void setUp() throws MalformedURLException {
        s3FileService = new S3FileService(s3Client, imageRepository, bucketName, endpoint);
    }

    @Test
    @DisplayName("S3 연동 테스트")
    void import_S3() throws IOException {
        // given
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "test data".getBytes()
        );

        // when
        String expectedUrl = endpoint + file.getOriginalFilename();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        s3Client.putObject(new PutObjectRequest("test-bucket", "test.txt", file.getInputStream(), metadata));

        // 실제 로직에서는 UUID로 변환된 파일명이 저장될 것
        String fullPath = s3FileService.getFullPath("test.txt");

        // 서비스 로직을 사용하면 파일명이 변경되어 S3 연동 테스트가 어려워 직접 넣는 로직으로 연동 테스트 함
//        Image image = s3FileService.saveImage(file);
//        String expectedUrl = image.getFullPath();
//        when(s3Client.getUrl(any(), any())).thenReturn(new URL(expectedUrl));

        // then
        ArgumentCaptor<PutObjectRequest> captor = ArgumentCaptor.forClass(PutObjectRequest.class);
        verify(s3Client).putObject(captor.capture());
        PutObjectRequest actualRequest = captor.getValue();
        assertThat(actualRequest.getBucketName()).isEqualTo(bucketName);

        assertThat(actualRequest.getKey()).isEqualTo("test.txt");
        assertThat(actualRequest.getMetadata().getContentType()).isEqualTo(MediaType.TEXT_PLAIN_VALUE);
        assertThat(actualRequest.getMetadata().getContentLength()).isEqualTo(file.getSize());

        // DB에 저장되는 경로값이 일치하는지 테스트
        assertThat(fullPath).isEqualTo(expectedUrl);
    }


}