package com.Supportportal.service;

import com.Supportportal.domain.User;
import com.Supportportal.exception.domain.EmailExistException;
import com.Supportportal.exception.domain.EmailNotFoundException;
import com.Supportportal.exception.domain.UserNotFoundException;
import com.Supportportal.exception.domain.UsernameExistException;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

public interface UserService {
     User register(String firstName, String lastName, String username, String email) throws UserNotFoundException, EmailExistException, UsernameExistException, MessagingException;
     List<User> getUsers();
     User findUserByUsername(String username);
     User findUserByEmail(String email);
     User addNewUser(String firstName, String lastName, String username, String email, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage) throws UserNotFoundException, EmailExistException, UsernameExistException, IOException;
     User updateUser(String currentUsername, String newFirstName,String newLastName, String newUsername, String newEmail, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage) throws UserNotFoundException, EmailExistException, UsernameExistException, IOException;
     void deleteUser(Long id);
     void resetPassword(String email) throws MessagingException, EmailNotFoundException;
     User updateProfileImage(String username, MultipartFile profileImage) throws UserNotFoundException, EmailExistException, UsernameExistException, IOException;
}
