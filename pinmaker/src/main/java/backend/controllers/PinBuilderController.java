package backend.controllers;


import backend.dto.requests.PinRequest;
import backend.dto.responses.PinWithPhotoResponse;
import backend.services.BoardService;
import backend.services.PinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
public class PinBuilderController {

    private final PinService pinService;

    private final BoardService boardService;


    /**
     * PIN
     */

    /**
     * creating Pin
     */

    @RequestMapping(value = "/pin-builder", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> makePin(@RequestBody PinRequest pin) throws Exception {
        log.info("POST request to create new pin {}", pin);

        if (pin.getBoard_id() == null) {
            log.info("Validation Error - no board");
            return new ResponseEntity<>
                    ("The board must be specified, " +
                            "if there are no boards, create a new one.", HttpStatus.BAD_REQUEST);
        }

        if (boardService.findBoardById(pin.getBoard_id(), pin.getUserId())) {
            log.info("Selected board does not exist.");
            return new ResponseEntity<>
                    ("Selected board does not exist", HttpStatus.BAD_REQUEST);
        }

        if (pin.getFileName() == null || pin.getFileName().trim().isEmpty() ) {
            log.info("Photo is not uploaded");
            return new ResponseEntity<>
                    ("Photo is not uploaded, please, upload photo ", HttpStatus.BAD_REQUEST);
        }

        pinService.createPin(pin);

        log.info("Pin {} was successfully created!", pin);
        return new ResponseEntity<>("Pin was created", HttpStatus.CREATED);
    }


    /**
     * finding all user's pin
     */
    @RequestMapping(value = "/pin-builder/find-user-pin", method = RequestMethod.GET)
    public List<PinWithPhotoResponse> getAllUserPin(@RequestParam Long userID) {
        return pinService.findUserPins(userID);
    }

    /**
     * get all pins of one board
     */
    @RequestMapping(value = "/pin-builder/find-board-pin", method = RequestMethod.GET)
    public List<PinWithPhotoResponse> getAllBoardPin(@RequestParam Long boardId) {
        return pinService.findBoardPins(boardId);
    }


}