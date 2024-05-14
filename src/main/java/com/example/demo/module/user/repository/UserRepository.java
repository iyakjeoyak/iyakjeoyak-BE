package com.example.demo.module.user.repository;

import com.example.demo.module.user.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByUsername(String username);

    @EntityGraph(attributePaths = {"image"})
    Optional<User> findByUsername(String username);

    Boolean existsByNickname(String nickname);

//    Optional<User> findBySocialTypeAndSocialId(SocialType socialType, String socialId);
}
