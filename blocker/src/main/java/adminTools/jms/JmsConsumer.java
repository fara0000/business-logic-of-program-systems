package adminTools.jms;

import adminTools.services.AdminControlService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class JmsConsumer {

    private final AdminControlService adminControlService;

    @JmsListener(destination = "blockUser")
    public void getMessageForBlockingUser(String message) throws Exception {
        adminControlService.blockUser(Long.valueOf(message));
    }

    @JmsListener(destination = "blockBoard")
    public void getMessageForBlockingBoard(String message) throws Exception {
        adminControlService.blockUserBoard(Long.valueOf(message));

    }

    @JmsListener(destination = "blockPin")
    public void getMessageForBlockingPin(String message) throws Exception {
        adminControlService.blockUserPin(Long.valueOf(message));
    }

}
