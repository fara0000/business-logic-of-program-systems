package adminTools.repositories;

import adminTools.entities.CheckBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckBoardRepository extends JpaRepository<CheckBoard, Long> {

    CheckBoard findByBoard_Id(Long id);

}
