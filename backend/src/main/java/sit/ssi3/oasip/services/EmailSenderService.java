package sit.ssi3.oasip.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {
    private final JavaMailSender javaMailSender;

    public EmailSenderService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail (String toMail , String subject , String body) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("bam.infinite@gmail.com");
        msg.setTo(toMail);
        msg.setSubject(subject);
        msg.setText(body);

        javaMailSender.send(msg);
    }
}