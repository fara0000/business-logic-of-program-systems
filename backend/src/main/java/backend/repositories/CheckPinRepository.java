package backend.repositories;

import backend.entities.CheckPin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckPinRepository extends JpaRepository<CheckPin, Long> {
}
