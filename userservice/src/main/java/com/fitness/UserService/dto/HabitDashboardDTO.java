package com.fitness.UserService.dto;

public class HabitDashboardDTO {
    private long totalHabits;
    private long completedHabits;
    private long activeHabits;
    private double completionRate; // %

    // Getters & Setters
    public long getTotalHabits() {
        return totalHabits;
    }
    public void setTotalHabits(long totalHabits) {
        this.totalHabits = totalHabits;
    }

    public long getCompletedHabits() {
        return completedHabits;
    }
    public void setCompletedHabits(long completedHabits) {
        this.completedHabits = completedHabits;
    }

    public long getActiveHabits() {
        return activeHabits;
    }
    public void setActiveHabits(long activeHabits) {
        this.activeHabits = activeHabits;
    }

    public double getCompletionRate() {
        return completionRate;
    }
    public void setCompletionRate(double completionRate) {
        this.completionRate = completionRate;
    }
}
