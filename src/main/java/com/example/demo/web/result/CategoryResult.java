package com.example.demo.web.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryResult {

    @Schema(description = "카테고리 PK")
    private Long id;

    @Schema(description = "카테고리 이름")
    private String name;
}
