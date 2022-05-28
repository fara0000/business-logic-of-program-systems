package backend.delegates.mainAdminProcess;

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
public class LoginAdmin implements JavaDelegate {

    private final UserService userService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String email = (String) delegateExecution.getVariable("email");
        String password = (String) delegateExecution.getVariable("password");
        long adminId = -1;

        LoginResponse loginResponse = userService.login(LoginRequest.builder().
                email(email).
                password(password).
                build());
        adminId = loginResponse.getUser().getId();


        if (userService.getUserRole(adminId) == Role.USER) {
            delegateExecution.setVariable("authError", "User cannot start this process");
            throw new BpmnError("authorizationRoleFailed");
        }

        delegateExecution.setVariable("adminId", adminId);
    }
}
