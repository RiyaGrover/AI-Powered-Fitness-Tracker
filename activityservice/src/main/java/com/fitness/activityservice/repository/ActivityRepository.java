package com.fitness.activityservice.repository;

import com.fitness.activityservice.dto.ActivityResponse;
import com.fitness.activityservice.model.Activity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface ActivityRepository extends MongoRepository<Activity, String> {

    List<Activity> findByUserId(String userId);

    List<Activity> findByUserIdAndStartTimeBetween(String userId, LocalDateTime startOfWeek, LocalDateTime endOfWeek);
}
