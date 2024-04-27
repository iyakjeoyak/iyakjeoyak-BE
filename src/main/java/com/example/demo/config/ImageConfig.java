package com.example.demo.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.example.demo.module.image.repository.ImageRepository;
import com.example.demo.module.image.service.ImageServiceImpl;
import com.example.demo.module.image.service.S3FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@Slf4j
public class ImageConfig {
    // S3 설정
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${cloud.aws.s3.endPointUrl}")
    private String endPointUrl;
    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;
    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;
    @Value("${cloud.aws.region.static}")
    private String region;

    @Bean
    @Primary
    public S3FileService s3FileService(ImageRepository imageRepository) {
        AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .build();
        return new S3FileService(amazonS3, imageRepository, bucket, endPointUrl);
    }

    // local 설정
    @Value("${file.path}")
    private String filePath;
    @Bean
    public ImageServiceImpl imageServiceImpl(ImageRepository imageRepository) {
        return new ImageServiceImpl(imageRepository, filePath);
    }

}
