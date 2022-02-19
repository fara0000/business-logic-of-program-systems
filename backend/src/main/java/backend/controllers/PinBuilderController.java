package backend.controllers;

import backend.dto.requests.BoardRequest;
import backend.dto.requests.PinRequest;
import backend.dto.requests.UploadPhotoRequest;
import backend.entities.Board;
import backend.entities.User;
import backend.repositories.BoardRepository;
import backend.services.BoardService;
import backend.services.PinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequiredArgsConstructor
public class PinBuilderController {

    private final PinService pinService;

    private final BoardService boardService;

    private final BoardRepository boardRepository;

    private final static int MBYTE_20 = 20971520;

    private static Map<Long, MultipartFile> photoMap = new HashMap<>();

    /**
     * PIN
     */

    /**
     * creating Pin
     */

    @RequestMapping(value = "/pin-builder", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> makePin(@RequestBody PinRequest pin) throws IOException {
        log.info("POST request to create new pin {}", pin);

        if (pin.getNameOfBoard().isEmpty() || pin.getNameOfBoard() == null) {
            log.info("Validation Error - no board");
            return new ResponseEntity<>
                    ("The board must be specified, " +
                            "if there are no boards, create a new one.", HttpStatus.BAD_REQUEST);
        }

        if (boardService.findBoard(pin.getNameOfBoard())) {
            log.info("Selected board does not exist.");
            return new ResponseEntity<>
                    ("Selected board does not exist", HttpStatus.BAD_REQUEST);
        }

        if (photoMap.containsKey(pin.getUserId()) ) {
            log.info("Photo is not uploaded" + photoMap.get(Long.getLong(String.valueOf(pin.getUserId()))));
            return new ResponseEntity<>
                    ("Photo is not uploaded, please, upload photo ", HttpStatus.BAD_REQUEST);
        }

        pinService.createPin(pin, photoMap.get(Long.getLong(String.valueOf(pin.getUserId()))));
        log.info("Pin {} was successfully created!", pin);
        return new ResponseEntity<>("Pin was created", HttpStatus.CREATED);
    }

    /**
     * upload photo for pin
     */
    @RequestMapping(value = "/pin-builder/upload-photo", method = RequestMethod.POST, consumes = "multipart/form-data", produces = "multipart/form-data")
    public ResponseEntity<String> uploadPhoto(@RequestParam("file") MultipartFile multipartFile, Long userId) throws IOException {
        if (multipartFile.getSize() == 0) {
            log.info("Photo is 0 bite");
            return new ResponseEntity<>("Error: photo is 0 bite", HttpStatus.BAD_REQUEST);
        }
        if (multipartFile.getSize() > MBYTE_20) {
            log.info("Photo is so big. Size is " + multipartFile.getSize());
            return new ResponseEntity<>("Error: photo is so big !", HttpStatus.BAD_REQUEST);
        }
        photoMap.put(userId, multipartFile);
        log.info("Photo has been uploaded " + photoMap.get(userId));
        return new ResponseEntity<>("Photo has been uploaded", HttpStatus.CREATED);

    }

    /**
     * BOARD - pins are stored on boards
     */

    /**
     * finding all boards
     */

    @RequestMapping(value = "/pin-builder/find-boards", method = RequestMethod.GET, consumes = "application/json", produces = "application/json")
    public List<Board> getAllBoards() {
        return boardRepository.findAll();
    }

    /**
     * creating boards for pin
     */

    @RequestMapping(value = "/pin-builder/create-board", method = {RequestMethod.OPTIONS, RequestMethod.POST})
    public ResponseEntity<String> makeBoard(@Valid @RequestBody BoardRequest board, BindingResult result) {
        log.info("POST request to create new board {}", board);

        if (result.hasErrors()) {
            log.info("Validation Error");
            return new ResponseEntity<>("Board's name cannot be empty", HttpStatus.BAD_REQUEST);
        }

        if (!boardService.findBoard(board.getName())) {
            log.info("Board name is not unique");
            return new ResponseEntity<>("Board name must be unique", HttpStatus.BAD_REQUEST);
        }
        boardService.createBoard(board);
        log.info("Board {} was successfully created!", board);
        return new ResponseEntity<>("Board was created", HttpStatus.CREATED);
    }


}
