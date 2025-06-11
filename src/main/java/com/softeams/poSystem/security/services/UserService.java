package com.softeams.poSystem.security.services;


import com.softeams.poSystem.security.dto.UserRegistrationDto;
import com.softeams.poSystem.security.entities.User;
import com.softeams.poSystem.security.mapper.Interface.IUserMapper;
import com.softeams.poSystem.security.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final IUserMapper userMapper;
    /**
     * Find user by id in the database
     * This method is only for admin porpuses
     * @param id
     * @return
     */

    public Optional<User> findById(Long id){
        log.info("Finding user by id: {}",id);
        return Optional.ofNullable(userRepository.findById(id))
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public Optional<User> findByUsername(String username){
        log.info("Finding user by username: {}",username);
        return Optional.ofNullable(userRepository.findByUsername(username))
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public List<User> findAll(){
        log.info("Finding all users");
        return userRepository.findAllByIsActiveTrue();
    }

    public List<User> findByRoles(String roles){
        return userRepository.findByRoles(roles);
    }

    public List<User> search(String query){
        log.info("Searching for users with query: {}", query);
        return userRepository.findByIsActiveTrueAndUsernameContainingIgnoreCase(query);
    }

    //UPDATE
    public ResponseEntity<?> updateUser(Long id, UserRegistrationDto userDto){
        log.info("Updating user with id: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.setUsername(userDto.userName());
        user.setPassword(userMapper.encodePassword(userDto.userPassword()));
        return ResponseEntity.ok(userRepository.save(user));
    }

    //DELETE

    public ResponseEntity<?> deleteUserById(Long id){
        log.info("Deleting user with id: {}", id);
        if(id == 1L){
            log.error("Cannot delete default user");
            return ResponseEntity.badRequest().body("Cannot delete default user");
        }
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        user.setActive(false);
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }
}
