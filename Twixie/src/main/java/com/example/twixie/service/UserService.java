package com.example.twixie.service;

import com.example.twixie.model.Users;
import com.example.twixie.service.repository.UsersRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UsersRepository usersRepository;
    private EmailService emailService;

    public UserService(UsersRepository usersRepository, EmailService emailService) {
        this.usersRepository = usersRepository;
        this.emailService = emailService;
    }

    public int updateRate(Long userId, UpdateRateAction action) {
        return usersRepository.incrementUsersRate(userId, action.getRateChange());
    }
}
