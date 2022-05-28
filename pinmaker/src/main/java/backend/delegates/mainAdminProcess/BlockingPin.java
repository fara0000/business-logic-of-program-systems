package backend.delegates.mainAdminProcess;

import backend.services.PinService;
import backend.services.UserService;
import backend.services.adminService.impl.AdminControlService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BlockingPin implements JavaDelegate {
    private final PinService pinService;
    private final AdminControlService adminService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        long pinId = (long) delegateExecution.getVariable("pinId");

        if (pinService.findPin(pinId))
            adminService.blockUserPin(pinId);
        else
            throw new Exception("пина с pinId = " + pinId + " не существует");

    }
}
