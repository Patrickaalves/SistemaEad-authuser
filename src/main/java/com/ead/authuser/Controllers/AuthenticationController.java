package com.ead.authuser.Controllers;

import com.ead.authuser.dto.UserDto;
import com.ead.authuser.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@RequestBody @Validated (UserDto.UserView.RegistrationPost.class)
                                               @JsonView(UserDto.UserView.RegistrationPost.class) UserDto userDto) {

        if (userService.existByUserName(userDto.username())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Username is Already taken!");
        }

        if (userService.existByEmail(userDto.email())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Email is Already taken!");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerUser(userDto));
    }

    @GetMapping("/logs")
    public String index(){
        logger.trace("TRACE");
        logger.debug("DEBUG");
        logger.info("INFO");
        logger.warn("WARN");
        logger.error("ERROR");

        return "Loggin spring boot";
    }
}
