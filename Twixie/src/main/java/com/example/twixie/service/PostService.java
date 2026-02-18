package com.example.twixie.service;

import com.example.twixie.service.repository.PostRepository;
import com.example.twixie.service.repository.TopicRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final TopicRepository topicRepository;

    public PostService(PostRepository postRepository, TopicRepository topicRepository) {
        this.postRepository = postRepository;
        this.topicRepository = topicRepository;
    }

    public int updateRate(Long postId, UpdateRateAction action) {
        return postRepository.incrementUsersRate(postId, action.getRateChange());
    }

    @Scheduled(fixedRate = 3600000)
    @Transactional
    public void schedulePostRateDecrement() {
        int updatedCount = postRepository.decrementAllPostsRate(UpdateRateAction.DECREMENT_AGING);
    }
}
