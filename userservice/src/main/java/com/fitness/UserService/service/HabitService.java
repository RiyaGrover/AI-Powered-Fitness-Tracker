package com.fitness.UserService.service;

import com.fitness.UserService.model.Habit;
import com.fitness.UserService.repository.HabitRepository;
import org.springframework.stereotype.Service;
import java.util.List;

import java.util.Optional;

@Service
public class HabitService {

    private final HabitRepository habitRepository;

    public HabitService(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }

    // ✅ Save habit
    public Habit saveHabit(Habit habit) {
        return habitRepository.save(habit);
    }

    // ✅ Get all habits by userId
    public List<Habit> getHabitsByUserId(String userId) {
        return habitRepository.findByUserId(userId);
    }

    // ✅ Get habit by ID
    public Habit getHabitById(Long id) {
        return habitRepository.findById(id).orElse(null);
    }

    // ✅ Delete habit
    public void deleteHabit(Long id) {
        habitRepository.deleteById(id);
    }
    public Habit getHabitByIdAndUserId(Long habitId, String userId) {
        return habitRepository.findByIdAndUserId(habitId, userId)
                .orElseThrow(() -> new RuntimeException("Habit not found or not yours"));
    }

    public void deleteHabitByIdAndUserId(Long habitId, String userId) {
        Habit habit = getHabitByIdAndUserId(habitId, userId);
        habitRepository.delete(habit);
    }
    public Habit toggleCompletion(Long habitId, String userId) {
        Habit habit = getHabitByIdAndUserId(habitId, userId);
        if (habit != null) {
            habit.setCompleted(!habit.getCompleted());
            return saveHabit(habit);
        }
        throw new RuntimeException("Habit not found or not authorized");
    }

}

