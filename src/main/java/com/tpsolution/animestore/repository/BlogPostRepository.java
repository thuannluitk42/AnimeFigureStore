package com.tpsolution.animestore.repository;

import com.tpsolution.animestore.entity.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface BlogPostRepository extends JpaRepository<BlogPost, Integer> {
    List<BlogPost> findByScheduledTimeBeforeAndIsPublishedFalse(Timestamp currentTime);
}
