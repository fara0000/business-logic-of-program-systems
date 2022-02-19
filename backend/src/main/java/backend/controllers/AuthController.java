package backend.controllers;

import backend.dto.mappers.UserMapper;
import backend.dto.requests.LoginRequest;
import backend.dto.requests.UserDto;
import backend.dto.responses.LoginDto;
import backend.dto.responses.LoginResponse;
import backend.entities.User;
import backend.repositories.UserRepository;
import backend.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@Slf4j
@RestController
public class AuthController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public AuthController(UserService userService, UserMapper userMapper, UserRepository userRepository) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/register", consumes = "application/json", produces = "application/json", method = {RequestMethod.OPTIONS, RequestMethod.POST})
    public ResponseEntity<String> register(@Valid @RequestBody UserDto user, BindingResult result) {
        log.debug(String.valueOf(user));
        try {
            log.info("POST request to register user {}", user);
            if (result.hasErrors()) {
                log.info("Validation Error");
                return new ResponseEntity<>("Validation Error", HttpStatus.BAD_REQUEST);
            }

            boolean isSaved = userService.saveMember(user);
            System.out.println(isSaved);
            return isSaved ? new ResponseEntity<>("User registered successfully!", HttpStatus.OK) :
                    new ResponseEntity<>("User has already registered!", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.info("Unexpected Error {}", e.getMessage());
            return new ResponseEntity<>("Validation Error", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/login", consumes = "application/json", method = {RequestMethod.OPTIONS, RequestMethod.POST})
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest, BindingResult bindingResult) {
        log.debug(String.valueOf(loginRequest));
        try {
            log.debug("POST request to login user {}", loginRequest);

            if(bindingResult.hasErrors()) {
                log.error("Validation error");
                return new ResponseEntity<>("Ошибка валидации", HttpStatus.BAD_REQUEST);
            }

            User user = userRepository.findUserByEmail(loginRequest.getEmail());
            LoginDto loginDto = userMapper.convertMemberToDto(user);
            LoginResponse loginResponse = new LoginResponse("Bearer kjfdskljfklsfjksfjksfjkfkjsdkf", loginDto);
            return new ResponseEntity<>(loginResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Unexpected error {}", e.getMessage());
            return new ResponseEntity<>("Неверные учетные данные пользователя", HttpStatus.BAD_REQUEST);
        }
    }
}
