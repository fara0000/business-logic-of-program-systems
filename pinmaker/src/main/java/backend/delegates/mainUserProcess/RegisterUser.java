package backend.delegates.mainUserProcess;

import backend.dto.requests.UserDto;
import backend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegisterUser implements JavaDelegate {

    private final UserService userService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String email = (String) delegateExecution.getVariable("email");
        String password = (String) delegateExecution.getVariable("password");
        String age = (String) delegateExecution.getVariable("age");
        UserDto newUser = UserDto.builder()
                .email(email)
                .password(password)
                .age(age)
                .build();
         boolean flag = userService.addUser(newUser);

    }

}
