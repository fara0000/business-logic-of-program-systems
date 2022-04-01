package backend.controllers;


import backend.dto.requests.PinRequest;
import backend.services.BoardService;
import backend.services.PinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.*;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@RestController
@RequiredArgsConstructor
public class PinBuilderController {

    private final PinService pinService;

    private final BoardService boardService;

    private final static int MBYTE_20 = 20971520;

    private static Map<Long, String> photoMap = new HashMap<>();

    /**
     * PIN
     */

    /**
     * creating Pin
     */

//    @Transactional
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

        if (photoMap.containsKey(pin.getUserId()) || photoMap.get(pin.getUserId()).isEmpty()) {
            log.info("Photo is not uploaded" + photoMap.get(Long.getLong(pin.getUserId().toString())));
            return new ResponseEntity<>
                    ("Photo is not uploaded, please, upload photo ", HttpStatus.BAD_REQUEST);
        }

        String file_name = photoMap.get(Long.getLong(pin.getUserId().toString()));
        byte[] bytes;

        try {
            bytes = getPhoto(file_name);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: there is some problem with your photo", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        pinService.createPin(pin, file_name, bytes);
        photoMap.put(pin.getUserId(), "");
        log.info("Pin {} was successfully created!", pin);
        return new ResponseEntity<>("Pin was created", HttpStatus.CREATED);
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

        photoMap.put(userId, file_name);
        log.info("Photo has been uploaded");
        return new ResponseEntity<>("Photo has been uploaded", HttpStatus.CREATED);
    }


    private String generateFileName(String name, String type) {
        String[] ct = type.split("/");
        name = (name + Math.random()).replace(".", "");
        return new StringBuilder().append(name).append(".").append(ct[1]).toString();
    }

    private synchronized boolean putPhoto(String name, MultipartFile file) {
        boolean flag = false;
        try {
            byte[] bytes = file.getBytes();
            BufferedOutputStream stream =
                    new BufferedOutputStream(new FileOutputStream(("user_photos/" + name)));
            stream.write(bytes);
            stream.close();
            flag = true;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return flag;
    }

    private synchronized byte[] getPhoto(String name) {
        byte[] buff = new byte[MBYTE_20];
        try {
            File file = new File("user_photos/" + name);
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
}