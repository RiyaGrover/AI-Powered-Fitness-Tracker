package com.fitness.UserService.dashboard;
// src/main/java/com/riyalabs/userservice/dashboard/DashboardController.java
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final WebClient webClient;

    public DashboardController(WebClient.Builder webClientBuilder) {
        // This is the URL for your activityservice
        this.webClient = webClientBuilder.baseUrl("http://localhost:8081").build();
    }

    @GetMapping("/weekly-activity")
    public List<WeeklyActivityData> getWeeklyActivityData(@RequestHeader("X-User-ID") String userId) {
        // 1. Fetch user-specific activities by calling the activityservice API.
        // The header with the userId is passed to the activityservice.
        List<Activity> userActivities = webClient.get()
                .uri("/api/activities")
                .header("X-User-ID", userId)
                .retrieve()
                .bodyToFlux(Activity.class)
                .collectList()
                .block();

        // 2. Group the fetched activities by their date and sum the calories burned.
        Map<LocalDate, Integer> dailyCalories = userActivities.stream()
                .collect(Collectors.groupingBy(
                        activity -> activity.date(), // <-- Corrected line
                        Collectors.summingInt(Activity::caloriesBurned)
                ));

        // 3. Convert the grouped data into a list that your frontend chart can use.
        return dailyCalories.entrySet().stream()
                .map(entry -> new WeeklyActivityData(entry.getKey().getDayOfWeek().toString(), entry.getValue()))
                .collect(Collectors.toList());
    }

    // You may need a simple record to represent the data coming from the activityservice
    private record Activity(String activityType, int durationMinutes, int caloriesBurned, LocalDate date) {}
    private record WeeklyActivityData(String day, int caloriesBurned) {}
}