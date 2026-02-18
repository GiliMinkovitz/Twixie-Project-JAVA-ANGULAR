package com.example.twixie.service.repository;

import com.example.twixie.model.Likes;
import com.example.twixie.model.Post;
import com.example.twixie.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    List<Likes> findLikesByParentPostLiked_PostId(Long id);

    List<Likes> findLikesByLiker_UserId(Long id);

    List<Likes> findLikesByType(boolean type);

    Optional<Likes> findByLikerAndParentPostLiked(Users liker, Post parentPostLiked);

    int countLikesByParentPostLiked_PostIdAndType(Long parentPostLikedPostId, boolean type);
}
