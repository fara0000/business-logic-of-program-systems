package backend.services;

import backend.dto.requests.PinRequest;
import backend.entities.Board;
import backend.entities.Pin;
import backend.entities.User;
import backend.exception.ServiceDataBaseException;
import backend.repositories.BoardRepository;
import backend.repositories.PinRepository;
import backend.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class PinService {
    private final PinRepository pinRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public void createPin(PinRequest pinRequest, String file_name, byte[] bytes) throws IOException, ServiceDataBaseException {

        Pin pin = toPinEntity(pinRequest, file_name, bytes);

        Board board = boardRepository.
                findBoardsByName(pinRequest.getNameOfBoard());
        User user = userRepository.findUserById(pinRequest.getUserId());

        pin.setBoard(board);
        pin.setUser(user);
        user.addPinToUser(pin);
        board.addPinToBoard(pin);

        try {
            pinRepository.save(pin);
        } catch (Exception e) {
            log.error("Unexpected Error {}", e.getMessage());
            new ServiceDataBaseException();
        }

        log.info("created new pin");

    }


    private Pin toPinEntity(PinRequest pinRequest, String file_name, byte[] bytes) throws IOException {
        Pin pin = new Pin();
        //      photo's service information
        pin.setOriginalFileName(file_name);
        pin.setBytes(bytes);
        //      pin's basic information
        pin.setName(pinRequest.getName());
        pin.setDescription(pinRequest.getDescription());
        pin.setAltText(pinRequest.getAlt_text());
        pin.setLink(pinRequest.getLink());

        return pin;
    }


}
