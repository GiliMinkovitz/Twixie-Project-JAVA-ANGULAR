package com.example.twixie.service.mapper;

import com.example.twixie.dto.TopicDTO;
import com.example.twixie.model.Topic;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel="spring")
public interface TopicMapper {
    TopicDTO topicToDto(Topic topic);
    List<TopicDTO> topicsToDto(List<Topic> topics);
}
