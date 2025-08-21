package com.example.memo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.memo.entity.UserProfile;
import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    
    Optional<UserProfile> findByUserId(String userId);
    
    boolean existsByUserId(String userId);
    
    void deleteByUserId(String userId);
}
