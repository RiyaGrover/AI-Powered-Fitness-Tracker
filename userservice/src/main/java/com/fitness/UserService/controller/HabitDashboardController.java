package com.fitness.UserService.controller;

import com.fitness.UserService.dto.HabitDashboardDTO;
import com.fitness.UserService.service.HabitDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/habits/dashboard")
@RequiredArgsConstructor
public class HabitDashboardController {

    private final HabitDashboardService dashboardService;

    @GetMapping
    public HabitDashboardDTO getDashboard(
            Authentication authentication,
            @RequestHeader(value = "X-User-ID", required = false) String userIdFromHeader
    ) {
        // ✅ 1. Prefer header if present (useful for testing or dashboard aggregation calls)
        if (userIdFromHeader != null && !userIdFromHeader.isBlank()) {
            return dashboardService.getDashboardForUser(userIdFromHeader);
        }

        // ✅ 2. Otherwise extract userId from JWT
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String userId = jwt.getSubject();

        return dashboardService.getDashboardForUser(userId);
    }
}
