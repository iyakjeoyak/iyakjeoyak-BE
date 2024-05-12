package com.example.demo.module.bookmark.service;

import com.example.demo.module.bookmark.dto.result.BookmarkResult;
import com.example.demo.module.common.result.PageResult;
import org.springframework.data.domain.PageRequest;

public interface BookmarkService {
    PageResult<BookmarkResult> findAll(Long userId, PageRequest pageRequest);

    BookmarkResult findOneByUser(Long bookmarkId, Long userId);

    Long save(Long medicineId, Long userId);

    Long delete(Long medicineId, Long userId);

    Boolean isChecked(Long medicineId, Long userId);

    Boolean click(Long medicineId, Long userId);
}
