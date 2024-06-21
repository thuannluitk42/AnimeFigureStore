package com.tpsolution.animestore.service;

import com.tpsolution.animestore.entity.SocialMediaPost;
import com.tpsolution.animestore.repository.SocialMediaPostRepository;
import com.tpsolution.animestore.service.imp.SocialMediaPostServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

public class SocialMediaPostService implements SocialMediaPostServiceImp {

    @Autowired
    private SocialMediaPostRepository socialMediaPostRepository;

    //todo
//    @Autowired
//    private SocialMediaApiClient socialMediaApiClient; // Hypothetical API client

    @Transactional
    @Override
    public void postScheduledSocialMediaPosts() {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        List<SocialMediaPost> socialMediaPosts = socialMediaPostRepository.findByScheduledTimeBeforeAndIsPostedFalse(currentTime);
        socialMediaPosts.forEach(socialMediaPost -> {
            postToSocialMedia(socialMediaPost);
            socialMediaPost.setIsPosted(true);
            socialMediaPostRepository.save(socialMediaPost);
        });
    }

    private void postToSocialMedia(SocialMediaPost socialMediaPost) {
        //TODO
        //socialMediaApiClient.post(socialMediaPost.getPlatform(), socialMediaPost.getContent());
    }

}
