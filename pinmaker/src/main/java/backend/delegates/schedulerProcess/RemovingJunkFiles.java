package backend.delegates.schedulerProcess;

import backend.utils.PhotoUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class RemovingJunkFiles implements JavaDelegate {
    private final static String PATH_TO_PHOTO_BUFFER = "photoBuffer";

    private final PhotoUtil photoUtil;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("Camunda-scheduler started his work");
        photoUtil.clearBuffer(PATH_TO_PHOTO_BUFFER);
        log.info("Camunda-scheduler finished his work");
    }
}
