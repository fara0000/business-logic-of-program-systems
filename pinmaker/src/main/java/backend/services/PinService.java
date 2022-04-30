package backend.services;

import backend.dto.requests.PinRequest;
import backend.dto.responses.PinWithPhotoResponse;
import backend.entities.Board;
import backend.entities.Photo;
import backend.entities.Pin;
import backend.entities.User;
import backend.exceptions.ApplicationException;
import backend.exceptions.ErrorEnum;
import backend.repositories.BoardRepository;
import backend.repositories.PhotoRepository;
import backend.repositories.PinRepository;
import backend.repositories.UserRepository;
import backend.utils.PhotoUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class PinService {
    private final static String PATH_TO_PHOTO_BUFFER = "photoBuffer";
    private final static String PATH_TO_PERMANENT_STORAGE = "userPhotos";

    private final PinRepository pinRepository;
    private final PhotoRepository photoRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    private final UserService userService;
    private final AdminControlService adminControlService;

    private final PhotoUtil photoIO;

    private PlatformTransactionManager transactionManager;

    public void createPin(PinRequest pinRequest) throws Exception {

        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("pinTx");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);

        try {

            /**
             *  загружаем фотографию в постоянное хранилище и удаляем ее из буфера
             */

            byte[] photoByte = photoIO.getPhoto(PATH_TO_PHOTO_BUFFER, pinRequest.getFileName());
            photoIO.savePhoto(PATH_TO_PERMANENT_STORAGE, photoByte, pinRequest.getFileName());
            photoIO.deletePhoto(PATH_TO_PHOTO_BUFFER, pinRequest.getFileName());

            /**
             * загружаем информацию о фотографии в базу
             */

            Photo photo = toPhotoEntity(pinRequest.getFileName());

            try {
                photo = photoRepository.save(photo);
            } catch (Exception e) {
                log.error("Unexpected Error {}", e.getMessage());
                new ApplicationException(ErrorEnum.SERVICE_DATA_BASE_EXCEPTION.createApplicationError());
            }

            /**
             * загружаем пин в базу
             */

            Pin pin = toPinEntity(pinRequest, photo);

            Board board = boardRepository.
                    findBoardsByIdAndUser_Id(pinRequest.getBoard_id(), pinRequest.getUserId());

            User user = userRepository.findUserById(pinRequest.getUserId());

            pin.setBoard(board);
            pin.setUser(user);
            pin.setPhoto(photo);
            user.addPinToUser(pin);
            board.addPinToBoard(pin);

            try {
                pin = pinRepository.save(pin);
            } catch (Exception e) {
                log.error("Unexpected Error {}", e.getMessage());
                new ApplicationException(ErrorEnum.SERVICE_DATA_BASE_EXCEPTION.createApplicationError());
            }

            log.info("created new pin");

            /**
             * отправляем пин на проверку
             */

            adminControlService.sendPinToCheck(pin);

        } catch (Exception ex) {
            transactionManager.rollback(status);
            throw ex;
        }

        transactionManager.commit(status);
    }

    public List<PinWithPhotoResponse> findUserPins(Long id) {
        if (userService.findUser(id)) {
            return Collections.emptyList();
        }
        return pinToDTO(pinRepository.findAllByUser_Id(id));
    }


    public List<PinWithPhotoResponse> findBoardPins(Long id) {
        return pinToDTO(pinRepository.findAllByBoard_Id(id));
    }


    private Pin toPinEntity(PinRequest pinRequest, Photo photo) throws IOException {
        Pin pin = new Pin();
        pin.setName(pinRequest.getName());
        pin.setDescription(pinRequest.getDescription());
        pin.setAltText(pinRequest.getAlt_text());
        pin.setLink(pinRequest.getLink());
        pin.set_blocked(false);
        pin.setPhoto(photo);

        return pin;
    }

    private Photo toPhotoEntity(String file_name) {
        Photo photo = new Photo();
        photo.setOriginalFileName(file_name);
        return photo;
    }

    private List<PinWithPhotoResponse> pinToDTO(List<Pin> pins) {

        List<PinWithPhotoResponse> photos = new ArrayList<>();
        for (Pin pin : pins) {
            PinWithPhotoResponse photo = new PinWithPhotoResponse();
            photo.setId(pin.getId());
            photo.setName(pin.getName());
            photo.setDescription(pin.getDescription());
            photo.setAltText(pin.getAltText());
            photo.setLink(pin.getLink());
            photo.setPhoto(pin.getPhoto().getOriginalFileName());
            photos.add(photo);
        }
        return photos;
    }
}
