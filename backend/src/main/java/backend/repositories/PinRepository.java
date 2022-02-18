package backend.repositories;

import backend.entities.Pin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PinRepository extends JpaRepository<Pin,Long> {
}
