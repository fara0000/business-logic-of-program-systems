package backend.controllers;

import backend.utils.PhotoUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PhotoController {

    private final PhotoUtil photoUtil;

    private final static int MBYTE_20 = 20971520;

    private final static String PATH_TO_PHOTO_BUFFER = "photoBuffer";

    /**
     * upload photo for pin - write photo to buffer
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

        photoUtil.putPhoto(PATH_TO_PHOTO_BUFFER, file_name, file);

        log.info("Photo has been uploaded");
        return new ResponseEntity<>(file_name, HttpStatus.CREATED);
    }


    private String generateFileName(String name, String type) {
        String[] ct = type.split("/");
        name = (name + Math.random()).replace(".", "");
        return new StringBuilder().append(name).append(".").append(ct[1]).toString();
    }


}
