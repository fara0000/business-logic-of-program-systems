package backend.repositories;

import backend.entities.Pin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface PinRepository extends JpaRepository<Pin, Long> {

    List<Pin> findAllByUser_Id(Long id);

    Pin findPinById(Long id);

    List<Pin> findAllByBoard_Id(Long id);

}
