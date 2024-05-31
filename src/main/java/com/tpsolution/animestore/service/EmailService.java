package com.tpsolution.animestore.service;

import com.tpsolution.animestore.constant.StringConstant;
import com.tpsolution.animestore.dto.EmailDetails;
import com.tpsolution.animestore.service.imp.EmailServiceImpl;
import com.tpsolution.animestore.utils.CommonUtils;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;

// send email with gmail
@Service
public class EmailService implements EmailServiceImpl {

    private static final Logger logger = LogManager.getLogger(EmailService.class);

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
        logger.info("#sendEmailVerifyAccount email: {}", details.getRecipient());

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper email = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            String confirmationUrl = "http://localhost:8080/client/generated_confirmation_url";

            email.setFrom(sender);
            email.setTo(details.getRecipient());
            email.setSubject(StringConstant.TemplateEmail.MAIL_SUBJECT_VERIFY_ACCOUNT);

            Context ctx = new Context(LocaleContextHolder.getLocale());
            ctx.setVariable("email", details.getRecipient());
            ctx.setVariable("name", CommonUtils.extractUsernameFromEmail(details.getRecipient()));
            ctx.setVariable("shopLogo", StringConstant.TemplateEmail.SHOP_LOGO_IMAGE);
            ctx.setVariable("url", confirmationUrl);
            String htmlContent = htmlTemplateEngine.process(StringConstant.TemplateEmail.TEMPLATE_EMAIL_VERIFY_ACCOUNT, ctx);
            email.setText(htmlContent, true);

//            ClassPathResource clr = new ClassPathResource(SHOP_LOGO_IMAGE);
//            email.addInline("springLogo", clr, PNG_MIME);email.addInline("springLogo", clr, PNG_MIME);

            javaMailSender.send(mimeMessage);
            return "Mail sent Successfully";
        } catch (Exception e) {
            return "Error while Sending Mail";
        }
    }

    @Override
    public String sendEmaiForgotPW(EmailDetails details) {
        logger.info("#sendEmaiForgotPW email: {}", details.getRecipient());

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper email = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            String confirmationUrl = "http://localhost:8080/client/client-forgot-password";

            email.setFrom(sender);
            email.setTo(details.getRecipient());
            email.setSubject(StringConstant.TemplateEmail.MAIL_SUBJECT_FORGOT_PASSWORD);

            Context ctx = new Context(LocaleContextHolder.getLocale());
            ctx.setVariable("username", CommonUtils.extractUsernameFromEmail(details.getRecipient()));
            ctx.setVariable("shopLogo", StringConstant.TemplateEmail.SHOP_LOGO_IMAGE);
            ctx.setVariable("url", confirmationUrl);
            String htmlContent = htmlTemplateEngine.process(StringConstant.TemplateEmail.TEMPLATE_EMAIL_FORGOT_PASSWORD, ctx);
            email.setText(htmlContent, true);

            javaMailSender.send(mimeMessage);
            return "Mail sent Successfully";
        } catch (Exception e) {
            return "Error while Sending Mail";
        }
    }

}
