package backend.controllers;


import backend.dto.requests.PinRequest;
import backend.dto.responses.PinWithPhotoResponse;
import backend.entities.Board;
import backend.entities.Pin;
import backend.services.BoardService;
import backend.services.PinService;
import backend.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.*;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequiredArgsConstructor
public class PinBuilderController {

    private final PinService pinService;

    private final BoardService boardService;

    private final UserService userService;

    private final static int MBYTE_20 = 20971520;

    private final static String PATH_TO_FILES = "userPhotos/";


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

        if (pin.getFileName() == null || pin.getFileName().trim().isEmpty() || findPhoto()) {
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

    /**
     * upload photo for pin
     */
    @RequestMapping(value = "/pin-builder/upload-photo", method = RequestMethod.POST, consumes = "multipart/form-data", produces = "multipart/form-data")
    public ResponseEntity<String> uploadPhoto(@RequestParam("file") MultipartFile file, Long userId) {
        if (file.isEmpty()) {
            log.info("Photo is 0 bite");
            return new ResponseEntity<>("Error: photo is 0 bite", HttpStatus.BAD_REQUEST);
        }
        if (file.getSize() > MBYTE_20) {
            log.info("Photo is so big. Size is " + file.getSize() + " bite");
            return new ResponseEntity<>("Error: photo is so big !", HttpStatus.BAD_REQUEST);
        }

        String file_name = generateFileName(file.getOriginalFilename(), file.getContentType());

        if (!putPhoto(file_name, file)) {
            log.error(" Error: photo wasn't upload");
            return new ResponseEntity<>("Error: photo wasn't upload", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        log.info("Photo has been uploaded");
        return new ResponseEntity<>(file_name, HttpStatus.CREATED);
    }


    private String generateFileName(String name, String type) {
        String[] ct = type.split("/");
        name = (name + Math.random()).replace(".", "");
        return new StringBuilder().append(name).append(".").append(ct[1]).toString();
    }

    public synchronized boolean putPhoto(String name, MultipartFile file) {
        boolean flag = false;
        try {
            byte[] bytes = file.getBytes();
            BufferedOutputStream stream =
                    new BufferedOutputStream(new FileOutputStream((PATH_TO_FILES + name)));
            stream.write(bytes);
            stream.close();
            flag = true;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return flag;
    }

    public synchronized byte[] getPhoto(String name) {
        byte[] buff = new byte[MBYTE_20];
        try {
            File file = new File(PATH_TO_FILES + name);
            BufferedInputStream stream = new BufferedInputStream(new FileInputStream(file));
            int bytes = stream.read(buff);
            while (bytes != -1) {
                bytes = stream.read(buff);
            }
            file.delete();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buff;

    }

    private boolean findPhoto() {
        return false;
    }

}