package backend.services;

import backend.entities.*;
import backend.exceptions.ApplicationException;
import backend.exceptions.ErrorEnum;
import backend.repositories.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class AdminControlService {
    private final CheckPinRepository checkPinRepository;
    private final CheckBoardRepository checkBoardRepository;
    private final PinRepository pinRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Qualifier("transactionManager")
    private PlatformTransactionManager transactionManager;


    public void blockUserPin(Long pinId) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("pinBlockTx");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            /**
             * меняем значение в pinCheck
             */
            CheckPin checkPin = checkPinRepository.findByPin_Id(pinId);
            checkPin.setCheck(true);
            checkPinRepository.save(checkPin);

            /**
             * блокируем пин
             */
            Pin pin = pinRepository.getById(pinId);
            pin.set_blocked(true);
            pinRepository.save(pin);

        } catch (Exception ex) {
            transactionManager.rollback(status);
            throw ex;
        }

        transactionManager.commit(status);

    }


    public void blockUserBoard(Long boardId) throws Exception {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("boardBlockTx");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            /**
             * меняем значение в boardCheck
             */
            CheckBoard checkBoard = checkBoardRepository.findByBoard_Id(boardId);
            checkBoard.setCheck(true);
            checkBoardRepository.save(checkBoard);

            /**
             * блокируем доску
             */

            Board board = boardRepository.findById(boardId).orElseThrow(Exception::new);
            board.set_blocked(true);
            boardRepository.save(board);

            /**
             * блокируем пины в доске
             */
            List<Pin> pins = pinRepository.findAllByBoard_Id(boardId);
            for (Pin pin : pins) {
                pin.set_blocked(true);
                pinRepository.save(pin);
            }

        } catch (Exception ex) {
            transactionManager.rollback(status);
            throw ex;
        }

        transactionManager.commit(status);
    }

    public void blockUser(Long userId) throws Exception {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("boardBlockTx");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            /**
             * блокируем пользователя
             */
            User user = userRepository.findById(userId).orElseThrow(Exception::new);
            user.set_blocked(true);
            userRepository.save(user);

            /**
             * блокируем доски пользователя
             */

            List<Board> boards = boardRepository.findAllByUser_Id(userId);
            for (Board board : boards) {
                board.set_blocked(true);
                boardRepository.save(board);
            }

            /**
             * блокируем пины пользователя
             */
            List<Pin> pins = pinRepository.findAllByUser_Id(userId);
            for (Pin pin : pins) {
                pin.set_blocked(true);
                pinRepository.save(pin);
            }

        } catch (Exception ex) {
            transactionManager.rollback(status);
            throw ex;
        }

        transactionManager.commit(status);
    }


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

