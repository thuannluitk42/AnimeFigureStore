package com.tpsolution.animestore.service;

import com.tpsolution.animestore.entity.BlogPost;
import com.tpsolution.animestore.repository.BlogPostRepository;
import com.tpsolution.animestore.service.imp.BlogPostServiceImp;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class BlogPostService implements BlogPostServiceImp {
    @Autowired
    private BlogPostRepository blogPostRepository;

    @Transactional
    @Override
    public void publishScheduledBlogPosts() {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        List<BlogPost> blogPosts = blogPostRepository.findByScheduledTimeBeforeAndIsPublishedFalse(currentTime);
        blogPosts.forEach(blogPost -> {
            blogPost.setIsPublished(true);
            blogPostRepository.save(blogPost);
        });
    }

}
