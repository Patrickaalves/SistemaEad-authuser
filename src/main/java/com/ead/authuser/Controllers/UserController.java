package com.ead.authuser.Controllers;

import com.ead.authuser.dto.UserDto;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Page<UserModel>> getAllUsers(Pageable pageable) {
        Page<UserModel> userModelPage = userService.findAll(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(userModelPage);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getOneUser(@PathVariable(value = "userId") UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findById(id).get());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable(value = "userId") UUID id) {
        userService.delete(userService.findById(id).get());
        return ResponseEntity.status(HttpStatus.OK).body("User deleted succesfully.");
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable(value = "userId") UUID id,
                                             @RequestBody @Validated(UserDto.UserView.UserPut.class)
                                             @JsonView(UserDto.UserView.UserPut.class) UserDto userDto) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(userDto, userService.findById(id).get()));
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<Object> updateUserPassword(@PathVariable(value = "userId") UUID id,
                                                     @RequestBody @Validated(UserDto.UserView.PasswordPut.class)
                                                     @JsonView(UserDto.UserView.PasswordPut.class) UserDto userDto) {
        Optional<UserModel> userModelOptional = userService.findById(id);
        if (!userModelOptional.get().getPassword().equals(userDto.oldPassword())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Mismatched old password.");
        }

        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUserPassword(userDto, userModelOptional.get()));
    }

    @PutMapping("/{userId}/image")
    public ResponseEntity<Object> updateUserImage(@PathVariable(value = "userId") UUID id,
                                                  @RequestBody @Validated(UserDto.UserView.ImagePut.class)
                                                  @JsonView(UserDto.UserView.ImagePut.class) UserDto userDto) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUserImage(userDto, userService.findById(id).get()));
    }
}
