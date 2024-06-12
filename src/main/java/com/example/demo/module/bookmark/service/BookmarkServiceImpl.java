package com.example.demo.module.bookmark.service;

import com.example.demo.global.exception.CustomException;
import com.example.demo.module.bookmark.entity.Bookmark;
import com.example.demo.module.bookmark.repository.BookmarkRepository;
import com.example.demo.module.medicine.entity.Medicine;
import com.example.demo.module.medicine.repository.MedicineRepository;
import com.example.demo.module.user.entity.User;
import com.example.demo.module.user.repository.UserRepository;
import com.example.demo.module.bookmark.dto.result.BookmarkResult;
import com.example.demo.module.common.result.PageResult;
import com.example.demo.util.mapper.MedicineMapper;
import io.micrometer.core.annotation.Counted;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static com.example.demo.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookmarkServiceImpl implements BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final MedicineRepository medicineRepository;
    private final UserRepository userRepository;
    private final MedicineMapper medicineMapper;

    @Override
    public PageResult<BookmarkResult> findAll(Long userId, PageRequest pageRequest) {
        Page<BookmarkResult> bookmarkResults = bookmarkRepository.findAllByUserUserId(userId, pageRequest).map(bookmark -> bookmark.toDto(medicineMapper));
        return new PageResult<>(bookmarkResults);
    }

    @Override
    public BookmarkResult findOneByUser(Long bookmarkId, Long userId) {
        return bookmarkRepository.findByIdAndUserUserId(bookmarkId, userId)
                .orElseThrow(() -> new CustomException(BOOKMARK_NOT_FOUND)).toDto(medicineMapper);
    }

    @Counted("my.bookmark")
    @Override
    @Transactional
    public Long save(Long medicineId, Long userId) {
        if(!isChecked(medicineId, userId)) {
            return bookmarkRepository.save(Bookmark
                    .builder()
                    .medicine(medicineRepository.findById(medicineId).orElseThrow(() -> new CustomException(MEDICINE_NOT_FOUND)))
                    .user(userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND)))
                    .build()).getId();
        }
        throw new CustomException(BOOKMARK_EXIST);
    }

    @Override
    @Transactional
    public Long delete(Long medicineId, Long userId) {
        if(!medicineRepository.existsById(medicineId)){
            throw new CustomException(MEDICINE_NOT_FOUND);
        }
        if(!userRepository.existsById(userId)){
            throw new CustomException(USER_NOT_FOUND);
        }
        BookmarkResult bookmarkResult = bookmarkRepository.findByMedicineIdAndUserUserId(medicineId, userId)
                .orElseThrow(() -> new CustomException(ACCESS_BLOCKED)).toDto(medicineMapper);
        bookmarkRepository.deleteById(bookmarkResult.getId());
        return bookmarkResult.getId();
    }

    @Override
    public Boolean isChecked(Long medicineId, Long userId) {
        return bookmarkRepository.existsByMedicineIdAndUserUserId(medicineId, userId);
    }

    @Counted("my.bookmark")
    @Override
    @Transactional
    public Boolean click(Long medicineId, Long userId) {
        Medicine medicine = medicineRepository.findById(medicineId).orElseThrow(() -> new CustomException(MEDICINE_NOT_FOUND));
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        if(isChecked(medicineId, userId)){
            BookmarkResult bookmarkResult = bookmarkRepository.findByMedicineIdAndUserUserId(medicineId, userId)
                    .orElseThrow(() -> new CustomException(ACCESS_BLOCKED)).toDto(medicineMapper);
            bookmarkRepository.deleteById(bookmarkResult.getId());
            return false;
        }
        bookmarkRepository.save(Bookmark
                .builder()
                .medicine(medicine)
                .user(user)
                .build());
        return true;
    }
}
