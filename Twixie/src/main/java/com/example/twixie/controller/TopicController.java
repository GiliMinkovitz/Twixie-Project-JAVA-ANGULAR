package com.example.twixie.controller;

import com.example.twixie.dto.TopicDTO;
import com.example.twixie.model.Topic;
import com.example.twixie.service.mapper.TopicMapper;
import com.example.twixie.service.repository.TopicRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping({"api/topic"})
@RestController
public class TopicController {

    private final TopicRepository topicRepository;
    private final TopicMapper topicMapper;

    public TopicController(TopicRepository topicRepository, TopicMapper topicMapper) {
        this.topicRepository = topicRepository;
        this.topicMapper = topicMapper;
    }

    @GetMapping({"/getTopicById/{id}"})
    public ResponseEntity<TopicDTO> getTopicById(@PathVariable Long id) {
        try {
            Topic t = topicRepository.findById(id).get();
            if (t == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(topicMapper.topicToDto(t), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping({"/getTopics"})
    public ResponseEntity<List<TopicDTO>> getTopics(){
        try {
            List<Topic> list = topicRepository.findAll();
            return new ResponseEntity<>(topicMapper.topicsToDto(list), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping({"/addTopic"})
    public ResponseEntity<TopicDTO> addTopic(@RequestBody Topic topic){
        try {
            Topic t = topicRepository.save(topic);
            return new ResponseEntity<>(topicMapper.topicToDto(t), HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
