package backend.services;

import backend.dto.mappers.UserMapper;
import backend.dto.requests.LoginRequest;
import backend.dto.requests.UserDto;
import backend.dto.responses.LoginDto;
import backend.dto.responses.LoginResponse;
import backend.entities.User;
import backend.exceptions.ServiceDataBaseException;
import backend.exceptions.ErrorEnum;
import backend.repositories.UserRepository;
import backend.security.JwtUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;
//    private final AuthenticationManager authenticationManager;

    public boolean checkUser(String email) {
        return userRepository.findUserByEmail(email) != null;
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

        try {
            userRepository.save(user);
        } catch (Exception e) {
            log.error("Unexpected Error {}", e.getMessage());
            new ServiceDataBaseException();
        }

        return true;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        log.debug(String.valueOf(loginRequest));
        User user = userRepository.findUserByEmail(loginRequest.getEmail());
        ErrorEnum.AUTH_LOGIN_ERROR.throwIfFalse(!ObjectUtils.isEmpty(user));
        ErrorEnum.AUTH_PASSWORD_ERROR.throwIfFalse(passwordEncoder.matches(loginRequest.getPassword(),
                user.getPassword()));
        LoginDto loginDto = userMapper.convertMemberToDto(user);
        String token = jwtUtils.generateToken(loginRequest.getEmail());
        LoginResponse loginResponse = new LoginResponse(token, loginDto);
        return loginResponse;
    };


}
