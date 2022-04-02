package backend.repositories;

import backend.entities.Board;
import backend.entities.Pin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Board findBoardsByNameAndUser_Id(String name, Long userId);
}
