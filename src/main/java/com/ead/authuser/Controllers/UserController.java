package com.ead.authuser.Controllers;

import com.ead.authuser.dto.UserDto;
import com.ead.authuser.exceptions.GlobalExceptionHandler;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import com.ead.authuser.specifications.SpecificationTemplate;
import com.fasterxml.jackson.annotation.JsonView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/users")
public class UserController {

    Logger logger = LogManager.getLogger(UserController.class);

    final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Page<UserModel>> getAllUsers(SpecificationTemplate.UserSpec spec, Pageable pageable) {
        Page<UserModel> userModelPage = userService.findAll(spec, pageable);
        if (!userModelPage.isEmpty()) {
            for (UserModel user : userModelPage.toList()) {
                user.add(linkTo(methodOn(UserController.class).getOneUser(user.getUserId())).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(userModelPage);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getOneUser(@PathVariable(value = "userId") UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findById(id).get());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable(value = "userId") UUID id) {
        logger.debug("DELETE registerUser userDto received {}", id);
        userService.delete(userService.findById(id).get());
        return ResponseEntity.status(HttpStatus.OK).body("User deleted succesfully.");
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable(value = "userId") UUID id,
                                             @RequestBody @Validated(UserDto.UserView.UserPut.class)
                                             @JsonView(UserDto.UserView.UserPut.class) UserDto userDto) {
        logger.debug("PUT updateUser userDto received {}", userDto);
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(userDto, userService.findById(id).get()));
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<Object> updateUserPassword(@PathVariable(value = "userId") UUID id,
                                                     @RequestBody @Validated(UserDto.UserView.PasswordPut.class)
                                                     @JsonView(UserDto.UserView.PasswordPut.class) UserDto userDto) {
        logger.debug("PUT updatePassword userId received {}", id);
        Optional<UserModel> userModelOptional = userService.findById(id);
        if (!userModelOptional.get().getPassword().equals(userDto.oldPassword())) {
            logger.warn("Mismatched old password! userId {} ", id);
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Mismatched old password.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUserPassword(userDto, userModelOptional.get()));
    }

    @PutMapping("/{userId}/image")
    public ResponseEntity<Object> updateUserImage(@PathVariable(value = "userId") UUID id,
                                                  @RequestBody @Validated(UserDto.UserView.ImagePut.class)
                                                  @JsonView(UserDto.UserView.ImagePut.class) UserDto userDto) {
        logger.debug("PUT updateImage userId received {}", id);
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUserImage(userDto, userService.findById(id).get()));
    }
}
