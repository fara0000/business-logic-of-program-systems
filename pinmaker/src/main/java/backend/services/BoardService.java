package backend.services;

import backend.dto.requests.BoardRequest;
import backend.entities.Board;
import backend.entities.User;
import backend.exceptions.ApplicationException;
import backend.exceptions.ErrorEnum;
import backend.repositories.BoardRepository;
import backend.repositories.UserRepository;
import backend.services.adminService.impl.AdminControlService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
@AllArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    private final AdminControlService adminControlService;

    private PlatformTransactionManager transactionManager;

    public Long createBoard(BoardRequest boardRequest) throws Exception {
        Long result;
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("boardTx");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);

        try {

            /**
             * сохраняем доску в базу
             */

            Board board = toBoardEntity(boardRequest);
            User user = userRepository.findUserById(boardRequest.getUserId());
            user.addBoardToUser(board);
            try {
                board = boardRepository.save(board);
            } catch (Exception e) {
                log.error("Unexpected Error {}", e.getMessage());
                new ApplicationException(ErrorEnum.SERVICE_DATA_BASE_EXCEPTION.createApplicationError());
            }

            result = board.getId();

            log.info("create new board");

            /**
             * отправляем доску на проверку
             */

            adminControlService.sendBoardToCheck(board);

        } catch (Exception ex) {
            transactionManager.rollback(status);
            throw ex;
        }

        transactionManager.commit(status);

        return result;
    }

    private Board toBoardEntity(BoardRequest boardRequest) {
        Board board = new Board();
        board.setName(boardRequest.getName());
        board.set_blocked(false);
        return board;
    }

    public boolean findBoard(String name, Long userId) {
        return boardRepository.findBoardsByNameAndUser_Id(name, userId) == null;
    }

    public boolean findBoardById(Long id, Long userId) {
        return boardRepository.findBoardsByIdAndUser_Id(id, userId) == null;
    }

    public List<String> findAllUserBoards(Long userId) {
        List<Board> boards = boardRepository.findAllByUser_Id(userId);
        List<String> names = new ArrayList<>();
        for (Board board : boards) {
            names.add(board.getName());
        }
        return names;
    }
}
