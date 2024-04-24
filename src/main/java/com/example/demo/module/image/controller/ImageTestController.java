package com.example.demo.module.image.controller;

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
    public ResponseEntity<List<Image>> saveList(
            @RequestPart(name = "uploadImgs") List<MultipartFile> uploadImgs) throws Exception {
        return new ResponseEntity<>(imageService.saveImageList(uploadImgs), HttpStatus.OK);
    }

    @PostMapping("/test")
    public ResponseEntity<Image> save(
            @RequestPart(name = "uploadImg") MultipartFile uploadImg) throws Exception {
        return new ResponseEntity<>(imageService.saveImage(uploadImg), HttpStatus.OK);
    }

    @DeleteMapping("/test/{imgId}")
    public ResponseEntity<Long> delete(
            @PathVariable(name = "pullPath") String pullPath , @RequestParam("userId")Long userId) {
        return new ResponseEntity<>(imageService.deleteImage(userId, pullPath), HttpStatus.OK);
    }

}
