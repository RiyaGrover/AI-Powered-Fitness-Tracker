package com.fitness.UserService.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/users/dashboard")
@AllArgsConstructor
public class DashboardController {

    private final RestTemplate restTemplate;

    @GetMapping("/weekly-activity")
    public Object getWeeklyActivity(@RequestHeader("X-User-ID") String userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-User-ID", userId);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Object> response = restTemplate.exchange(
                "http://ACTIVITYSERVICE/api/activities/weekly-activity",
                HttpMethod.GET,
                entity,
                Object.class
        );

        return response.getBody();
    }
}
