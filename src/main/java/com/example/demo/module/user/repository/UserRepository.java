package com.example.demo.module.user.repository;

import com.example.demo.module.user.entity.SocialType;
import com.example.demo.module.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByUsername(String username);

    User findByUsername(String username);

    Boolean existsByNickname(String nickname);

    User findBySocialTypeAndSocialId(SocialType socialType, String socialId);
}
