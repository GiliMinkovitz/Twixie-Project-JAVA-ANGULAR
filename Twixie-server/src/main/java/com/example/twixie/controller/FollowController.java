package com.example.twixie.controller;

import com.example.twixie.dto.UsersDTO;
import com.example.twixie.model.Follow;
import com.example.twixie.model.Users;
import com.example.twixie.security.CustomUserDetails;
import com.example.twixie.service.UpdateRateAction;
import com.example.twixie.service.UserService;
import com.example.twixie.service.mapper.UsersMapper;
import com.example.twixie.service.repository.FollowRepository;
import com.example.twixie.service.repository.UsersRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@RequestMapping({"api/follow"})
@RestController
public class FollowController {
    private final FollowRepository followRepository;
    private final UsersRepository usersRepository;
    private final UsersMapper usersMapper;
    private final UserService userService;

    public FollowController(FollowRepository followRepository, UsersRepository usersRepository, UsersMapper usersMapper, UserService userService) {
        this.followRepository = followRepository;
        this.usersRepository = usersRepository;
        this.usersMapper = usersMapper;
        this.userService = userService;
    }

    @Transactional
    @PostMapping("/addFollow/{followee_id}")
    public ResponseEntity<Follow> addFollow(Authentication authentication, @PathVariable Long followee_id) {
        try {
            Object principal = authentication.getPrincipal();
            Long follower_id = ((CustomUserDetails) principal).getId();
            Optional<Users> follower = usersRepository.findById(follower_id);
            Optional<Users> followee = usersRepository.findById(followee_id);
            Follow follow = this.followRepository.findFollowByFollowee_UserIdAndFollower_UserId(followee_id, follower_id);
            if (follow == null) {
                Follow newFollow = new Follow(follower.get(), followee.get(), LocalDateTime.now());
                followRepository.save(newFollow);
                userService.updateRate(followee_id, UpdateRateAction.FOLLOW);
                return new ResponseEntity<>(newFollow, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(null, HttpStatus.CONFLICT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/removeFollow/{followeeId}")
    @Transactional
    public ResponseEntity removeFollow(Authentication authentication, @PathVariable Long followeeId) {
        try {
            Object principal = authentication.getPrincipal();
            Long follower_id = ((CustomUserDetails) principal).getId();
            userService.updateRate(followeeId, UpdateRateAction.UNFOLLOW);
            followRepository.deleteFollowByFollowee_UserIdAndFollower_UserId(followeeId, follower_id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getFollowers")
    public ResponseEntity<List<UsersDTO>> getFollowers(Authentication authentication) {
        try {
            Object principal = authentication.getPrincipal();

            Long id = ((CustomUserDetails) principal).getId();

            List<UsersDTO> followers = usersMapper.usersListToDto(followRepository.findFollowersByFolloweeId(id));
            if (followers.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(followers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getFollowees")
    public ResponseEntity<List<UsersDTO>> getFollowees(Authentication authentication) {
        try {
            Object principal = authentication.getPrincipal();

            Long id = ((CustomUserDetails) principal).getId();
            List<UsersDTO> followees = usersMapper.usersListToDto(followRepository.findFolloweesByFollowerId(id));
            if (followees.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(followees, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();

            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/isFollowing/{followeeId}")
    public ResponseEntity<Boolean> isFollowing(Authentication authentication, @PathVariable Long followeeId) {
        try {
            Object principal = authentication.getPrincipal();
            Long followerId = ((CustomUserDetails) principal).getId();
            Follow follow = followRepository
                    .findFollowByFollowee_UserIdAndFollower_UserId(followeeId, followerId);
            if (follow != null) {
                return new ResponseEntity<>(true, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(false, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}


