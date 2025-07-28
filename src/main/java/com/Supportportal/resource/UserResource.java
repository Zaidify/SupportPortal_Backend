package com.Supportportal.resource;

import com.Supportportal.domain.User;
import com.Supportportal.exception.domain.EmailExistException;
import com.Supportportal.exception.domain.ExceptionHandling;
import com.Supportportal.exception.domain.UserNotFoundException;
import com.Supportportal.exception.domain.UsernameExistException;
import com.Supportportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(path = {"/","/user"})
public class UserResource extends ExceptionHandling {

    private UserService userService;

    @Autowired
    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User>  register(@RequestBody User user) throws UserNotFoundException, EmailExistException, UsernameExistException {
       User newUser = userService.register(user.getFirstName(), user.getLastName(),user.getUsername(),user.getEmail());
        return new ResponseEntity<>(newUser, OK);
    }
}
