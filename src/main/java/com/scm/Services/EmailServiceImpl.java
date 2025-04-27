package com.scm.Services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class EmailServiceImpl implements EmailService{



    private final JavaMailSender mailSender ;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Value("${spring.mail.properties.mail.domain_name}")
    private String domain_name ;


    @Override
    public void sendEmail(String to, String subject, String htmlContent){

        // simple mail send without html classs

//        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
//        simpleMailMessage.setTo(to);
//        simpleMailMessage.setSubject(subject);
//        simpleMailMessage.setText(body);
//        simpleMailMessage.setFrom(domain_name);
//        mailSender.send(simpleMailMessage);

        MimeMessage mailMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true,"UTF-8");
            helper.setTo(to);
            helper.setFrom(this.domain_name);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            mailSender.send(mailMessage);
        } catch (MessagingException e) {
            log.error(" Error : {}", e.toString());
        }
    }

    @Override
    public void sendEmailForContactUs(String From, String subject, String htmlContent) {
        MimeMessage mailMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true,"UTF-8");
            helper.setTo(this.domain_name);
            helper.setFrom(From);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            mailSender.send(mailMessage);
        } catch (MessagingException e) {
            log.error("Error : {}", e.toString());
        }
    }
}
