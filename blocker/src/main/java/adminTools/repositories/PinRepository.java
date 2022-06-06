package adminTools.repositories;

import adminTools.entities.Pin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PinRepository extends JpaRepository<Pin, Long> {

    List<Pin> findAllByUser_Id(Long id);

    List<Pin> findAllByBoard_Id(Long id);

}
