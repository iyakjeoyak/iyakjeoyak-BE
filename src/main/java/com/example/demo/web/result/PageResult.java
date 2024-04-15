package com.example.demo.web.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PageResult<T> {
    private List<T> content;
    private int number;
    private int size;
    private int totalPages;
    private Long totalElement;
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
