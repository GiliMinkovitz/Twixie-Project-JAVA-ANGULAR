package com.example.twixie.service.repository;

import com.example.twixie.model.Post;
import com.example.twixie.service.UpdateRateAction;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findPostsByPoster_UserId(Long id);

    @Query("SELECT p FROM Post p WHERE p.poster.userId <> :userId")
    List<Post> findAllExceptUserPosts(@Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query("UPDATE Post p SET p.rate = p.rate + :delta WHERE p.postId = :postId")
    int incrementUsersRate(Long postId, int delta);

    @Transactional
    @Modifying // חובה לפעולות עדכון
    @Query("UPDATE Post p SET p.rate = p.rate + :#{#updateRateAction.rateChange} WHERE p.rate > 0")
    int decrementAllPostsRate(UpdateRateAction updateRateAction);


    @Query("""
            SELECT p FROM Post p
            LEFT JOIN FETCH p.poster
            LEFT JOIN FETCH p.topic
            WHERE p.poster.userId <> :posterId
            """)
    Page<Post> findFeedExcludingCurrentUser(@Param("posterId") Long posterId, Pageable pageable);

    List<Post> findPostsByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String title, String content);

    List<Post> findPostByPoster_UserIdAndContentContainingIgnoreCaseOrTitleContainingIgnoreCase(Long userId, String content, String title);

}
