package backend.delegates.mainUserProcess;

import backend.dto.requests.LoginRequest;
import backend.dto.responses.LoginResponse;
import backend.models.Role;
import backend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.BpmnError;
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

        LoginResponse loginResponse = userService.login(LoginRequest.builder().
                email(email).
                password(password).
                build());
        userId = loginResponse.getUser().getId();

        if (userService.getUserRole(userId) == Role.ADMIN) {
            delegateExecution.setVariable("authError", "Admin cannot start this process");
            throw new BpmnError("authorizationRoleFailed");
        }

        delegateExecution.setVariable("userId", userId);
    }


}
