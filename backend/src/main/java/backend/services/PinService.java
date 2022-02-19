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

import java.io.IOException;

@Slf4j
@Service
@AllArgsConstructor
public class PinService {
    private final PinRepository pinRepository;
    private final BoardRepository boardRepository;

    public void createPin(PinRequest pinRequest) throws IOException {
        if (pinRequest.getMultipartFile().getSize() != 0) {

            Pin pin = toPinEntity(pinRequest);

            Board board = boardRepository.
                    findBoardsByName(pinRequest.getNameOfBoard());

            pin.setBoard(board);
            //pin.setUser(user)
            //user.addPinToUser(pin);
            board.addPinToBoard(pin);

            pinRepository.save(pin);

            log.info("created new pin");

        } else {

        }
    }


    private Pin toPinEntity(PinRequest pinRequest) throws IOException {
        Pin pin = new Pin();
        //      photo's service information
        pin.setOriginalFileName(pinRequest.getMultipartFile().getName());
        pin.setSize(pinRequest.getMultipartFile().getSize());
        pin.setContentType(pinRequest.getMultipartFile().getContentType());
        pin.setBytes(pinRequest.getMultipartFile().getBytes());
        //      pin's basic information
        pin.setName(pinRequest.getName());
        pin.setDescription(pinRequest.getDescription());
        pin.setAlt_text(pinRequest.getAlt_text());
        pin.setLink(pinRequest.getLink());

        return pin;
    }


}
