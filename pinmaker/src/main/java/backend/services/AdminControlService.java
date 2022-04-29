package backend.services;

import backend.entities.*;
import backend.exceptions.ApplicationException;
import backend.exceptions.ErrorEnum;
import backend.jms.JmsProducer;
import backend.repositories.CheckBoardRepository;
import backend.repositories.CheckPinRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@AllArgsConstructor
public class AdminControlService implements AdminControl {

    private final String QUEUE_USER_NAME = "blockUser";
    private final String QUEUE_BOARD_NAME = "blockBoard";
    private final String QUEUE_PIN_NAME = "blockPin";

    private final CheckPinRepository checkPinRepository;
    private final CheckBoardRepository checkBoardRepository;
    private final JmsProducer jmsProducer;

    @Override
    public void blockUserPin(Long pinId) {
        jmsProducer.sendMessageToQueue(QUEUE_PIN_NAME, pinId.toString());
    }

    @Override
    public void blockUserBoard(Long boardId) {
        jmsProducer.sendMessageToQueue(QUEUE_BOARD_NAME, boardId.toString());
    }

    @Override
    public void blockUser(Long userId) {
        jmsProducer.sendMessageToQueue(QUEUE_USER_NAME, userId.toString());
    }

    @Override
    public void sendPinToCheck(Pin pin) {
        CheckPin checkPin = new CheckPin();
        checkPin.setPin(pin);
        checkPin.setCheck(false);
        try {
            checkPinRepository.save(checkPin);
        } catch (Exception e) {
            log.error("Unexpected Error {}", e.getMessage());
            throw new ApplicationException(ErrorEnum.SERVICE_DATA_BASE_EXCEPTION.createApplicationError());
        }
        log.info("pin sent to check");

    }

    @Override
    public void sendBoardToCheck(Board board) {
        CheckBoard checkBoard = new CheckBoard();
        checkBoard.setBoard(board);
        checkBoard.setCheck(false);
        try {
            checkBoardRepository.save(checkBoard);
        } catch (Exception e) {
            log.error("Unexpected Error {}", e.getMessage());
            throw new ApplicationException(ErrorEnum.SERVICE_DATA_BASE_EXCEPTION.createApplicationError());
        }
        log.info("board sent to check");
    }
}

