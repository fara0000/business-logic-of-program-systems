package backend.delegates.mainUserProcess;

import backend.services.BoardService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FindingBoard implements JavaDelegate {

    private final BoardService boardService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        long userId = (long) delegateExecution.getVariable("userId");
        List<String> boards = boardService.findAllUserBoards(userId);
        delegateExecution.setVariable("boards", boards);
        delegateExecution.setVariable("countBoards", boards.size());
    }

}
