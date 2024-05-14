package com.tpsolution.animestore.controller;

import com.tpsolution.animestore.payload.DataResponse;
import com.tpsolution.animestore.service.imp.IImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/admin/images")
public class ImageController {
    @Autowired
    IImageService imageService;

    @PostMapping("/upload-image-product")
    public ResponseEntity<DataResponse> create(@RequestParam(name = "file") MultipartFile[] files) {
        System.out.println("imageUrl: "+ files);
        String fileName ="";
        String imageUrl = "";
        for (MultipartFile file : files) {

            try {

                fileName = imageService.save(file);

                imageUrl = imageService.getImageUrl(fileName);

                System.out.println("imageUrl: "+ imageUrl);

            } catch (Exception e) {
                System.out.println("Exception: "+ e.getMessage());
                e.printStackTrace();
            }
        }
        return ResponseEntity.ok(DataResponse.ok(imageUrl));

    }
}
