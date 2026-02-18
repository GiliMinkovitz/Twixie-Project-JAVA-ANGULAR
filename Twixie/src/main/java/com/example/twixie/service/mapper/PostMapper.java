
package com.example.twixie.service.mapper;
import com.example.twixie.dto.PostDTO;
import com.example.twixie.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UsersMapper.class})
public interface PostMapper{

    @Mapping(target = "topicName", source = "topic.name")
    @Mapping(target = "topicId", source = "topic.topicId")
    @Mapping(target = "imagePath", source = "imagePath", qualifiedByName = "loadImage")
    PostDTO postToPostDTO(Post post);

    List<PostDTO> postsToPostsDTO(List<Post> posts);

}

