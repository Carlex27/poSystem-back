package com.softeams.poSystem.security.mapper.Interface;


import com.softeams.poSystem.security.dto.UserRegistrationDto;
import com.softeams.poSystem.security.entities.User;

public interface IUserMapper {
    User convertToEntity(UserRegistrationDto userRegistrationDto);
    String encodePassword(String password);
}
