package backend.delegates.mainUserProcess;

import backend.dto.requests.PinRequest;
import backend.services.PinService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddingPin implements JavaDelegate {

    private final PinService pinService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String name = (String) delegateExecution.getVariable("nameOfPin");
        Long boardId = (long) delegateExecution.getVariable("boardId");
        String description = (String) delegateExecution.getVariable("description");
        String altText = (String) delegateExecution.getVariable("altText");
        String link = (String) delegateExecution.getVariable("link");
        Long userId = (long) delegateExecution.getVariable("userId");
        String fileName = (String) delegateExecution.getVariable("fileName");

        pinService.createPin(PinRequest.builder()
                .name(name)
                .board_id(boardId)
                .description(description)
                .alt_text(altText)
                .link(link)
                .userId(userId)
                .fileName(fileName)
                .build());
    }

}

