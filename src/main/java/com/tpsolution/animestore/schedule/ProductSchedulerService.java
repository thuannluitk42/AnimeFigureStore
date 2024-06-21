package com.tpsolution.animestore.schedule;

import com.tpsolution.animestore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class ProductSchedulerService {

    @Autowired
    private ProductService productService;

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
}
