package com.example.demo.service;

import com.example.demo.web.result.BookmarkResult;
import com.example.demo.web.result.PageResult;
import org.springframework.data.domain.PageRequest;

public interface BookmarkService {
    PageResult<BookmarkResult> findAll(Long userId, PageRequest pageRequest);

    BookmarkResult findOneByUser(Long bookmarkId, Long userId);

    Long save(Long medicineId, Long userId);

    Long delete(Long medicineId, Long userId);
}
