package com.fitness.activityservice.service;

import com.fitness.activityservice.dto.ActivityRequest;
import com.fitness.activityservice.dto.ActivityResponse;
import com.fitness.activityservice.model.Activity;
import com.fitness.activityservice.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityService {

    private final ActivityRepository activityRepository;

    private final UserValidationService userValidationService;
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.queue.name}")
    private String queue;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    public ActivityResponse trackActivity(ActivityRequest request) {

        boolean isValidUser = userValidationService.validateUser(request.getUserId());

        if(!isValidUser){
            throw new RuntimeException("Invalid USer: " + request.getUserId());
        }

        // Set startTime to now if it's null in the request
        if (request.getStartTime() == null) {
            request.setStartTime(LocalDateTime.now());
        }

        Activity activity = Activity.builder()
                .userId(request.getUserId())
                .type(request.getType())
                .duration(request.getDuration())
                .caloriesBurned(request.getCaloriesBurned())
                .startTime(request.getStartTime())
                .additionalMetrics(request.getAdditionalMetrics())
                .build();

        Activity savedActivity = activityRepository.save(activity);

        //Publish to rabbitMQ for Ai processing

        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, savedActivity);
        } catch (Exception e) {
            throw new RuntimeException("Failed to publish rabbitMQ");
        }

        return mapToResponse(savedActivity);


    }

    private ActivityResponse mapToResponse(Activity activity){
        ActivityResponse response = new ActivityResponse();
        response.setId(activity.getId());
        response.setUserId(activity.getUserId());
        response.setType(activity.getType());
        response.setDuration(activity.getDuration());
        response.setCaloriesBurned(activity.getCaloriesBurned());
        response.setStartTime(activity.getStartTime());
        response.setAdditionalMetrics(activity.getAdditionalMetrics());
        response.setCreatedAt(activity.getCreatedAt());
        response.setUpdatedAt(activity.getUpdatedAt());
        return response;
    }

    public List<ActivityResponse> getUserActivities(String userId) {
        List<Activity> activities = activityRepository.findByUserId(userId);
        return activities.stream()
                .map(this:: mapToResponse)
                .toList();

    }

    public ActivityResponse getActivityById(String activityId) {
        return activityRepository
                .findById(activityId)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Activity Not found with id " + activityId));
    }

    public List<WeeklyActivity> getWeeklyActivity(String userId) {
        // Business logic to fetch and calculate weekly activity data
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
        LocalDateTime startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).toLocalDate().atStartOfDay();
        LocalDateTime endOfWeek = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).toLocalDate().atTime(23, 59, 59, 999999999);

        // Add logging to see the parameters being used in the query
        log.info("Fetching weekly activity for userId: {}", userId);
        log.info("Start of week: {}", startOfWeek);
        log.info("End of week: {}", endOfWeek);

        List<Activity> weeklyActivities = activityRepository.findByUserIdAndStartTimeBetween(userId, startOfWeek, endOfWeek);

        log.info("Found {} activities for this week.", weeklyActivities.size());

        // Initialize map with all days of the week set to 0 to ensure a full week is returned
        Map<String, Integer> weeklySummary = new LinkedHashMap<>();
        weeklySummary.put("MONDAY", 0);
        weeklySummary.put("TUESDAY", 0);
        weeklySummary.put("WEDNESDAY", 0);
        weeklySummary.put("THURSDAY", 0);
        weeklySummary.put("FRIDAY", 0);
        weeklySummary.put("SATURDAY", 0);
        weeklySummary.put("SUNDAY", 0);

        // Aggregate calories burned by day
        for (Activity activity : weeklyActivities) {
            DayOfWeek day = activity.getStartTime().getDayOfWeek();
            weeklySummary.merge(day.toString(), activity.getCaloriesBurned(), Integer::sum);
        }

        return weeklySummary.entrySet().stream()
                .map(entry -> new WeeklyActivity(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    public record WeeklyActivity(String day, Integer caloriesBurned) {}
}
