package backend.controllers;

import backend.dto.mappers.UserMapper;
import backend.dto.requests.LoginRequest;
import backend.dto.requests.UserDto;
import backend.dto.responses.LoginResponse;
import backend.repositories.UserRepository;
import backend.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    /**
     * register user
     */

    @RequestMapping(value = "/register", consumes = "application/json", produces = "application/json", method = {RequestMethod.OPTIONS, RequestMethod.POST})
    public ResponseEntity<String> register(@Valid @RequestBody UserDto user, BindingResult result) {
        log.debug(String.valueOf(user));
        try {
            log.info("POST request to register user {}", user);
            if (result.hasErrors()) {
                log.info("Validation Error");
                return new ResponseEntity<>("Validation Error", HttpStatus.BAD_REQUEST);
            }

            boolean isSaved = userService.addUser(user);
            System.out.println(isSaved);
            return isSaved ? new ResponseEntity<>("User registered successfully!", HttpStatus.OK) :
                    new ResponseEntity<>("User has already registered!", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.info("Unexpected Error {}", e.getMessage());
            return new ResponseEntity<>("Validation Error", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * login user
     */

    @RequestMapping(value = "/login", consumes = "application/json", method = {RequestMethod.OPTIONS, RequestMethod.POST})
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest, BindingResult bindingResult) {
        log.debug(String.valueOf(loginRequest));
        try {
            log.debug("POST request to login user {}", loginRequest);

            if (bindingResult.hasErrors()) {
                log.error("Validation error");
                return new ResponseEntity<>("Validation error", HttpStatus.BAD_REQUEST);
            }

            LoginResponse loginResponse = userService.login(loginRequest);
            return new ResponseEntity<>(loginResponse, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Unexpected error {}", e.getMessage());
            return new ResponseEntity<>("Invalid user credentials", HttpStatus.BAD_REQUEST);
        }
    }
}
