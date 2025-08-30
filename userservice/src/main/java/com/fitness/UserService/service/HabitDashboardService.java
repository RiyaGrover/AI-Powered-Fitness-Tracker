package com.fitness.UserService.service;


import com.fitness.UserService.dto.HabitDashboardDTO;
import com.fitness.UserService.model.Habit;
import com.fitness.UserService.repository.HabitRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HabitDashboardService {

    private final HabitRepository habitRepository;

    public HabitDashboardService(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }

    public HabitDashboardDTO getDashboardForUser(String userId) {
        List<Habit> habits = habitRepository.findByUserId(userId);

        long total = habits.size();
        long completed = habits.stream().filter(Habit::getCompleted).count();
        long active = habits.stream().filter(h -> !h.getCompleted()).count();

        double completionRate = total > 0 ? (completed * 100.0) / total : 0;

        HabitDashboardDTO dto = new HabitDashboardDTO();
        dto.setTotalHabits(total);
        dto.setCompletedHabits(completed);
        dto.setActiveHabits(active);
        dto.setCompletionRate(completionRate);

        return dto;
    }
}
