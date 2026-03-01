package com.example.twixie.service.mapper;

import com.example.twixie.dto.UserBasicInfoDto;
import com.example.twixie.model.Users;
import com.example.twixie.service.ImageUtills;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.io.IOException;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserBasicInfoMapper {

    @Mapping(target = "picture", source = "imagePath", qualifiedByName = "loadImage")
    UserBasicInfoDto userToBasicInfoDTO(Users u);

    List<UserBasicInfoDto> usersToBasicInfoDTO(List<Users> usersList);


    @Named("loadImage")
    default String mapImage(String path) throws IOException {
        if (path == null) return null;
        return ImageUtills.getImage(path);
    }


}
