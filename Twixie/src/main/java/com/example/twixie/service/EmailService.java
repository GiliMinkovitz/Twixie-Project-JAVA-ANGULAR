package com.example.twixie.service;

import com.example.twixie.model.UserVerification;
import com.example.twixie.model.Users;
import com.example.twixie.service.repository.UserSettingsRepository;
import com.example.twixie.service.repository.UserVerificationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final UserVerificationRepository userVerificationRepository;

    public EmailService(JavaMailSender mailSender, UserVerificationRepository userVerificationRepository) {
        this.mailSender = mailSender;
        this.userVerificationRepository = userVerificationRepository;
    }

    @Async
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage massage = new SimpleMailMessage();
        massage.setTo(to);
        massage.setSubject(subject);
        massage.setText(body);
        massage.setFrom("twixiecommunity@gmail.com");
        mailSender.send(massage);
    }

    public void signUpEmail(Users u) {
        String subject = "Welcome to Twixie! \uD83C\uDF1F";
        String body = "Hi " + u.getUserName() + """
                , 
                Thank you for signing up to Twixie!
                We’re excited to have you on board.
                
                Your account has been successfully created, and you’re all set to start exploring everything Twixie has to offer. If you have any questions or need help getting started, feel free to reach out—we’re always here for you.
                
                Enjoy the experience,
                The Twixie Team
                """;
        sendEmail(u.getEmail(), subject, body);
    }

    @Async
    public void addPostEmail(Users poster) {
        String subject = poster.getUserName() + " Just Shared Something New on Twixie ✨";
        String body =
                "\nGood news! " + poster.getUserName() + " just shared a new post on Twixie 🎉.\n" + """
                        
                        Jump in to see what’s new and stay up to date with their latest content.
                        Discover insights, laughs, and inspiration in every post they share ✨.  
                        Twixie is all about connecting, exploring, and enjoying the creativity of our community, and this post is no exception.  
                        
                        Enjoy,  
                        The Twixie Team
                        """;
        poster.getFollowers().forEach(follow ->
        {
            sendEmail(follow.getFollower().getEmail(),
                    subject,
                    "Hi " + follow.getFollower().getUserName() + ",\n" + body);
        });

    }

    @Transactional
    public void sendVerificationEmail(String toEmail) {
        String verificationCode = VerificationCodeGenerator.generateVerificationCode();
        sendEmail(toEmail, "Email Verification Code", "Your verification code is: " + verificationCode);
        UserVerification userVerification = userVerificationRepository.findByEmail(toEmail);
        if(userVerification == null){
            userVerification = new UserVerification(toEmail, verificationCode);
        }
        else{
            userVerification.setVerificationCode(verificationCode);
        }
        userVerificationRepository.save(userVerification);
    }

}
