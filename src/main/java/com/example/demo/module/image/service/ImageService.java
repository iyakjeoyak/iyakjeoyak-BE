package com.example.demo.module.image.service;

import com.example.demo.module.image.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageService {
    Long saveImage(MultipartFile file) throws IOException;

    List<Long> saveImageList(List<MultipartFile> files) throws IOException;
}
