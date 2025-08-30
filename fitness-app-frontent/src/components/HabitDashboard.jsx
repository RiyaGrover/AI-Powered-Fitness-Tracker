import React, { useState, useEffect } from "react";
import {
  Box,
  Button,
  Card,
  CardContent,
  Typography,
  TextField,
  MenuItem,
  Grid,
  Divider
} from "@mui/material";
import { getHabits, addHabit, deleteHabitById } from "../services/habitApi.js";

const HabitDashboard = () => {
  const [habits, setHabits] = useState([]);
  const [name, setName] = useState("");
  const [frequency, setFrequency] = useState("Daily");
  const [stats, setStats] = useState({
    totalHabits: 0,
    activeHabits: 0,
    completedHabits: 0,
    completionRate: 0,
  });

  const calculateStats = (habitList) => {
    const total = habitList.length;
    const completed = habitList.filter(h => h.completed).length;
    const active = total - completed;
    const rate = total === 0 ? 0 : Math.round((completed / total) * 100);
    setStats({
      totalHabits: total,
      activeHabits: active,
      completedHabits: completed,
      completionRate: rate,
    });
  };

  const fetchHabits = async () => {
    try {
      const res = await getHabits();
      const habitsData = res.data.map(h => ({ ...h, completed: false }));
      setHabits(habitsData);
      calculateStats(habitsData);
    } catch (err) {
      console.error(err);
    }
  };

  useEffect(() => {
    fetchHabits();
  }, []);

  const handleAddHabit = async () => {
    if (!name) return;
    try {
      const res = await addHabit({ name, frequency });
      const newHabit = { ...res.data, completed: false };
      const updatedHabits = [...habits, newHabit];
      setHabits(updatedHabits);
      calculateStats(updatedHabits);
      setName("");
      setFrequency("Daily");
    } catch (err) {
      console.error(err);
    }
  };

  const handleCompleteHabit = async (habitId) => {
    try {
      await deleteHabitById(habitId); // remove habit on completion
      const updatedHabits = habits.filter(h => h.id !== habitId);
      setHabits(updatedHabits);
      calculateStats(updatedHabits);
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <Box sx={{ p: 4 }}>
      <Typography variant="h4" gutterBottom align="center">Habit Tracker</Typography>

      {/* Add Habit Form */}
      <Card sx={{ mb: 4, p: 3, maxWidth: 700, mx: "auto" }}>
        <Typography variant="h6" gutterBottom>Add New Habit</Typography>
        <Grid container spacing={2}>
          <Grid item xs={12} sm={6}>
            <TextField
              label="Habit Name"
              value={name}
              onChange={(e) => setName(e.target.value)}
              fullWidth
            />
          </Grid>
          <Grid item xs={12} sm={4}>
            <TextField
              select
              label="Frequency"
              value={frequency}
              onChange={(e) => setFrequency(e.target.value)}
              fullWidth
            >
              <MenuItem value="Daily">Daily</MenuItem>
              <MenuItem value="Weekly">Weekly</MenuItem>
            </TextField>
          </Grid>
          <Grid item xs={12} sm={2}>
            <Button variant="contained" color="primary" onClick={handleAddHabit} fullWidth>Add</Button>
          </Grid>
        </Grid>
      </Card>

      {/* Stats Cards */}
      <Grid container spacing={2} sx={{ mb: 4, maxWidth: 900, mx: "auto" }}>
        {Object.entries(stats).map(([key, value]) => (
          <Grid item xs={6} sm={3} key={key}>
            <Card sx={{ p: 2, textAlign: "center" }}>
              <Typography sx={{ textTransform: "capitalize" }}>{key.replace(/([A-Z])/g, ' $1')}</Typography>
              <Typography variant="h6">{key === "completionRate" ? `${value}%` : value}</Typography>
            </Card>
          </Grid>
        ))}
      </Grid>

      {/* Habit List */}
      <Card sx={{ maxWidth: 700, mx: "auto", p: 2 }}>
        <Typography variant="h6" gutterBottom>All Habits</Typography>
        <Divider sx={{ mb: 2 }} />
        {habits.length === 0 ? (
          <Typography align="center">No habits found.</Typography>
        ) : (
          <Grid container spacing={2}>
            {habits.map(habit => (
              <Grid item xs={12} key={habit.id}>
                <Card sx={{ p: 2, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                  <Typography>{habit.name} - {habit.frequency}</Typography>
                  <Button variant="contained" color="success" onClick={() => handleCompleteHabit(habit.id)}>Complete</Button>
                </Card>
              </Grid>
            ))}
          </Grid>
        )}
      </Card>
    </Box>
  );
};

export default HabitDashboard;
