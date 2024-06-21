package com.tpsolution.animestore.schedule;

import com.tpsolution.animestore.entity.Users;
import com.tpsolution.animestore.entity.Voucher;
import com.tpsolution.animestore.repository.UsersRepository;
import com.tpsolution.animestore.repository.VoucherRepository;
import com.tpsolution.animestore.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
public class ScheduledTasks {

    @Autowired
    private UsersRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private BlogPostService blogPostService;

    @Autowired
    private EmailCampaignService emailCampaignService;

    @Autowired
    private VoucherRepository voucherRepository;

    // List of special days when vouchers should be activated or deactivated
    private static final List<LocalDate> SPECIAL_DAYS = List.of(
            LocalDate.of(LocalDate.now().getYear(), 1, 1),  // New Year's Day
            LocalDate.of(LocalDate.now().getYear(), 12, 25)  // Christmas Day
            // Add more special days as needed
    );

//    @Autowired
//    private SocialMediaPostService socialMediaPostService;

    @Autowired
    private ProductService productService;

    @Scheduled(cron = "0 0 * * * ?") // Every hour
    public void publishBlogPosts() {
        blogPostService.publishScheduledBlogPosts();
    }

    @Scheduled(cron = "0 0 * * * ?") // Every hour
    public void sendEmailCampaigns() {
        emailCampaignService.sendScheduledEmailCampaigns();
    }

//    @Scheduled(cron = "0 0 * * * ?") // Every hour
//    public void postSocialMediaPosts() {
//        socialMediaPostService.postScheduledSocialMediaPosts();
//    }

    //TODO: phase 2
//    @Scheduled(cron = "0 0 2 * * ?") // Every day at 2 AM
//    public void uploadProducts() {
//        // Example: Upload new products
//        Product newProduct = new Product();
//        newProduct.setProductName("New Product");
//        newProduct.setProductPrice(10000);
//        newProduct.setProductImages("image1.jpg,image2.jpg");
//        newProduct.setProductQuantity(100);
//        newProduct.setProductDescription("Description of new product");
//        newProduct.setDiscount("10%");
//        newProduct.setCategoryId(1);
//        newProduct.setUrlImage("image1.jpg");
//
//        productService.uploadNewProducts(Arrays.asList(newProduct));
//    }

    @Scheduled(cron = "0 0 9 * * ?") // Every day at 9 AM
    public void startFlashSale() {
        // Example: Apply flash sale to products with IDs 1, 2, 3
        productService.applyFlashSale(Arrays.asList(1, 2, 3), 20);
    }

    @Scheduled(cron = "0 0 21 * * ?") // Every day at 9 PM
    public void endFlashSale() {
        // Example: Revert flash sale for products with IDs 1, 2, 3
        productService.revertFlashSale(Arrays.asList(1, 2, 3), 20);
    }

    @Scheduled(cron = "0 0 0 * * ?") // Every day at midnight
    public void deactivateOutOfStockProducts() {
        productService.deactivateOutOfStockProducts();
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void sendBirthdayEmails() {
        LocalDate today = LocalDate.now();
        List<Users> birthdayUsers = userRepository.findAllByDobMonthAndDobDay(today.getMonthValue(), today.getDayOfMonth());
        for (Users user : birthdayUsers) {
            emailService.sendBirthdayEmail(user);
        }
    }

    @Scheduled(cron = "0 0 0 * * *")  // This cron expression means midnight every day
    @Transactional
    public void manageVouchersOnSpecialDays() {
        LocalDate today = LocalDate.now();

        List<Voucher> vouchers = (List<Voucher>) voucherRepository.findAll();

        for (Voucher voucher : vouchers) {
            if (SPECIAL_DAYS.contains(today)) {
                if (!voucher.isActive() && voucher.getExpiryDate().isAfter(today)) {
                    voucher.setActive(true);
                } else if (voucher.isActive() && (voucher.getUsageCount() >= voucher.getMaxUsage() || voucher.getExpiryDate().isBefore(today))) {
                    voucher.setActive(false);
                }
            } else if (voucher.getUsageCount() >= voucher.getMaxUsage() || voucher.getExpiryDate().isBefore(today)) {
                voucher.setActive(false);
            }
            voucherRepository.save(voucher);
        }
    }
}
