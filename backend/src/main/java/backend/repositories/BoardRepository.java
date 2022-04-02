package backend.repositories;

import backend.entities.Board;
import backend.entities.Pin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Board findBoardsByIdAndUser_Id(Long id, Long user_id);

    Board findBoardsByNameAndUser_Id(String name, Long user_id);

    List<Board> findAllByUser_Id(Long id);


}
