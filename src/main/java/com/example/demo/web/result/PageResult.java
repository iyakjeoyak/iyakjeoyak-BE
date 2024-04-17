package com.example.demo.web.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PageResult<T> {

    @Schema(description = "데이터 리스트")
    private List<T> content;

    @Schema(description = "현재 페이지")
    private int number;

    @Schema(description = "페이지 사이즈")
    private int size;

    @Schema(description = "전체 페이지 수")
    private int totalPages;

    @Schema(description = "전체 데이터 수")
    private Long totalElement;

    @Schema(description = "데이터 넘버")
    private int numberOfElement;

    public PageResult(Page<T> page) {
        this.content = page.getContent();
        this.number = page.getNumber();
        this.size = page.getSize();
        this.totalPages = page.getTotalPages();
        this.totalElement = page.getTotalElements();
        this.numberOfElement = page.getNumberOfElements();
    }
}
