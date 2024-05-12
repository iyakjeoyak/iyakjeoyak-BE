package com.example.demo.module.bookmark.service;

import com.example.demo.global.exception.CustomException;
import com.example.demo.global.exception.ErrorCode;
import com.example.demo.module.bookmark.dto.result.BookmarkResult;
import com.example.demo.module.bookmark.entity.Bookmark;
import com.example.demo.module.bookmark.repository.BookmarkRepository;
import com.example.demo.module.common.result.PageResult;
import com.example.demo.module.medicine.entity.Medicine;
import com.example.demo.module.medicine.repository.MedicineRepository;
import com.example.demo.module.user.entity.User;
import com.example.demo.module.user.repository.UserRepository;
import com.example.demo.util.mapper.MedicineMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.example.demo.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookmarkServiceImplTest {
    @Mock
    private BookmarkRepository bookmarkRepository;
    @Mock
    private MedicineRepository medicineRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private MedicineMapper medicineMapper;
    @InjectMocks
    private BookmarkServiceImpl bookmarkService;
    User user;
    Medicine medicine;
    Bookmark bookmark;
    @BeforeEach
    void setup(){
        user = User.builder().userId(1L).build();
        medicine = Medicine.builder().id(2L).build();
        bookmark = Bookmark.builder().id(1L).user(user).medicine(medicine).build();
    }
    @Test
    void findAll() {
        Medicine medicine1 = Medicine.builder().id(5L).build();
        Medicine medicine2 = Medicine.builder().id(4L).build();
        Medicine medicine3 = Medicine.builder().id(3L).build();
        Bookmark bookmark1 = Bookmark.builder().id(2L).medicine(medicine1).user(user).build();
        Bookmark bookmark2 = Bookmark.builder().id(3L).medicine(medicine2).user(user).build();
        Bookmark bookmark3 = Bookmark.builder().id(4L).medicine(medicine3).user(user).build();

        PageRequest pageRequest = PageRequest.of(0, 3);
        List<Bookmark> bookmarks = Arrays.asList(bookmark1, bookmark2, bookmark3);
        List<BookmarkResult> bookmarkResults = bookmarks.stream().map(bm -> bm.toDto(medicineMapper)).toList();
        Page<Bookmark> bookmarkPage = new PageImpl<>(bookmarks);
        Page<BookmarkResult> bookmarkResultPage = new PageImpl<>(bookmarkResults);
        PageResult<BookmarkResult> expected = new PageResult<>(bookmarkResultPage);

        when(bookmarkRepository.findAllByUserUserId(user.getUserId(), pageRequest)).thenReturn(bookmarkPage);
        PageResult<BookmarkResult> result = bookmarkService.findAll(user.getUserId(), pageRequest);

        assertEquals(expected, result);
    }

    @Test
    void findOneByUser() {
        BookmarkResult bookmarkResult = bookmark.toDto(medicineMapper);
        when(bookmarkRepository.findByIdAndUserUserId(bookmark.getId(), user.getUserId())).thenReturn(Optional.ofNullable(bookmark));

        BookmarkResult result = bookmarkService.findOneByUser(bookmark.getId(), user.getUserId());

        assertEquals(bookmarkResult, result);
    }
    @Test
    void findOneByUser_fail_notFoundBookmark(){
        when(bookmarkRepository.findByIdAndUserUserId(bookmark.getId(), user.getUserId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookmarkService.findOneByUser(bookmark.getId(), user.getUserId())).isInstanceOf(CustomException.class).hasMessage(BOOKMARK_NOT_FOUND.getMessage());
    }

    @Test
    void save() {
        when(bookmarkRepository.existsByMedicineIdAndUserUserId(medicine.getId(), user.getUserId())).thenReturn(false);
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(medicineRepository.findById(anyLong())).thenReturn(Optional.ofNullable(medicine));
        when(bookmarkRepository.save(any(Bookmark.class))).thenReturn(bookmark);

        Long id = bookmarkService.save(medicine.getId(), user.getUserId());

        assertEquals(bookmark.getId(), id);
    }
    @Test
    void save_fail_userNotFound() {
        when(bookmarkRepository.existsByMedicineIdAndUserUserId(medicine.getId(), user.getUserId())).thenReturn(false);
        when(medicineRepository.findById(medicine.getId())).thenReturn(Optional.ofNullable(medicine));
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookmarkService.save(medicine.getId(), user.getUserId()))
                .isInstanceOf(CustomException.class).hasMessage(USER_NOT_FOUND.getMessage());
    }
    @Test
    void save_fail_medicineNotFound() {
        when(bookmarkRepository.existsByMedicineIdAndUserUserId(medicine.getId(), user.getUserId())).thenReturn(false);
        when(medicineRepository.findById(medicine.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookmarkService.save(medicine.getId(), user.getUserId())).isInstanceOf(CustomException.class).hasMessage(MEDICINE_NOT_FOUND.getMessage());
    }
    @Test
    void save_fail_notExist() {
        when(bookmarkRepository.existsByMedicineIdAndUserUserId(medicine.getId(), user.getUserId())).thenReturn(true);

        assertThatThrownBy(() -> bookmarkService.save(medicine.getId(), user.getUserId())).isInstanceOf(IllegalArgumentException.class).hasMessage("해당유저는 이미 북마크 등록을 했습니다.");
    }

    @Test
    void delete() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(medicineRepository.existsById(anyLong())).thenReturn(true);
        when(bookmarkRepository.findByMedicineIdAndUserUserId(medicine.getId(), user.getUserId())).thenReturn(Optional.ofNullable(bookmark));

        Long id = bookmarkService.delete(medicine.getId(), user.getUserId());

        assertEquals(bookmark.getId(), id);
    }
    @Test
    void delete_fail_notFoundMedicine(){
        when(medicineRepository.existsById(medicine.getId())).thenReturn(false);

        assertThatThrownBy(() -> bookmarkService.delete(medicine.getId(), user.getUserId())).isInstanceOf(CustomException.class).hasMessage(MEDICINE_NOT_FOUND.getMessage());
    }
    @Test
    void delete_fail_notFoundUser(){
        when(medicineRepository.existsById(medicine.getId())).thenReturn(true);
        when(userRepository.existsById(user.getUserId())).thenReturn(false);

        assertThatThrownBy(() -> bookmarkService.delete(medicine.getId(), user.getUserId())).isInstanceOf(CustomException.class).hasMessage(USER_NOT_FOUND.getMessage());
    }
    @Test
    void delete_fail_notExistMedicineAndUser(){
        when(medicineRepository.existsById(medicine.getId())).thenReturn(true);
        when(userRepository.existsById(user.getUserId())).thenReturn(true);
        when(bookmarkRepository.findByMedicineIdAndUserUserId(medicine.getId(), user.getUserId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookmarkService.delete(medicine.getId(), user.getUserId())).isInstanceOf(CustomException.class).hasMessage(ErrorCode.ACCESS_BLOCKED.getMessage());
    }
    @Test
    void isChecked() {
        when(bookmarkRepository.existsByMedicineIdAndUserUserId(medicine.getId(), user.getUserId())).thenReturn(true);
        when(bookmarkRepository.existsByMedicineIdAndUserUserId(2L, 2L)).thenReturn(false);

        Boolean checked1 = bookmarkService.isChecked(medicine.getId(), user.getUserId());
        Boolean checked2 = bookmarkService.isChecked(2L, 2L);

        assertEquals(true, checked1);
        assertEquals(false, checked2);
    }
}