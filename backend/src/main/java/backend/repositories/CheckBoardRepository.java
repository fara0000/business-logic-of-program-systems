package backend.repositories;

import backend.entities.CheckBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckBoardRepository extends JpaRepository<CheckBoard, Long> {
}
