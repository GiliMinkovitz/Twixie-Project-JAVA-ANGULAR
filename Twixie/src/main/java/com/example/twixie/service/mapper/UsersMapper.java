package com.example.twixie.service.mapper;

import com.example.twixie.dto.UsersDTO;
import com.example.twixie.model.Users;
import com.example.twixie.service.ImageUtills;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.io.IOException;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UsersMapper {

    @Mapping(target = "picture", source = "imagePath", qualifiedByName = "loadImage")
    UsersDTO userToUserDto(Users u);

    List<UsersDTO> usersListToDto(List<Users> list);

    @Named("loadImage")
    default String mapImage(String path) throws IOException {
        if (path == null) return null;
        return ImageUtills.getImage(path);
    }
}
