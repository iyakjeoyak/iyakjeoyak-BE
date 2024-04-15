package com.example.demo.web.payload;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NonNull;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
public class ReviewPayload {

    @NotEmpty(message = "제목을 입력해주세요")
    private String title;

    @NonNull
    private Long medicineId;

    private List<Integer> tagList = new ArrayList<>();
    private List<MultipartFile> imgList = new ArrayList<>();
    @NotEmpty(message = "내용을 입력해주세요")
    private String content;
    @Range(min = 0, max = 5)
    private Integer star;
}
