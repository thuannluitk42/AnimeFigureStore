package com.tpsolution.animestore.service;

import com.tpsolution.animestore.entity.EmailCampaign;
import com.tpsolution.animestore.repository.EmailCampaignRepository;
import com.tpsolution.animestore.service.imp.EmailCampaignServiceImp;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class EmailCampaignService implements EmailCampaignServiceImp {

    @Autowired
    private EmailCampaignRepository emailCampaignRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Override
    @Transactional
    public void sendScheduledEmailCampaigns() {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        List<EmailCampaign> emailCampaigns = emailCampaignRepository.findByScheduledTimeBeforeAndIsSentFalse(currentTime);
        emailCampaigns.forEach(emailCampaign -> {
            sendEmail(emailCampaign);
            emailCampaign.setIsSent(true);
            emailCampaignRepository.save(emailCampaign);
        });
    }

    private void sendEmail(EmailCampaign emailCampaign) {
        try {
            // Creating a simple mail message
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            // Setting up necessary details
            mailMessage.setFrom(sender);
            // send to multiple use
            // message.setTo(new String[] {"recipient1@example.com", "recipient2@example.com", "recipient3@example.com"});
            mailMessage.setTo(emailCampaign.getRecipient());
            mailMessage.setText(emailCampaign.getBody());
            mailMessage.setSubject(emailCampaign.getSubject());

            // Sending the mail
            javaMailSender.send(mailMessage);

        } catch (Exception e) {
        }
    }
}
