package backend.controllers;

import backend.dto.responses.UserResponse;
import backend.services.adminService.impl.AdminControlService;
import backend.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final AdminControlService adminService;

    /**
     * get all users and their pins and boards
     */

    @GetMapping(value = "/users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> userList = userService.getAllUsers();
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }


    /**
     * block user - it will block all user's pins and boards
     */

    @RequestMapping(value = "/block-user", method = RequestMethod.PUT)
    public ResponseEntity<String> blockUser(@Valid @RequestBody Long userId) throws Exception {
        adminService.blockUser(userId);
        return new ResponseEntity<>("User " + userId + " is blocked", HttpStatus.OK);
    }

    /**
     * block user's pin
     */

    @RequestMapping(value = "/block-pin", method = RequestMethod.PUT)
    public ResponseEntity<String> blockUserPin(@Valid @RequestBody Long pinId) throws Exception {
        adminService.blockUserPin(pinId);
        return new ResponseEntity<>("Pin " + pinId + " is blocked", HttpStatus.OK);
    }

    /**
     * block user's board - it will block all user's pins on this board
     */

    @RequestMapping(value = "/block-board", method = RequestMethod.PUT)
    public ResponseEntity<String> blockUserBoard(@Valid @RequestBody Long boardId) throws Exception {
        adminService.blockUserBoard(boardId);
        return new ResponseEntity<>("Board " + boardId + " is blocked", HttpStatus.OK);
    }


}
