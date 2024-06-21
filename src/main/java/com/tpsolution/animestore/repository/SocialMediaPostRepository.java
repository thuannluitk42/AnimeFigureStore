package com.tpsolution.animestore.repository;

import com.tpsolution.animestore.entity.SocialMediaPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface SocialMediaPostRepository extends JpaRepository<SocialMediaPost, Integer> {
    List<SocialMediaPost> findByScheduledTimeBeforeAndIsPostedFalse(Timestamp currentTime);
}
