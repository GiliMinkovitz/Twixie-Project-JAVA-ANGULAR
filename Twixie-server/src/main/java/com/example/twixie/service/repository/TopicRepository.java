package com.example.twixie.service.repository;

import com.example.twixie.model.Topic;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TopicRepository extends JpaRepository<Topic, Long> {
    @Transactional
    @Modifying
    @Query("UPDATE Topic t SET t.rate = t.rate + :delta WHERE t.topicId = :topicId")
    int incrementTopicRate(Long topicId, int delta);
}
