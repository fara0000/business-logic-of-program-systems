package backend.delegates.mainAdminProcess;

import backend.services.UserService;
import backend.services.adminService.impl.AdminControlService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BlockingUser implements JavaDelegate {

    private final UserService userService;
    private final AdminControlService adminService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        long userId = (long) delegateExecution.getVariable("userId");

        if (userService.findUser(userId))
            adminService.blockUser(userId);
        else
            throw new BpmnError("пользователя с userId = " + userId + " не существует");

    }
}
