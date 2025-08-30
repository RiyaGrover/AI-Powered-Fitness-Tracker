package com.fitness.UserService.repository;

import com.fitness.UserService.model.Habit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HabitRepository extends JpaRepository<Habit, Long> {
    List<Habit> findByUserId(String userId);
    Optional<Habit> findByIdAndUserId(Long id, String userId);
}
