package backend.controllers;

import backend.dto.requests.RegistrationRequest;
import backend.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@Slf4j
@Controller
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/register", consumes = "application/json", produces = "application/json", method = {RequestMethod.OPTIONS, RequestMethod.POST})
    public ResponseEntity<String> register(@Valid @RequestBody RegistrationRequest user, BindingResult result) {
        try {
            log.info("POST request to register user {}", user);
            if (result.hasErrors()) {
                log.info("Validation Error");
                return new ResponseEntity<>("Validation Error", HttpStatus.BAD_REQUEST);
            }

            boolean isSaved = userService.saveMember();
            System.out.println(isSaved);
            return isSaved ? new ResponseEntity<>("User registered successfully!", HttpStatus.OK) :
                    new ResponseEntity<>("User has already registered!", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.info("Unexpected Error {}", e.getMessage());
            return new ResponseEntity<>("Validation Error", HttpStatus.BAD_REQUEST);
        }
    }
}
