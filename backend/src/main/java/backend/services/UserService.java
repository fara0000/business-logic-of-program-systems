package backend.services;
import backend.dto.requests.LoginRequest;
import backend.dto.requests.UserDto;
import backend.entities.User;
import backend.repositories.UserRepository;
import backend.security.JwtUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
//    private final AuthenticationManager authenticationManager;

    public boolean checkUser(String email) {
        return userRepository.findUserByEmail(email) != null;
    }

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(email);

        if(email == null) {
            throw new UsernameNotFoundException("Member not found");
        }

        return user;
    }

    public boolean saveMember(UserDto userDto) {
        if (checkUser(userDto.getEmail())) {
            return false;
        }
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setAge(userDto.getAge());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);

        return true;
    }

//    public String getUserToken(@Valid LoginRequest user) {
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        System.out.println(authentication);
//        return jwtUtils.generateJwtToken(authentication);
//    }
}
