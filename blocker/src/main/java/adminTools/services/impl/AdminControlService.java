package adminTools.services.impl;

import adminTools.entities.*;
import adminTools.jms.JmsProducer;
import adminTools.repositories.*;
import adminTools.services.BlockingUtil;
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
public class AdminControlService implements BlockingUtil {
    private final String QUEUE_BLOCKED_USER_MAIL = "blockUserMail";
    private final String QUEUE_BLOCKED_BOARD_MAIL = "blockBoardMail";
    private final String QUEUE_BLOCKED_PIN_MAIL = "blockPinMail";

    private final CheckPinRepository checkPinRepository;
    private final CheckBoardRepository checkBoardRepository;
    private final PinRepository pinRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final JmsProducer jmsProducer;

    @Qualifier("transactionManager")
    private PlatformTransactionManager transactionManager;

    /**
     * Непосредственно блокируем в базе конкретный Pin
     * @param pinId - id пина, который нужно заблокировать
     */
    @Override
    public void blockUserPin(Long pinId) throws Exception {
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

            String email = pin.getUser().getEmail();
            jmsProducer.sendMessageToQueue(QUEUE_BLOCKED_PIN_MAIL, email);
        } catch (Exception ex) {
            transactionManager.rollback(status);
            throw ex;
        }

        transactionManager.commit(status);

    }

    /**
     * Непосредственно блокируем в базе конкретный Board и все Pins на ней
     * @param boardId - id доски, которую нужно заблокировать
     */
    @Override
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

            String email =  board.getUser().getEmail();
            jmsProducer.sendMessageToQueue(QUEUE_BLOCKED_BOARD_MAIL,email);

        } catch (Exception ex) {
            transactionManager.rollback(status);
            throw ex;
        }

        transactionManager.commit(status);

    }

    /**
     * Непосредственно блокируем в базе конкретного User, все его Boards и Pins на ней
     * @param userId - id пользователя, которого нужно заблокировать
     */
    @Override
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

            String email =  user.getEmail();
            jmsProducer.sendMessageToQueue(QUEUE_BLOCKED_USER_MAIL, email);

        } catch (Exception ex) {
            transactionManager.rollback(status);
            throw ex;
        }

        transactionManager.commit(status);


    }




}

