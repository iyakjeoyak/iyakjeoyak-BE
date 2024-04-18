package com.example.demo.module.image.service;

import com.example.demo.module.image.entity.Image;
import com.example.demo.module.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;

    @Value("${file.path}")
    private String filePath;

    @Override
    public Long saveImage(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return null;
        }
        return imageRepository.save(saveFileAndGetEntity(file)).getId();
    }

    @Override
    public List<Long> saveImageList(List<MultipartFile> files) throws IOException {
        List<Long> ids = new ArrayList<>();
        if (!files.isEmpty()) {
            for (MultipartFile file : files) {
                Image image = saveFileAndGetEntity(file);
                if (image != null) {
                    ids.add(imageRepository.save(image).getId());
                }
            }
        }
        return ids;
    }


    //이하 공용 메서드

    private Image saveFileAndGetEntity(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String storeFileName = createFileName(originalFilename);

        file.transferTo(new File(getFullPath(storeFileName)));

        return Image.builder().originName(originalFilename).storeName(storeFileName).build();
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
