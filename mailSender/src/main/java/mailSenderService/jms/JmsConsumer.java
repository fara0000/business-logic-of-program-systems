package mailSenderService.jms;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mailSenderService.dto.NewMailDto;
import mailSenderService.services.MailSenderService;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class JmsConsumer {
    private final MailSenderService mailSenderService;

    @JmsListener(destination = "blockUserMail")
    public void getMessageForBlockingUser(String message) throws Exception {
        NewMailDto newMailDto = new NewMailDto();
        newMailDto.setEmailTo(message);
        newMailDto.setSubject("Блокировка аккаунта на Pinmaker.com");
        newMailDto.setMessageBody("Ваш аккаунт на Pinmaker.com был заблокирован" +
                " в связи с тем, что выши действия угражают сообществу Pinmaker.");

        log.info("Mail was sent to email " + message);
        mailSenderService.sendEmail(newMailDto);
    }

    @JmsListener(destination = "blockBoardMail")
    public void getMessageForBlockingBoard(String message) throws Exception {
        NewMailDto newMailDto = new NewMailDto();
        newMailDto.setEmailTo(message);
        newMailDto.setSubject("Блокировка вашей доски на Pinmaker.com");
        newMailDto.setMessageBody("Ваша доска была заблокирована на Pinmaker.com.");

        log.info("Mail was sent to email " + message);
        mailSenderService.sendEmail(newMailDto);
    }

    @JmsListener(destination = "blockPinMail")
    public void getMessageForBlockingPin(String message) throws Exception {
        NewMailDto newMailDto = new NewMailDto();
        newMailDto.setEmailTo(message);
        newMailDto.setSubject("Блокировка пина на Pinmaker.com");
        newMailDto.setMessageBody("Ваш пин был заблокиров на Pinmaker.com.");

        log.info("Mail was sent to email " + message);
        mailSenderService.sendEmail(newMailDto);
    }
}
