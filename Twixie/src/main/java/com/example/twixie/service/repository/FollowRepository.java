package com.example.twixie.service.repository;

import com.example.twixie.model.Follow;
import com.example.twixie.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    List<Users> findAllByFollowee_UserId(Long id);

    @Query("SELECT f.followee FROM Follow f WHERE f.follower.userId = :userId")
    List<Users> findFolloweesByFollowerId(@Param("userId") Long id);

    @Query("SELECT f.follower FROM Follow f WHERE f.followee.userId = :userId")
    List<Users> findFollowersByFolloweeId(@Param("userId") Long id);

    @Modifying
    @Transactional
    void deleteFollowByFollowee_UserIdAndFollower_UserId(Long followerId, Long followeeId);

    Follow findFollowByFollowee_UserIdAndFollower_UserId(Long followeeId, Long followerId);

}
