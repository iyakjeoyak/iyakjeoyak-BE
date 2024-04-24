package com.example.demo.module.image.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.demo.module.image.entity.Image;
import com.example.demo.module.image.repository.ImageRepository;
import com.example.demo.module.user.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@Primary
public class S3FileService implements ImageService {
    private final AmazonS3 s3;
    private final ImageRepository imageRepository;


    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.endPointUrl}")
    private String endPointUrl;


    public S3FileService(ImageRepository imageRepository,
                         //의존성 생성을 위한 값
                         @Value("${cloud.aws.credentials.access-key}")
                         String accessKey,
                         @Value("${cloud.aws.credentials.secret-key}")
                         String secretKey,
                         @Value("${cloud.aws.region.static}")
                         String region) {
        this.imageRepository = imageRepository;
        this.s3 = AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .build();
        ;
    }

    @Override
    public Image saveImage(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return null;
        }
        return imageRepository.save(saveFileAndGetEntity(file));
    }

    @Override
    public List<Image> saveImageList(List<MultipartFile> files) throws IOException {
        List<Image> ids = new ArrayList<>();
        if (!files.isEmpty()) {
            for (MultipartFile file : files) {
                Image image = saveFileAndGetEntity(file);
                if (image != null) {
                    ids.add(imageRepository.save(image));
                }
            }
        }
        return ids;
    }

    @Override
    public Long deleteImage(Long userId, String filePath) {
        Image image = imageRepository.findByFullPath(filePath).orElseThrow(() -> new NoSuchElementException("이미지 경로가 잘못되었습니다."));
        User createdBy = image.getCreatedBy();
        if (createdBy == null || !createdBy.getUserId().equals(userId)) {
            throw new IllegalArgumentException("이미지를 저장한 사용자가 아닙니다.");
        }
        imageRepository.deleteById(image.getId());
        return image.getId();
    }

    private Image saveFileAndGetEntity(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String storeFileName = createFileName(originalFilename);
        String fullPath = getFullPath(storeFileName);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        s3.putObject(bucket, storeFileName, file.getInputStream(), metadata);

        return Image.builder().originName(originalFilename).storeName(storeFileName).fullPath(fullPath).build();
    }

    public String getFullPath(String storeName) {
        return endPointUrl + storeName;
    }


    private String createFileName(String originalFilename) {
        String uu = UUID.randomUUID().toString();
        String extracted = extract(originalFilename);

        return uu + "." + extracted;
    }

    private String extract(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
