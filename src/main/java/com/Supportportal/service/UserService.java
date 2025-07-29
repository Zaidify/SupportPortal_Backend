package com.Supportportal.service;

import com.Supportportal.domain.User;
import com.Supportportal.exception.domain.EmailExistException;
import com.Supportportal.exception.domain.UserNotFoundException;
import com.Supportportal.exception.domain.UsernameExistException;

import javax.mail.MessagingException;
import java.util.List;

public interface UserService {
     User register(String firstName, String lastName, String username, String email) throws UserNotFoundException, EmailExistException, UsernameExistException, MessagingException;
     List<User> getUsers();
     User findUserByUsername(String username);
     User findUserByEmail(String email);
}
