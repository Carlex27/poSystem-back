package com.softeams.poSystem.security.mapper;


import com.softeams.poSystem.security.dto.UserRegistrationDto;
import com.softeams.poSystem.security.entities.User;
import com.softeams.poSystem.security.mapper.Interface.IUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper implements IUserMapper {
    private final PasswordEncoder passwordEncoder;

    public User convertToEntity(UserRegistrationDto userRegistrationDto){
        User userEntity = new User();
        userEntity.setUsername(userRegistrationDto.userName());
        userEntity.setPassword(passwordEncoder.encode(userRegistrationDto.userPassword()));
        userEntity.setRoles(userRegistrationDto.userRole());
        return userEntity;
    }

    public String encodePassword(String password){
        return passwordEncoder.encode(password);
    }
}
