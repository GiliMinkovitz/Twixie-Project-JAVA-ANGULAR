package com.example.twixie.model;

import jakarta.persistence.*;

@Entity
public class UserVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String verificationCode;

    public UserVerification() {
    }

    public UserVerification(Long id, String email, String verificationCode) {
        this.id = id;
        this.email = email;
        this.verificationCode = verificationCode;
    }

    public UserVerification(String email, String verificationCode) {
        this.email = email;
        this.verificationCode = verificationCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }
}
