package com.tpsolution.animestore.controller;

import com.tpsolution.animestore.dto.EmailDetails;
import com.tpsolution.animestore.service.imp.EmailServiceImpl;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
public class EmailController {
    @Autowired
    private EmailServiceImpl emailService;

    @PostMapping("/sendMail")
    public String sendMail(@RequestBody EmailDetails details) {
        String status = emailService.sendEmailNoAttachment(details);
        return status;
    }

    // Sending email with attachment
    @PostMapping("/sendMailWithAttachment")
    public String sendMailWithAttachment(@RequestBody EmailDetails details) {
        String status = emailService.sendEmailWithAttachment(details);
        return status;
    }

    @PostMapping("/sendHtmlEmailUseGG")
    public String sendHtmlEmailUseGG(@RequestBody EmailDetails details) throws MessagingException {
        String status = emailService.sendHtmlEmail(details);
        return status;
    }
}
