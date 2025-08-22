package com.example.memo.controller;

import com.example.memo.entity.UserProfile;
import com.example.memo.repository.UserProfileRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.Optional;

@RestController
@RequestMapping("/api/user-profile")
@CrossOrigin(origins = "*")
public class UserProfileController {
    // 自分のプロフィール取得（簡易版）
    @GetMapping("/me")
    public ResponseEntity<UserProfile> getMyProfile(@RequestParam String userId) {
        Optional<UserProfile> profile = userProfileRepository.findByUserId(userId);
        return profile.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    private final UserProfileRepository userProfileRepository;

    public UserProfileController(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    // ユーザープロフィール取得
    @GetMapping
    public ResponseEntity<UserProfile> getUserProfile(@RequestParam String userId) {
        Optional<UserProfile> profile = userProfileRepository.findByUserId(userId);
        return profile.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // ユーザープロフィール作成・更新
    @PostMapping
    public ResponseEntity<UserProfile> saveUserProfile(@RequestBody UserProfile userProfile) {
        Optional<UserProfile> existingProfile = userProfileRepository.findByUserId(userProfile.getUserId());
        
        if (existingProfile.isPresent()) {
            // 既存のプロフィールを更新
            UserProfile profile = existingProfile.get();
            updateProfileFields(profile, userProfile);
            return ResponseEntity.ok(userProfileRepository.save(profile));
        } else {
            // 新規プロフィール作成
            return ResponseEntity.ok(userProfileRepository.save(userProfile));
        }
    }

    // ユーザープロフィール更新
    @PutMapping
    public ResponseEntity<UserProfile> updateUserProfile(@RequestBody UserProfile userProfile) {
        Optional<UserProfile> existingProfile = userProfileRepository.findByUserId(userProfile.getUserId());
        
        if (existingProfile.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        UserProfile profile = existingProfile.get();
        updateProfileFields(profile, userProfile);
        
        return ResponseEntity.ok(userProfileRepository.save(profile));
    }

    // ユーザープロフィール削除
    @DeleteMapping
    public ResponseEntity<Void> deleteUserProfile(@RequestParam String userId) {
        if (!userProfileRepository.existsByUserId(userId)) {
            return ResponseEntity.notFound().build();
        }
        
        userProfileRepository.deleteByUserId(userId);
        return ResponseEntity.ok().build();
    }

    // プロフィールフィールドを更新するヘルパーメソッド
    private void updateProfileFields(UserProfile existing, UserProfile updated) {
        if (updated.getDisplayName() != null) {
            existing.setDisplayName(updated.getDisplayName());
        }
        if (updated.getEmail() != null) {
            existing.setEmail(updated.getEmail());
        }
        if (updated.getPhoneNumber() != null) {
            existing.setPhoneNumber(updated.getPhoneNumber());
        }
        if (updated.getPostalCode() != null) {
            existing.setPostalCode(updated.getPostalCode());
        }
        if (updated.getPrefecture() != null) {
            existing.setPrefecture(updated.getPrefecture());
        }
        if (updated.getCity() != null) {
            existing.setCity(updated.getCity());
        }
        if (updated.getAddressLine() != null) {
            existing.setAddressLine(updated.getAddressLine());
        }
        if (updated.getBuilding() != null) {
            existing.setBuilding(updated.getBuilding());
        }
        if (updated.getDepartment() != null) {
            existing.setDepartment(updated.getDepartment());
        }
        if (updated.getPosition() != null) {
            existing.setPosition(updated.getPosition());
        }
        if (updated.getEmployeeNumber() != null) {
            existing.setEmployeeNumber(updated.getEmployeeNumber());
        }
        if (updated.getHireDate() != null) {
            existing.setHireDate(updated.getHireDate());
        }
        if (updated.getBirthDate() != null) {
            existing.setBirthDate(updated.getBirthDate());
        }
        if (updated.getEmergencyContactName() != null) {
            existing.setEmergencyContactName(updated.getEmergencyContactName());
        }
        if (updated.getEmergencyContactPhone() != null) {
            existing.setEmergencyContactPhone(updated.getEmergencyContactPhone());
        }
    }
}
