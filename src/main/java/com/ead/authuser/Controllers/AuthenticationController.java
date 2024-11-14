package com.ead.authuser.Controllers;

import com.ead.authuser.dto.UserDto;
import com.ead.authuser.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@RequestBody @JsonView(UserDto.UserView.RegistrationPost.class)
                                                   UserDto userDto) {

        if (userService.existByUserName(userDto.username())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Username is Already taken!");
        }

        if (userService.existByEmail(userDto.email())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Email is Already taken!");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerUser(userDto));
    }
}
