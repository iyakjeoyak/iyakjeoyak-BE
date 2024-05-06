package com.example.demo.module.image.controller;

import com.example.demo.module.image.dto.result.ImageResult;
import com.example.demo.module.image.entity.Image;
import com.example.demo.module.image.service.ImageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "ImageService 테스트 용")
@RequestMapping("/image")
public class ImageTestController {
    private final ImageService imageService;

    @PostMapping("/test/list")
    public ResponseEntity<List<ImageResult>> saveList(
            @RequestPart(name = "uploadImgs") List<MultipartFile> uploadImgs) throws Exception {
        List<ImageResult> list = imageService.saveImageList(uploadImgs).stream().map(ImageTestController::toImageResult).toList();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/test")
    public ResponseEntity<ImageResult> save(
            @RequestPart(name = "uploadImg") MultipartFile uploadImg) throws Exception {
        ImageResult imageResult = toImageResult(imageService.saveImage(uploadImg));
        return new ResponseEntity<>(imageResult, HttpStatus.OK);
    }


    @DeleteMapping("/test/{imageId}")
    public ResponseEntity<Long> delete(@PathVariable(name = "imageId") Long imageId , @AuthenticationPrincipal Long userId) {
        return new ResponseEntity<>(imageService.deleteImage(userId, imageId), HttpStatus.OK);
    }

    private static ImageResult toImageResult(Image image) {
        ImageResult imageResult = new ImageResult();
        imageResult.setId(image.getId());
        imageResult.setFullPath(image.getFullPath());
        return imageResult;
    }
}
