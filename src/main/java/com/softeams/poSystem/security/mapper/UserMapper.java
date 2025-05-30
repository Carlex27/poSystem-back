package com.softeams.poSystem.security.mapper;


import com.softeams.poSystem.security.dto.UserDto;
import com.softeams.poSystem.security.dto.UserRegistrationDto;
import com.softeams.poSystem.security.entities.User;
import com.softeams.poSystem.security.mapper.Interface.IUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapper implements IUserMapper {
    private final PasswordEncoder passwordEncoder;

    public User convertToEntity(UserRegistrationDto userRegistrationDto){
        User userEntity = new User();
        userEntity.setUsername(userRegistrationDto.userName());
        userEntity.setPassword(passwordEncoder.encode(userRegistrationDto.userPassword()));
        userEntity.setRoles(userRegistrationDto.userRole());
        userEntity.setCreatedAt(LocalDateTime.now());
        return userEntity;
    }

    public String encodePassword(String password){
        return passwordEncoder.encode(password);
    }

    public UserDto convertToDto(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getRoles(),
                user.getCreatedAt()
        );
    }
    public List<UserDto> convertToDto(List<User> user) {
        return user.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}
