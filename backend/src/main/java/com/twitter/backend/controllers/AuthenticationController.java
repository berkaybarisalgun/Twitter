package com.twitter.backend.controllers;

import com.twitter.backend.models.ApplicationUser;
import com.twitter.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {


    private final UserService userService;;

    @Autowired
    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ApplicationUser registeruser(@RequestBody ApplicationUser user) {
        return userService.registerUser(user);
    }


}
