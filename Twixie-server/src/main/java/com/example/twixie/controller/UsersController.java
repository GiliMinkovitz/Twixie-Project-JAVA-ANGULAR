package com.example.twixie.controller;

import com.example.twixie.dto.UserBasicInfoDto;
import com.example.twixie.dto.UsersDTO;
import com.example.twixie.model.UserSettings;
import com.example.twixie.model.Users;
import com.example.twixie.security.CustomUserDetails;
import com.example.twixie.security.jwt.JwtUtils;
import com.example.twixie.service.EmailService;
import com.example.twixie.service.ImageUtills;
import com.example.twixie.service.VerificationCodeGenerator;
import com.example.twixie.service.VerificationService;
import com.example.twixie.service.mapper.UserBasicInfoMapper;
import com.example.twixie.service.mapper.UsersMapper;
import com.example.twixie.service.repository.RoleRepository;
import com.example.twixie.service.repository.UsersRepository;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    UsersRepository usersRepository;
    UsersMapper usersMapper;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final EmailService emailService;
    private final UserBasicInfoMapper userBasicInfoMapper;
    private final VerificationCodeGenerator verificationCodeGenerator;
    private final VerificationService verificationService;


    public UsersController(RoleRepository roleRepository, AuthenticationManager authenticationManager, JwtUtils jwtUtils, EmailService emailService, UserBasicInfoMapper userBasicInfoMapper, VerificationCodeGenerator verificationCodeGenerator, VerificationService verificationService, UsersMapper usersMapper, UsersRepository usersRepository) {
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.emailService = emailService;
        this.userBasicInfoMapper = userBasicInfoMapper;
        this.verificationCodeGenerator = verificationCodeGenerator;
        this.verificationService = verificationService;
        this.usersMapper = usersMapper;
        this.usersRepository = usersRepository;
    }

    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestPart("image") MultipartFile profileImage,
                                    @RequestPart("newUser") Users newUser, @RequestPart("verifyCode") String code) {
        try {
            if (usersRepository.existsUsersByUserName(newUser.getUserName())) {
                return new ResponseEntity<>(null, HttpStatus.CONFLICT);
            }

            boolean isVerified = verificationService.verifyCode(newUser.getEmail(), code);
            if (!isVerified) {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }

            //save the picture in the directory
            ImageUtills.uploadImage(profileImage);

            //saving the path of the picture in the user object
            newUser.setImagePath(profileImage.getOriginalFilename());

            //encoder the password
            String pass = newUser.getPassword();
            newUser.setPassword(new BCryptPasswordEncoder().encode(pass));

            newUser.getRoles().add(roleRepository.findById((long) 1).get());

            UserSettings settings = new UserSettings();
            newUser.setSettings(settings);
            newUser.getSettings().setUser(newUser);

            Users u = usersRepository.save(newUser);
            emailService.signUpEmail(u);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(usersMapper.userToUserDto(u));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("sendVerificationEmail")
    public ResponseEntity sendVerificationEmail(@RequestBody String email) {
        try {
            emailService.sendVerificationEmail(email);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/logIn")
    public ResponseEntity<?> logIn(@RequestBody Users u) {

        if (!usersRepository.existsUsersByUserName(u.getUserName())) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(u.getUserName(), u.getPassword()));

        //save the authentication
        SecurityContextHolder.getContext().setAuthentication(authentication);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(userDetails.getUsername());
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signOut() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        Map<String, String> response = new HashMap<>();
        response.put("message", "you've been signed out!");

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(response);
    }

    @GetMapping("/getUserById/{id}")
    public ResponseEntity<UsersDTO> getUserById(@PathVariable Long id) {
        try {
            Optional<Users> optionalUser = usersRepository.findById(id);
            if (optionalUser.isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }

            Users u = optionalUser.get();
            return new ResponseEntity<>(usersMapper.userToUserDto(u), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping({"/getUsersList"})
    public ResponseEntity<List<UsersDTO>> getUsersList() {
        try {
            List<Users> list = usersRepository.findAll();
            return new ResponseEntity<>(usersMapper.usersListToDto(list), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/getUserDetails")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UsersDTO> getUserById() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            Optional<Users> optionalUser = usersRepository.findByUserName(username);
            if (optionalUser.isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }

            Users u = optionalUser.get();
            return new ResponseEntity<>(usersMapper.userToUserDto(u), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getUsersName")
    public ResponseEntity<List<String>> getUserNames() {
        try {
            List<String> list = usersRepository.findAll()
                    .stream().map(user -> {
                        return user.getUserName();
                    }).toList();
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getBasicUsersList")
    public ResponseEntity<List<UserBasicInfoDto>> getBasicInfo() {
        try {
            List<UserBasicInfoDto> allList = userBasicInfoMapper.usersToBasicInfoDTO(
                    usersRepository.findAll());
            if (allList.isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(allList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/userBasicInfo")
    public ResponseEntity<UserBasicInfoDto> getBasicUserInfo(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        Long id = ((CustomUserDetails) principal).getId();
        UserBasicInfoDto currentUser = userBasicInfoMapper.userToBasicInfoDTO(
                usersRepository.findById(id).get());
        return new ResponseEntity<>(currentUser, HttpStatus.OK);
    }


}
