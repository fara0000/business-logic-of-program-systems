package backend.controllers;

import backend.dto.requests.BoardRequest;
import backend.dto.requests.PinRequest;
import backend.entities.Board;
import backend.repositories.BoardRepository;
import backend.services.BoardService;
import backend.services.PinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;


@Slf4j
@Controller
@RequiredArgsConstructor
public class PinBuilderController {
    private final PinService pinService;

    private final BoardService boardService;

    private final BoardRepository boardRepository;


    /**
     * PIN
     */

    /**
     * creating Pin
     */
    @RequestMapping(value = "/pin-builder", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> makePin(@RequestParam PinRequest pin) {
        try {
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

            pinService.createPin(pin);
            log.info("Pin {} was successfully created!", pin);
            return new ResponseEntity<>("Pin was created", HttpStatus.CREATED);

        } catch (Exception e) {
            log.info("Unexpected Error {}", e.getMessage());
            return new ResponseEntity<>("Validation Error", HttpStatus.BAD_REQUEST);
        }

    }

    /**
     * BOARD - pins are stored on boards
     */

    /**
     * finding all boards
     */

    @RequestMapping(value = "/pin-builder", method = RequestMethod.GET, consumes = "application/json", produces = "application/json")
    public List<Board> getAllBoards() {
        return boardRepository.findAll();
    }

    /**
     * creating boards for pin
     */

    @RequestMapping(value = "/pin-builder", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> makeBoard(@Valid @RequestParam BoardRequest board, BindingResult result) {
        try {
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
            return new ResponseEntity<String>("Board was created", HttpStatus.CREATED);
        } catch (Exception e) {
            log.info("Unexpected Error {}", e.getMessage());
            return new ResponseEntity<>("Validation Error", HttpStatus.BAD_REQUEST);
        }

    }
}
