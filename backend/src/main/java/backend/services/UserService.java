package backend.services;
import backend.dto.requests.RegistrationRequest;
import backend.entities.User;
import backend.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public boolean checkUser(String email) {
        return userRepository.findUserByEmail(email) != null;
    }

    public boolean saveMember(RegistrationRequest registrationRequest) {
        User user = new User();
        user.setEmail(registrationRequest.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setAge(registrationRequest.getAge());
        if (checkUser(user.getEmail())) {
            return false;
        }
        userRepository.save(user);

        return true;
    }
}
