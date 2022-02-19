package backend.services;

import backend.dto.requests.BoardRequest;
import backend.entities.Board;
import backend.entities.User;
import backend.exception.ServiceDataBaseException;
import backend.repositories.BoardRepository;
import backend.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@AllArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public void createBoard(BoardRequest boardRequest) throws ServiceDataBaseException {

        Board board = toBoardEntity(boardRequest);
        User user = userRepository.findUserById(boardRequest.getUserId());
        user.addBoardToUser(board);
        try {
            boardRepository.save(board);
        } catch (Exception e) {
            log.error("Unexpected Error {}", e.getMessage());
            new ServiceDataBaseException(e.getMessage());
        }

        log.info("create new board");


    }

    private Board toBoardEntity(BoardRequest boardRequest) {
        Board board = new Board();
        board.setName(boardRequest.getName());
        board.setPublic(boardRequest.isPublic());
        return board;
    }

    public boolean findBoard(String name) {
        return boardRepository.findBoardsByName(name) == null;
    }

}
