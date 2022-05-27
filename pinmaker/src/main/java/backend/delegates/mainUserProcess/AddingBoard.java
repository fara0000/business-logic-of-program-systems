package backend.delegates.mainUserProcess;

import backend.dto.requests.BoardRequest;
import backend.services.BoardService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddingBoard implements JavaDelegate {

    private final BoardService boardService;


    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String name = (String) delegateExecution.getVariable("nameOfBoard");
        long userId = (long) delegateExecution.getVariable("userId");

        Long res = boardService.createBoard(BoardRequest.builder()
                .name(name)
                .userId(userId)
                .build());

        delegateExecution.setVariable("boardId", res);

    }
}
