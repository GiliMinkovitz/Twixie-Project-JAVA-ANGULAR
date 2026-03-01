package com.example.twixie.service.repository;

import com.example.twixie.model.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ResponseRepository extends JpaRepository<Response, Long> {
    List<Response> findResponsesByParentResponseID(Long parentResponseID);

    @Query("SELECT r FROM Response r WHERE r.parentPostResponse.postId = :postId")
    List<Response> findAllResponsesByPostId(@Param("postId") Long postId);

}
