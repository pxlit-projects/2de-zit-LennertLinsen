package be.pxl.services.controller;

import be.pxl.services.domain.User;
import be.pxl.services.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/byCredentials")
    public ResponseEntity<User> getUserByEmailAndPassword(@RequestBody LoginRequest credentials) {

        User user = userService.getUserByEmailAndPassword(credentials.getEmail(), credentials.getPassword());

        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/createUser")
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@RequestBody User newUser){
        userService.createNewUser(newUser);
    }
}
