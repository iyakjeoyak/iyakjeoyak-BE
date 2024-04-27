package com.example.demo.module.image.service;

import com.example.demo.module.image.entity.Image;
import com.example.demo.module.image.repository.ImageRepository;
import com.example.demo.module.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;
    private final String filePath;


    @Override
    public Image saveImage(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return null;
        }
        return imageRepository.save(saveFileAndGetEntity(file));
    }

    @Override
    @Transactional
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
    public Long deleteImage(Long userId, Long imageId) {
        Image image = imageRepository.findById(imageId).orElseThrow(() -> new NoSuchElementException("이미지 경로가 잘못되었습니다."));
        User createdBy = image.getCreatedBy();
        if (createdBy == null || !createdBy.getUserId().equals(userId)) {
            throw new IllegalArgumentException("이미지를 저장한 사용자가 아닙니다.");
        }
        imageRepository.deleteById(image.getId());
        return image.getId();
    }


    //이하 공용 메서드

    private Image saveFileAndGetEntity(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String storeFileName = createFileName(originalFilename);
        String fullPath = getFullPath(storeFileName);

        file.transferTo(new File(getFullPath(storeFileName)));

        return Image.builder().originName(originalFilename).storeName(storeFileName).fullPath(fullPath).build();
    }


    public String getFullPath(String storeName) {
        return filePath + storeName;
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
