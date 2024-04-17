package com.example.demo.service.impl;

import com.example.demo.domain.entity.User;
import com.example.demo.domain.repository.UserRepository;
import com.example.demo.service.CustomUserDetailsService;
import com.example.demo.util.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements UserDetailsService, CustomUserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
       User user = userRepository.findById(Long.parseLong(userId)).orElseThrow(() -> new UsernameNotFoundException("해당하는 유저가 없습니다."));

       return new CustomUserDetails(user);
    }


}
