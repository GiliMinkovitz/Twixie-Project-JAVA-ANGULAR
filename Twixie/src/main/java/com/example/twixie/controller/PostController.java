package com.example.twixie.controller;

import com.example.twixie.dto.PostDTO;
import com.example.twixie.model.Post;
import com.example.twixie.model.Topic;
import com.example.twixie.model.Users;
import com.example.twixie.security.CustomUserDetails;
import com.example.twixie.service.*;
import com.example.twixie.service.mapper.PostMapper;
import com.example.twixie.service.repository.PostRepository;
import com.example.twixie.service.repository.TopicRepository;
import com.example.twixie.service.repository.UsersRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequestMapping({"api/post"})
@RestController
public class PostController {

    private final UserService userService;
    private final UsersRepository usersRepository;
    private final TopicRepository topicRepository;
    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final TopicService topicService;
    private final EmailService emailService;


    public PostController(PostRepository postRepository, PostMapper postMapper, TopicService topicService, UserService userService, UsersRepository usersRepository, TopicRepository topicRepository, EmailService emailService) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
        this.topicService = topicService;
        this.userService = userService;
        this.usersRepository = usersRepository;
        this.topicRepository = topicRepository;
        this.emailService = emailService;
    }

    @GetMapping("/getMyPostsByUser")
    public ResponseEntity<List<PostDTO>> getMyPostsByUser(Authentication authentication) {
        try {
            Object principal = authentication.getPrincipal();
            Long id = ((CustomUserDetails) principal).getId();

            List<PostDTO> posts = postMapper.postsToPostsDTO((postRepository.findPostsByPoster_UserId(id)));

            if (posts.isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(posts, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/addPost")
    @Transactional
    public ResponseEntity<PostDTO> addPost(@Valid @RequestPart("post") Post post,
                                           @RequestPart("image") MultipartFile image, @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            //upload image
            ImageUtills.uploadImage(image);
            post.setImagePath(image.getOriginalFilename());

            //set the right poster
            Long userId = userDetails.getId();
            Users currentUser = usersRepository.findById(userId).get();
            post.setPoster(currentUser);

            //rate the post
            Topic topic = topicRepository.findById(post.getTopic().getTopicId()).get();
            post.setRate((long) (0.7 * currentUser.getRate() + 0.3 * topic.getRate()));

            //set the right time
            post.setLastUpdated(LocalDateTime.now());
            post.setLastUpdated(LocalDateTime.now());

            //save the post
            Post p = postRepository.save(post);

            //update the user's and topic's rate
            topicService.updateRate(post.getTopic().getTopicId(), UpdateRateAction.INCREMENT_TOPIC);
            userService.updateRate(post.getPoster().getUserId(), UpdateRateAction.INCREMENT_USER_ADD_POST);

            emailService.addPostEmail(p.getPoster());
            return new ResponseEntity<>(postMapper.postToPostDTO(p), HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getPostDTOByPostId/{id}")
    public ResponseEntity<PostDTO> getPostDTOById(@PathVariable Long id) {
        try {
            Post p = postRepository.findById(id).get();
            if (p == null)
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            else
                return new ResponseEntity<>(postMapper.postToPostDTO(p), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deletePostById/{postId}")
    @PreAuthorize("hasRole('ADMIN') or @postRepository.findById(#postId).orElse(null)?.poster.userId == authentication.principal.id")
    @Transactional
    public ResponseEntity deletePostById(@PathVariable Long postId) {
        try {
            Optional<Post> postOptional = postRepository.findById(postId);
            if (postOptional.isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            Post p = postOptional.get();
            userService.updateRate(p.getPoster().getUserId(), UpdateRateAction.DECREMENT_USER_DELETE_POST);
            topicService.updateRate(p.getTopic().getTopicId(), UpdateRateAction.DECREMENT_TOPIC);
            postRepository.deleteById(postId);

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getAllPosts")
    public ResponseEntity<List<PostDTO>> getAllPosts(Authentication authentication) {
        try {
            Object principal = authentication.getPrincipal();
            Long id = ((CustomUserDetails) principal).getId();

            List<Post> posts = postRepository.findAllExceptUserPosts(id);
            if (posts == null)
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            else
                return new ResponseEntity<>(postMapper.postsToPostsDTO(posts), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/feed")
    public ResponseEntity<Page<PostDTO>> getFeed(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size) {

        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();

        Pageable pageable = PageRequest.of(page, size, Sort.by("rate").descending());

        Page<Post> postsPage = postRepository.findFeedExcludingCurrentUser(userId, pageable);

        Page<PostDTO> dtoPage = postsPage.map(postMapper::postToPostDTO);

        return ResponseEntity.ok(dtoPage);
    }

    @PutMapping("/editPost/{postID}")
    @PreAuthorize("hasRole('ADMIN') or @postRepository.findById(#postID).orElse(null)?.poster.userId == authentication.principal.id")
    public ResponseEntity<PostDTO> editPost(@PathVariable Long postID,
                                            @RequestPart(value = "content", required = false) String content,
                                            @RequestPart(value = "image", required = false) MultipartFile image) {
        Optional<Post> existingPostOpt = postRepository.findById(postID);
        if (existingPostOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Post post = existingPostOpt.get();
        try {
            if (image != null && !image.isEmpty()) {
                ImageUtills.uploadImage(image);
                post.setImagePath(image.getOriginalFilename());
            }

            if (content != null) {
                if (content != null) {
                    post.setContent(content);
                }
            }
            post.setLastUpdated(LocalDateTime.now());
            Post savedPost = postRepository.save(post);
            return ResponseEntity.ok(postMapper.postToPostDTO(savedPost));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}




