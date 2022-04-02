package backend.repositories;

import backend.entities.Board;
import backend.entities.Pin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PinRepository extends JpaRepository<Pin,Long> {
    List<Pin> findAllByBoard_Id(Long id);
}
