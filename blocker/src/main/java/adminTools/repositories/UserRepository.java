package adminTools.repositories;

import adminTools.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByEmail(String email);

    User findUserById(Long id);

    @Query("SELECT u FROM User u WHERE u.role = 0")
    List<User> findAllUsers();

}