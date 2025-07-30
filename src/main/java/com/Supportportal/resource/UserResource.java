    package com.Supportportal.resource;

    import com.Supportportal.domain.HttpResponse;
    import com.Supportportal.domain.User;
    import com.Supportportal.domain.UserPrincipal;
    import com.Supportportal.exception.domain.*;
    import com.Supportportal.service.UserService;
    import com.Supportportal.utility.JWTTokenProvider;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpHeaders;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.access.prepost.PreAuthorize;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.web.multipart.MultipartFile;

    import javax.mail.MessagingException;

    import java.io.IOException;
    import java.util.List;

    import static com.Supportportal.constant.SecurityConstant.JWT_TOKEN_HEADER;
    import static org.springframework.http.HttpStatus.NO_CONTENT;
    import static org.springframework.http.HttpStatus.OK;

    @RestController
    @RequestMapping(path = {"/","/user"})
    public class UserResource extends ExceptionHandling {

        public static final String EMAIL_SENT = "An email with new password was sent to: ";
        public static final String USER_DELETED_SUCCESSFULLY = "User deleted successfully";
        private UserService userService;
    private AuthenticationManager authenticationManager;
    private JWTTokenProvider jwtTokenProvider;

    @Autowired
    public UserResource(UserService userService, AuthenticationManager authenticationManager, JWTTokenProvider jwtTokenProvider) {
    this.userService = userService;
    this.authenticationManager = authenticationManager;
    this.jwtTokenProvider = jwtTokenProvider;
    }


    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user) {
    authenticate(user.getUsername(),user.getPassword());
    User loginUser = userService.findUserByUsername(user.getUsername());
    UserPrincipal userPrincipal = new UserPrincipal(loginUser);
    HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
    return new ResponseEntity<>(loginUser,jwtHeader, OK);
    }


    @PostMapping("/register")
    public ResponseEntity<User>  register(@RequestBody User user) throws UserNotFoundException, EmailExistException, UsernameExistException, MessagingException {
    User newUser = userService.register(user.getFirstName(), user.getLastName(),user.getUsername(),user.getEmail());
    return new ResponseEntity<>(newUser, OK);
    }

    @PostMapping("/add")
    public ResponseEntity<User>addNewUser(@RequestParam("firstName") String firstName,
                                          @RequestParam("lastName") String lastName,
                                          @RequestParam("username") String username,
                                          @RequestParam("email") String email,
                                          @RequestParam("role") String role,
                                          @RequestParam("isActive") String isActive,
                                          @RequestParam("isNonLocked") String isNonLocked,
                                          @RequestParam(value = "profileImage", required = false) MultipartFile profileImage ) throws UserNotFoundException, EmailExistException, IOException, UsernameExistException {
        User newUser = userService.addNewUser(firstName,lastName,username,email,role, Boolean.parseBoolean(isNonLocked),Boolean.parseBoolean(isActive),profileImage);
        return new ResponseEntity<>(newUser, OK);

    }

        @PostMapping("/update")
        public ResponseEntity<User>update(@RequestParam("currentUsername") String currentUsername,
                                          @RequestParam("firstName") String firstName,
                                          @RequestParam("lastName") String lastName,
                                          @RequestParam("username") String username,
                                          @RequestParam("email") String email,
                                          @RequestParam("role") String role,
                                          @RequestParam("isActive") String isActive,
                                          @RequestParam("isNonLocked") String isNonLocked,
                                          @RequestParam(value = "profileImage", required = false) MultipartFile profileImage ) throws UserNotFoundException, EmailExistException, IOException, UsernameExistException {
            User updatedUser = userService.updateUser(currentUsername,firstName,lastName,username,email,role, Boolean.parseBoolean(isNonLocked),Boolean.parseBoolean(isActive),profileImage);
            return new ResponseEntity<>(updatedUser, OK);
        }

        @GetMapping("/find/{username}")
        public ResponseEntity<User> getUser(@PathVariable("username") String username) {
        User user = userService.findUserByUsername(username);
        return new ResponseEntity<>(user, OK);
        }

        @GetMapping("/list")
        public ResponseEntity<List<User>> getAllUsers() {
            List<User> users = userService.getUsers();
            return new ResponseEntity<>(users, OK);
        }

        @GetMapping("/resetPassword/{email}")
        public ResponseEntity<HttpResponse> resetPassword(@PathVariable("email") String email) throws EmailNotFoundException, MessagingException {
        userService.resetPassword(email);
            return response(OK, EMAIL_SENT + email);
        }

        @DeleteMapping("/delete/{id}")
        @PreAuthorize("hasAnyAuthority('user:delete')")
        public ResponseEntity<HttpResponse> deleteUser(@PathVariable("id") long id){
        userService.deleteUser(id);
        return response(NO_CONTENT, USER_DELETED_SUCCESSFULLY);
        }

        @PostMapping("/updateProfileImage")
        public ResponseEntity<User>updateProfileImage( @RequestParam("username") String username, @RequestParam(value = "profileImage") MultipartFile profileImage ) throws UserNotFoundException, EmailExistException, IOException, UsernameExistException {
            User user = userService.updateProfileImage(username,profileImage);
        return new ResponseEntity<>(user, OK);
        }

        private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
            return  new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus,httpStatus.getReasonPhrase().toUpperCase(),
                    message.toUpperCase()), httpStatus);
        }

        private HttpHeaders getJwtHeader(UserPrincipal user) {
    HttpHeaders headers = new HttpHeaders();
    headers.add(JWT_TOKEN_HEADER,jwtTokenProvider.generateJwtToken(user));
    return headers;
    }

    private void authenticate(String username, String password) {
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }
    }
