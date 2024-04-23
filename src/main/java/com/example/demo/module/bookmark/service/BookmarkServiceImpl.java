package com.example.demo.module.bookmark.service;

import com.example.demo.module.bookmark.entity.Bookmark;
import com.example.demo.module.bookmark.repository.BookmarkRepository;
import com.example.demo.module.medicine.repository.MedicineRepository;
import com.example.demo.module.user.repository.UserRepository;
import com.example.demo.module.bookmark.dto.result.BookmarkResult;
import com.example.demo.module.common.result.PageResult;
import com.example.demo.util.mapper.MedicineMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
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
        if(bookmarkRepository.existsByIdAndUserUserId(bookmarkId, userId)) {
            return bookmarkRepository.findById(bookmarkId)
                    .orElseThrow(() -> new NoSuchElementException("해당 북마크는 없습니다.")).toDto(medicineMapper);
        }
        throw new IllegalArgumentException("해당 유저는 북마크 등록을 하지 않았습니다.");
    }

    @Override
    public Long save(Long medicineId, Long userId) {
        if(!isChecked(medicineId, userId)) {
            return bookmarkRepository.save(Bookmark
                    .builder()
                    .medicine(medicineRepository.findById(medicineId).orElseThrow(() -> new NoSuchElementException("해당하는 영양제가 없습니다.")))
                    .user(userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("해당하는 유저가 없습니다.")))
                    .build()).getId();
        }
        throw new IllegalArgumentException("해당유저는 이미 북마크 등록을 했습니다.");
    }

    @Override
    public Long delete(Long medicineId, Long userId) {
        if(!medicineRepository.existsById(medicineId)){
            throw new IllegalArgumentException("해당 영양제는 없습니다.");
        }
        if(!userRepository.existsById(userId)){
            throw new IllegalArgumentException("해당 유저는 없습니다.");
        }
        BookmarkResult bookmarkResult = bookmarkRepository.findByMedicineIdAndUserUserId(medicineId, userId)
                .orElseThrow(() -> new NoSuchElementException("해당 유저는 영양제를 북마크 하지 않았습니다.")).toDto(medicineMapper);
        bookmarkRepository.deleteById(bookmarkResult.getId());
        return bookmarkResult.getId();
    }

    @Override
    public Boolean isChecked(Long medicineId, Long userId) {
        return bookmarkRepository.existsByMedicineIdAndUserUserId(medicineId, userId);
    }
}
