package com.softeams.poSystem.security.mapper.Interface;


import com.softeams.poSystem.security.dto.UserDto;
import com.softeams.poSystem.security.dto.UserRegistrationDto;
import com.softeams.poSystem.security.entities.User;

import java.util.List;

public interface IUserMapper {
    User convertToEntity(UserRegistrationDto userRegistrationDto);
    String encodePassword(String password);
    UserDto convertToDto(User user);
    List<UserDto> convertToDto(List<User> user);
}
