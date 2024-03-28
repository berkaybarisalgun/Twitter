package com.twitter.backend.controllers;

import com.twitter.backend.exceptions.EmailAlreadyTakenException;
import com.twitter.backend.exceptions.EmailFailedToSendException;
import com.twitter.backend.exceptions.UserdoesNotExistException;
import com.twitter.backend.models.ApplicationUser;
import com.twitter.backend.models.RegistrationObject;
import com.twitter.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {


    private final UserService userService;;

    @Autowired
    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @ExceptionHandler({EmailAlreadyTakenException.class})
    public ResponseEntity<String> handleEmailTaken(){
            return new ResponseEntity<String>("The email provided is already in use", HttpStatus.CONFLICT);
    }

    @PostMapping("/register")
    public ApplicationUser registeruser(@RequestBody RegistrationObject ro) {
        return userService.registerUser(ro);
    }

    @ExceptionHandler({UserdoesNotExistException.class})
    public ResponseEntity<String> handleUserDoesNotExist(){
        return new ResponseEntity<String>("The user does not exist", HttpStatus.NOT_FOUND);
    }


    @PutMapping("/update/phone")
    public ApplicationUser updatePhoneNumber(@RequestBody LinkedHashMap<String,String> body) {
        
        String username=body.get("username");
        String phone=body.get("phone");

        ApplicationUser user=userService.getUserByUserName(username);

        user.setPhone(phone);

        return userService.updateUser(user);
    }

    @ExceptionHandler({EmailFailedToSendException.class})
    public ResponseEntity<String> handleFailedEmail(){
        return new ResponseEntity<String>("Unable to send email,try again in a moment", HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @PostMapping("/email/code")
    public ResponseEntity<String> createEmailVerification(@RequestBody LinkedHashMap<String,String> body) {
        userService.generateEmailVerification(body.get("username"));

        return new ResponseEntity<String>("Verification code generated, email sent", HttpStatus.OK);
    }




}
