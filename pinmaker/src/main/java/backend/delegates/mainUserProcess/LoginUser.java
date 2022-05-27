package backend.delegates.mainUserProcess;

import backend.dto.requests.LoginRequest;
import backend.dto.responses.LoginResponse;
import backend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoginUser implements JavaDelegate {

    private final UserService userService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String email = (String) delegateExecution.getVariable("email");
        String password = (String) delegateExecution.getVariable("password");
        long userId = -1;

        try {
            LoginResponse loginResponse = userService.login(LoginRequest.builder().
                    email(email).
                    password(password).
                    build());
            userId = loginResponse.getUser().getId();
        } catch (Exception e){

            //TODO обработка исключений

        };

        delegateExecution.setVariable("userId", userId);
    }


}
