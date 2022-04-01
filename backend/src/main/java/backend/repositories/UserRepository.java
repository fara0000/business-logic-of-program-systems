package backend.repositories;
import backend.entities.Role;
import backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByEmail(String email);

    User findUserById(Long id);

    default Role findRoleByEmail(String email) {
        User user = findUserByEmail(email);
        return user.getRole();
    }

}