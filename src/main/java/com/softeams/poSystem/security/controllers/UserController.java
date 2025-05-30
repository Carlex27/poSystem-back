package com.softeams.poSystem.security.controllers;


import com.softeams.poSystem.security.dto.UserRegistrationDto;
import com.softeams.poSystem.security.mapper.Interface.IUserMapper;
import com.softeams.poSystem.security.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final IUserMapper userMapper;

    //READ
    @PreAuthorize("hasAuthority('SCOPE_READ')")
    @GetMapping("/all")
    public ResponseEntity<?> allUsers() {
        return ResponseEntity.ok(userMapper.convertToDto(userService.findAll()));
    }

    @PreAuthorize("hasAuthority('SCOPE_READ')")
    @GetMapping("/find/id/{id}")
    public ResponseEntity<?> findUserById(Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PreAuthorize("hasAuthority('SCOPE_READ')")
    @GetMapping("/find/username/{username}")
    public ResponseEntity<?> findUserByUsername(String username) {
        return ResponseEntity.ok(userService.findByUsername(username));
    }

    @PreAuthorize("hasAuthority('SCOPE_READ')")
    @GetMapping("/search")
    public ResponseEntity<?> searchUser(@RequestParam("q") String query) {
        log.info("[UserController:searchUser] Searching for users with query: {}", query);
        return ResponseEntity.ok(userMapper.convertToDto(userService.search(query)));
    }

    //UPDATE

    @PreAuthorize("hasAuthority('SCOPE_WRITE')")
    @PostMapping("/update")
    public ResponseEntity<?> updateUser(
            @RequestParam("id") Long id,
            @Valid
            @RequestBody UserRegistrationDto userDto
            ) {
        return ResponseEntity.ok(userService.updateUser(id, userDto));
    }

    //DELETE

    @PreAuthorize("hasAuthority('SCOPE_DELETE')")
    @DeleteMapping("/delete/id/{id}")
    public ResponseEntity<?> deleteUserById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(userService.deleteUserById(id));
    }

}
