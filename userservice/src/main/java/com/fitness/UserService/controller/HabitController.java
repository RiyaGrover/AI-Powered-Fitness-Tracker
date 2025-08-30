package com.fitness.UserService.controller;

import com.fitness.UserService.model.Habit;
import com.fitness.UserService.service.HabitService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/habits")
public class HabitController {

    private final HabitService habitService;

    public HabitController(HabitService habitService) {
        this.habitService = habitService;
    }

    // ✅ Create Habit (attach userId from X-User-ID header)
    @PostMapping
    public Habit createHabit(
            @RequestBody Habit habit,
            @RequestHeader("X-User-ID") String userId
    ) {
        habit.setUserId(userId);
        return habitService.saveHabit(habit);
    }

    // ✅ Get all habits for logged-in user (from header)
    @GetMapping
    public List<Habit> getUserHabits(
            @RequestHeader("X-User-ID") String userId
    ) {
        return habitService.getHabitsByUserId(userId);
    }

    // ✅ Get single habit by habitId (only if it belongs to logged-in user)
    @GetMapping("/{habitId}")
    public Habit getHabitById(
            @PathVariable Long habitId,
            @RequestHeader("X-User-ID") String userId
    ) {
        return habitService.getHabitByIdAndUserId(habitId, userId);
    }

    // ✅ Delete habit (only if it belongs to logged-in user)
    @DeleteMapping("/{habitId}")
    public String deleteHabit(
            @PathVariable Long habitId,
            @RequestHeader("X-User-ID") String userId
    ) {
        habitService.deleteHabitByIdAndUserId(habitId, userId);
        return "Habit deleted with id: " + habitId;
    }

    @PatchMapping("/{habitId}/toggle")
    public Habit toggleHabitCompletion(
            @PathVariable Long habitId,
            @RequestHeader("X-User-ID") String userId) {
        return habitService.toggleCompletion(habitId, userId);
    }

}
