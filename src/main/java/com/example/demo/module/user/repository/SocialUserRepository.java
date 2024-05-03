package com.example.demo.module.user.repository;

import com.example.demo.module.user.entity.SocialUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SocialUserRepository extends JpaRepository<SocialUser, Long> {

    Optional<SocialUser> findSocialUserBySocialEmail(String socialEmail);
}
