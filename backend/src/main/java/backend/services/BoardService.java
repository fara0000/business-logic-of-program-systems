package backend.services;

import backend.dto.requests.BoardRequest;
import backend.entities.Board;
import backend.repositories.BoardRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@AllArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public void createBoard(BoardRequest boardRequest) {
        Board board = toBoardEntity(boardRequest);
        //user.addBoardToUser;
        boardRepository.save(board);
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
