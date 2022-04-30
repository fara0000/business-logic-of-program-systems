package mailSenderService.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mailSenderService.dto.NewEmailDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
@Slf4j

public class EmailSenderService {
    @Value("${spring.mail.username}")
    String username;
    @Value("${spring.mail.password}")
    String password;

    public void sendEmail(NewEmailDto newEmailDto) throws MessagingException {
        System.out.println(username);
        System.out.println(password);
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.auth", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.test-connection", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.debug", "true");

        Session session = Session.getInstance(props,
                //Аутентификатор - объект, который передает логин и пароль
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(newEmailDto.getEmailTo()));
        message.setSubject(newEmailDto.getSubject());
        message.setText(newEmailDto.getMessageBody());

        Transport.send(message);
    }
}
