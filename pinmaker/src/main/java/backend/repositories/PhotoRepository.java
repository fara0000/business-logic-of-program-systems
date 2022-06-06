package backend.repositories;

import backend.entities.Photo;
import backend.entities.Pin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
}
