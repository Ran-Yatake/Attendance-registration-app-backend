package com.example.memo.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "user_profile")
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private String userId;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "prefecture")
    private String prefecture;

    @Column(name = "city")
    private String city;

    @Column(name = "address_line")
    private String addressLine;

    @Column(name = "building")
    private String building;

    @Column(name = "department")
    private String department;

    @Column(name = "position")
    private String position;

    @Column(name = "employee_number")
    private String employeeNumber;

    @Column(name = "hire_date")
    private String hireDate;

    @Column(name = "birth_date")
    private String birthDate;

    @Column(name = "emergency_contact_name")
    private String emergencyContactName;

    @Column(name = "emergency_contact_phone")
    private String emergencyContactPhone;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
