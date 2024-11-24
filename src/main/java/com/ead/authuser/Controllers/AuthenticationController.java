package com.ead.authuser.Controllers;

import com.ead.authuser.dto.UserDto;
import com.ead.authuser.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    Logger logger = LogManager.getLogger(AuthenticationController.class);

    final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@RequestBody @Validated (UserDto.UserView.RegistrationPost.class)
                                               @JsonView(UserDto.UserView.RegistrationPost.class) UserDto userDto) {

        logger.debug("POST registerUser userDto received {}", userDto);
        if (userService.existByUserName(userDto.username())) {
            logger.warn("Username {} is Already taken", userDto.username());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Username is Already taken!");
        }

        if (userService.existByEmail(userDto.email())) {
            logger.warn("Email {} is Already taken", userDto.email());
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
