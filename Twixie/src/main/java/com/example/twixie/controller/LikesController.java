package com.example.twixie.controller;

import com.example.twixie.model.Likes;
import com.example.twixie.model.Post;
import com.example.twixie.model.Users;
import com.example.twixie.service.PostService;
import com.example.twixie.service.UpdateRateAction;
import com.example.twixie.service.UserService;
import com.example.twixie.service.repository.LikesRepository;
import com.example.twixie.service.repository.PostRepository;
import com.example.twixie.service.repository.UsersRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequestMapping({"api/likes"})
@RestController
public class LikesController {
    private final UsersRepository usersRepository;
    private final LikesRepository likesRepository;
    private final UserService userService;
    private final PostService postService;
    private final PostRepository postRepository;

    public LikesController(LikesRepository likesRepository, UserService userService, PostService postService, PostRepository postRepository, UsersRepository usersRepository) {
        this.likesRepository = likesRepository;
        this.userService = userService;
        this.postService = postService;
        this.postRepository = postRepository;
        this.usersRepository = usersRepository;
    }

    @PostMapping("/addLike")
    @Transactional
    public ResponseEntity<?> addLike(@RequestBody Likes like) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            Users currentUser = usersRepository.findUsersByUserName(username);
            Post post = postRepository.findById(like.getParentPostLiked().getPostId()).get();

            like.setLiker(currentUser);

            //checking if the user already liked or disliked
            Optional<Likes> existingLikeOpt = likesRepository.findByLikerAndParentPostLiked(
                    currentUser, like.getParentPostLiked()
            );
            // if already liked \ disliked --
            if (existingLikeOpt.isPresent()) {
                Likes existing = existingLikeOpt.get();
                if (existing.isType() == like.isType()) {
                    likesRepository.delete(existing);
                    postService.updateRate(like.getParentPostLiked().getPostId(), like.isType() ? UpdateRateAction.UNLIKE : UpdateRateAction.UNDISLIKE);
                    userService.updateRate(currentUser.getUserId(), like.isType() ? UpdateRateAction.UNLIKE : UpdateRateAction.UNDISLIKE);
                    return ResponseEntity.ok("Removed like/dislike");
                } else {
                    existing.setType(like.isType());
                    existing.setDateLiked(LocalDateTime.now());
                    likesRepository.save(existing);

                    UpdateRateAction cancelAction = like.isType() ? UpdateRateAction.UNDISLIKE : UpdateRateAction.UNLIKE;
                    UpdateRateAction addAction = like.isType() ? UpdateRateAction.LIKE : UpdateRateAction.DISLIKE;

                    postService.updateRate(like.getParentPostLiked().getPostId(), cancelAction);
                    postService.updateRate(like.getParentPostLiked().getPostId(), addAction);
                    userService.updateRate(currentUser.getUserId(), cancelAction);
                    userService.updateRate(currentUser.getUserId(), addAction);

                    return new ResponseEntity<>("Switched to " + (like.isType() ? "LIKE" : "DISLIKE"), HttpStatus.OK);
                }
                //add a new like\dislike
            } else {
                like.setDateLiked(LocalDateTime.now());
                likesRepository.save(like);

                userService.updateRate(currentUser.getUserId(), like.isType() ? UpdateRateAction.LIKE : UpdateRateAction.DISLIKE);
                postService.updateRate(like.getParentPostLiked().getPostId(), like.isType() ? UpdateRateAction.LIKE : UpdateRateAction.DISLIKE);

                return new ResponseEntity<>("Added " + (like.isType() ? "LIKE" : "DISLIKE"), HttpStatus.CREATED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error processing like", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getLikesByPost/{id}")
    public ResponseEntity<List<Likes>> getLikesByPost(@PathVariable Long id) {
        try {
            List<Likes> likes = likesRepository.findLikesByParentPostLiked_PostId(id);
            return new ResponseEntity<>(likes, likes != null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getLikesByUser/{id}")
    public ResponseEntity<List<Likes>> getLikesByUser(@PathVariable Long id) {
        try {
            List<Likes> likes = likesRepository.findLikesByLiker_UserId(id);
            return new ResponseEntity<>(likes, likes != null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getLikesByType/{type}")
    public ResponseEntity<List<Likes>> getLikesByType(@PathVariable boolean type) {
        try {
            List<Likes> likes = likesRepository.findLikesByType(type);
            return new ResponseEntity<>(likes, likes != null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getAmountOfLikes/{postId}")
    public ResponseEntity<Integer> getAmoutOfLikesForPost(@PathVariable Long postId, @RequestParam boolean type) {
        try {
            int amount = likesRepository.countLikesByParentPostLiked_PostIdAndType(postId, type);
            return new ResponseEntity<>(amount, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
