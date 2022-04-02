package backend.repositories;
import backend.models.Role;
import backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByEmail(String email);

    User findUserById(Long id);

    default Role findRoleByEmail(String email) {
        User user = findUserByEmail(email);
        return user.getRole();
    }

    @Query("SELECT u FROM User u WHERE u.role = 0")
    List<User> findAllUsers();

}