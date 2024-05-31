package com.tpsolution.animestore.service.imp;

import com.tpsolution.animestore.dto.EmailDetails;
import jakarta.mail.MessagingException;

public interface EmailServiceImpl {

    String sendEmailNoAttachment(EmailDetails details);
    String sendEmailWithAttachment(EmailDetails details);
    String sendEmailVerifyAccount(EmailDetails details) throws MessagingException;
    String sendEmaiForgotPW(EmailDetails details);
}
