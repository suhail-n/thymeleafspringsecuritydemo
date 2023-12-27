package com.example.thymeleafsecuritydemo.mapper;

import com.example.thymeleafsecuritydemo.dto.accounts.UserDto;
import com.example.thymeleafsecuritydemo.models.UserEntity;

public class UserMapper {
    public static UserEntity mapToUserEntity(UserDto userDto) {
        return UserEntity.builder()
                .email(userDto.getEmail())
                .username(userDto.getUsername())
                .build();
    }

    public static UserDto mapToUserDto(UserEntity userEntity) {
        return UserDto.builder()
                .email(userEntity.getEmail())
                .username(userEntity.getUsername())
                .build();
    }
}
