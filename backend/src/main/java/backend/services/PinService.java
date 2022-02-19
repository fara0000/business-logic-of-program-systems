package backend.services;

import backend.dto.requests.PinRequest;
import backend.entities.Board;
import backend.entities.Pin;
import backend.repositories.BoardRepository;
import backend.repositories.PinRepository;
import backend.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
@AllArgsConstructor
public class PinService {
    private final PinRepository pinRepository;
    private final BoardRepository boardRepository;

    public void createPin(PinRequest pinRequest, MultipartFile photo) throws IOException {

        Pin pin = toPinEntity(pinRequest, photo);

        Board board = boardRepository.
                findBoardsByName(pinRequest.getNameOfBoard());

        pin.setBoard(board);
        //pin.setUser(user)
        //user.addPinToUser(pin);
        board.addPinToBoard(pin);

        pinRepository.save(pin);

        log.info("created new pin");

    }


    private Pin toPinEntity(PinRequest pinRequest, MultipartFile photo) throws IOException {
        Pin pin = new Pin();
        //      photo's service information
        pin.setOriginalFileName(photo.getOriginalFilename());
        pin.setSize(photo.getSize());
        pin.setContentType(photo.getContentType());
        pin.setBytes(photo.getBytes());
        //      pin's basic information
        pin.setName(pinRequest.getName());
        pin.setDescription(pinRequest.getDescription());
        pin.setAlt_text(pinRequest.getAlt_text());
        pin.setLink(pinRequest.getLink());

        return pin;
    }


}
