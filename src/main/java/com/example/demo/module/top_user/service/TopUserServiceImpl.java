package com.example.demo.module.top_user.service;

import com.example.demo.module.top_user.entity.TopUser;
import com.example.demo.module.top_user.repository.TopUserRepository;
import com.example.demo.module.user.dto.result.UserResult;
import com.example.demo.module.user.dto.result.UserSimpleResult;
import com.example.demo.module.user.entity.User;
import com.example.demo.util.mapper.UserMapper;
import com.example.demo.util.mapper.UserSimpleResultMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TopUserServiceImpl implements TopUserService{
    private final TopUserRepository topUserRepository;
//    private final UserMapper userMapper;
    private final UserSimpleResultMapper userSimpleResultMapper;

    @Override
    public List<UserSimpleResult> getTopUsers() {
        LocalDate localDate = LocalDate.now();
        int year = localDate.getYear();
        int week = localDate.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());

        List<UserSimpleResult> users = new ArrayList<>();
        for (TopUser topUser : topUserRepository.findByYearAndWeekOrderByRanking(year, week)) {
            User user = topUser.getUser();
//            users.add(userMapper.toDto(user));
            users.add(userSimpleResultMapper.toDto(user));
        }
        return users;
    }
}
