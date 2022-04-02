package backend.services;

import backend.entities.Board;
import backend.entities.CheckBoard;
import backend.entities.CheckPin;
import backend.entities.Pin;
import backend.exceptions.ApplicationException;
import backend.exceptions.ErrorEnum;
import backend.repositories.CheckBoardRepository;
import backend.repositories.CheckPinRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class AdminControlService {
    private final CheckPinRepository checkPinRepository;
    private final CheckBoardRepository checkBoardRepository;

    public void sendPinToCheck(Pin pin) {
        CheckPin checkPin = new CheckPin();
        checkPin.setPin(pin);
        checkPin.setCheck(false);
        try {
            checkPinRepository.save(checkPin);
        } catch (Exception e) {
            log.error("Unexpected Error {}", e.getMessage());
            new ApplicationException(ErrorEnum.SERVICE_DATA_BASE_EXCEPTION.createApplicationError());
        }
        log.info("pin sent to check");

    }

    public void sendBoardToCheck(Board board) {
        CheckBoard checkBoard = new CheckBoard();
        checkBoard.setBoard(board);
        checkBoard.setCheck(false);
        try {
            checkBoardRepository.save(checkBoard);
        } catch (Exception e) {
            log.error("Unexpected Error {}", e.getMessage());
            new ApplicationException(ErrorEnum.SERVICE_DATA_BASE_EXCEPTION.createApplicationError());
        }
        log.info("board sent to check");
    }


}
