package com.example.demo.module.common;

import com.example.demo.module.user.entity.User;
import com.example.demo.module.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserAuditorAware implements AuditorAware<User> {
    private final UserRepository userRepository;

    @Override
    public Optional<User> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null&&!authentication.getPrincipal().equals("anonymousUser")) {
                return userRepository.findById(Long.parseLong(authentication.getPrincipal().toString()));
        } else {
            return null;
        }
    }
}