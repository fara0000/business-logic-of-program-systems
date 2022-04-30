package mailSenderService.jms;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mailSenderService.dto.NewEmailDto;
import mailSenderService.services.EmailSenderService;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class JmsConsumer {
    private final EmailSenderService emailSenderService;

    @JmsListener(destination = "blockUserMail")
    public void getMessageForBlockingUser(String message) throws Exception {
        NewEmailDto newEmailDto = new NewEmailDto();
        newEmailDto.setEmailTo(message);
        newEmailDto.setSubject("Блокировка аккаунта на Pinterest");
        newEmailDto.setMessageBody("Ваш аккаунт был заблокирован!");

        emailSenderService.sendEmail(newEmailDto);
    }

    @JmsListener(destination = "blockBoardMail")
    public void getMessageForBlockingBoard(String message) throws Exception {
        NewEmailDto newEmailDto = new NewEmailDto();
        newEmailDto.setEmailTo(message);
        newEmailDto.setSubject("Блокировка доски на Pinterest");
        newEmailDto.setMessageBody("Ваша доска была заблокирован!");

        emailSenderService.sendEmail(newEmailDto);
    }

    @JmsListener(destination = "blockPinMail")
    public void getMessageForBlockingPin(String message) throws Exception {
        NewEmailDto newEmailDto = new NewEmailDto();
        newEmailDto.setEmailTo(message);
        newEmailDto.setSubject("Блокировка пина на Pinterest");
        newEmailDto.setMessageBody("Ваш пин был заблокирован!");

        emailSenderService.sendEmail(newEmailDto);
    }
}
