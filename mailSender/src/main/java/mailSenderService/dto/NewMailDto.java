package mailSenderService.dto;

import lombok.Data;

@Data
public class NewMailDto {
    String emailTo;
    String subject;
    String messageBody;
}