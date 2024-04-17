package com.example.demo.web.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
public class HashtagResult {

    @Schema(description = "해쉬태그 PK")
    private Long id;

    @Schema(description = "해쉬태그 이름")
    private String name;
}
