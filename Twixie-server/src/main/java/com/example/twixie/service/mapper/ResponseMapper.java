package com.example.twixie.service.mapper;

import com.example.twixie.dto.ResponseDTO;
import com.example.twixie.model.Response;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UsersMapper.class, PostMapper.class})
public interface ResponseMapper {

    @Mapping(target = "responser", source = "responser")
    @Mapping(target = "parentPostResponseId", source = "parentPostResponse.postId")
    ResponseDTO responseToResponseDto(Response response);

    List<ResponseDTO> responsesToDto(List<Response> responses);
}
