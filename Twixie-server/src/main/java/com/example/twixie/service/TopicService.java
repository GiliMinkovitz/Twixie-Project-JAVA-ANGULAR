package com.example.twixie.service;

import com.example.twixie.service.repository.TopicRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class TopicService {
    private TopicRepository topicRepository;

    public TopicService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    public int updateRate(Long topicId, UpdateRateAction action) {
        return topicRepository.incrementTopicRate(topicId, action.getRateChange());
    }
}
