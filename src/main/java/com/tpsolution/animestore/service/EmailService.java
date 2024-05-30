package com.tpsolution.animestore.service;

import com.tpsolution.animestore.dto.EmailDetails;
import com.tpsolution.animestore.service.imp.EmailServiceImpl;

import java.io.File;

import com.tpsolution.animestore.utils.CommonUtils;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

// send email with gmail
@Service
public class EmailService implements EmailServiceImpl {

    private static final String TEMPLATE_NAME = "email_verification";
    private static final String SHOP_LOGO_IMAGE = "templates/images/logo_tpstore.png";
    private static final String MAIL_SUBJECT = "Registration Confirmation";
    @Autowired
    private TemplateEngine htmlTemplateEngine;

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Override
    public String sendEmailNoAttachment(EmailDetails details) {
        try {
            // Creating a simple mail message
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            // Setting up necessary details
            mailMessage.setFrom(sender);
            // send to multiple use
            // message.setTo(new String[] {"recipient1@example.com", "recipient2@example.com", "recipient3@example.com"});
            mailMessage.setTo(details.getRecipient());
            mailMessage.setText(details.getMsgBody());
            mailMessage.setSubject(details.getSubject());

            // Sending the mail
            javaMailSender.send(mailMessage);
            return "Mail Sent Successfully...";
        } catch (Exception e) {
            return "Error while Sending Mail";
        }
    }

    @Override
    public String sendEmailWithAttachment(EmailDetails details) {
        // Creating a mime message
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try {
            // Setting multipart as true for attachments to be send
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(details.getRecipient());
            mimeMessageHelper.setText(details.getMsgBody());
            mimeMessageHelper.setSubject(details.getSubject());

            // Adding the attachment
            FileSystemResource file = new FileSystemResource(new File(details.getAttachment()));

            mimeMessageHelper.addAttachment(file.getFilename(), file);

            // Sending the mail
            javaMailSender.send(mimeMessage);
            return "Mail sent Successfully";
        } catch (MessagingException e) {
            return "Error while sending mail!!!";
        }
    }

    public String sendEmailVerifyAccount(EmailDetails details) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper email = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        String confirmationUrl = "generated_confirmation_url";
        try {
            email.setFrom(sender);
            email.setTo(details.getRecipient());
            email.setSubject(MAIL_SUBJECT);

            Context ctx = new Context(LocaleContextHolder.getLocale());
            ctx.setVariable("email", details.getRecipient());
            ctx.setVariable("name", CommonUtils.extractUsernameFromEmail(details.getRecipient()));
            ctx.setVariable("shopLogo", SHOP_LOGO_IMAGE);
            ctx.setVariable("url", confirmationUrl);
            String htmlContent = htmlTemplateEngine.process(TEMPLATE_NAME, ctx);
            email.setText(htmlContent, true);

//            ClassPathResource clr = new ClassPathResource(SHOP_LOGO_IMAGE);
//            email.addInline("springLogo", clr, PNG_MIME);email.addInline("springLogo", clr, PNG_MIME);

            javaMailSender.send(mimeMessage);
            return "Mail sent Successfully";
        } catch (Exception e) {
            return "Error while Sending Mail";
        }
    }

}
