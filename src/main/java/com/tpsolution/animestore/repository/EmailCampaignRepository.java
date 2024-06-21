package com.tpsolution.animestore.repository;

import com.tpsolution.animestore.entity.EmailCampaign;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface EmailCampaignRepository extends JpaRepository<EmailCampaign, Integer> {
    List<EmailCampaign> findByScheduledTimeBeforeAndIsSentFalse(Timestamp currentTime);
}
