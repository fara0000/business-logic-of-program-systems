package backend.services;

import backend.dto.mappers.UserMapper;
import backend.dto.requests.LoginRequest;
import backend.dto.requests.UserDto;
import backend.dto.responses.*;
import backend.entities.Board;
import backend.entities.Pin;
import backend.models.Role;
import backend.entities.User;
import backend.exceptions.ApplicationException;
import backend.exceptions.ErrorEnum;
import backend.repositories.BoardRepository;
import backend.repositories.PinRepository;
import backend.repositories.UserRepository;
import backend.security.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
@AllArgsConstructor
public class UserService {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final PinRepository pinRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

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
        user.setRole(Role.USER);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        try {
            userRepository.save(user);
        } catch (Exception e) {
            log.error("Unexpected Error {}", e.getMessage());
            new ApplicationException(ErrorEnum.SERVICE_DATA_BASE_EXCEPTION.createApplicationError());
        }

        return true;
    }

    public LoginResponse login(LoginRequest loginRequest) throws ApplicationException {
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        if (!authentication.isAuthenticated()) {
            throw new ApplicationException(ErrorEnum.UNAUTHORIZED_EXCEPTION.createApplicationError());
        }

        User user = userRepository.findUserByEmail(loginRequest.getEmail());
        ErrorEnum.AUTH_LOGIN_ERROR.throwIfFalse(!ObjectUtils.isEmpty(user));
        ErrorEnum.AUTH_PASSWORD_ERROR.throwIfFalse(passwordEncoder.matches(loginRequest.getPassword(),
                user.getPassword()));
        LoginDto loginDto = userMapper.convertMemberToDto(user);
        String token = jwtUtil.generateToken(loginRequest.getEmail());
        LoginResponse loginResponse = new LoginResponse(token, loginDto);
        return loginResponse;
    }

    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAllUsers();

        List<UserResponse> usersList = new ArrayList<>();
        for(User user: users) {
            UserResponse userResponse = new UserResponse();
            userResponse.setId(user.getId());
            userResponse.setEmail(user.getEmail());
            userResponse.setAge(user.getAge());
            userResponse.setBoards(getUserBoards(user.getId()));
            usersList.add(userResponse);
        }

        return usersList;
    }

    private List<BoardResponseDto> getUserBoards(Long userId) {
        List<Board> boards = boardRepository.findAllByUser_Id(userId);

        List<BoardResponseDto> boardList = new ArrayList<>();
        for(Board board: boards) {
            BoardResponseDto boardResponseDto = new BoardResponseDto();
            boardResponseDto.setId(board.getId());
            boardResponseDto.setName(board.getName());
            boardResponseDto.setPins(getBoardPins(board.getId()));
            boardResponseDto.set_blocked(board.is_blocked());
            boardList.add(boardResponseDto);
        }

        return boardList;
    }

    public List<PinResponseDto> getBoardPins(Long boardId) {
        List<Pin> pins = pinRepository.findAllByBoard_Id(boardId);

        List<PinResponseDto> pinList = new ArrayList<>();
        for(Pin pin: pins) {
            PinResponseDto pinResponseDto = new PinResponseDto();
            pinResponseDto.setId(pin.getId());
            pinResponseDto.setName(pin.getName());
            pinResponseDto.setDescription(pin.getDescription());
            pinResponseDto.setAltText(pin.getAltText());
            pinResponseDto.setLink(pin.getLink());
            pinResponseDto.setOriginalFileName(pin.getPhoto().getOriginalFileName());
            pinResponseDto.set_blocked(pin.is_blocked());
            pinList.add(pinResponseDto);
        }

        return pinList;
    }
}
