package com.tpsolution.animestore.schedule;

import com.tpsolution.animestore.entity.Users;
import com.tpsolution.animestore.repository.UsersRepository;
import com.tpsolution.animestore.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BirthdayEmailService {

    @Autowired
    private UsersRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void sendBirthdayEmails() {
        LocalDate today = LocalDate.now();
        List<Users> birthdayUsers = userRepository.findAllByDobMonthAndDobDay(today.getMonthValue(), today.getDayOfMonth());
        for (Users user : birthdayUsers) {
            emailService.sendBirthdayEmail(user);
        }
    }
}
