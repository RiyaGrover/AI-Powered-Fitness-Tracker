import React, { useState } from "react";
import {
  Box,
  Button,
  Card,
  CardContent,
  Typography,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  TextField,
  Grid
} from "@mui/material";
import { addActivity } from '../services/api';

const ActivityForm = ({ onActivityAdded }) => {
  const [activity, setActivity] = useState({
    type: "RUNNING",
    duration: '',
    caloriesBurned: '',
    additionalMetrics: {}
  });

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await addActivity(activity);
      if (onActivityAdded) onActivityAdded();
      setActivity({ type: "RUNNING", duration: '', caloriesBurned: '', additionalMetrics: {} });
    } catch (error) {
      console.error("Error adding activity:", error);
    }
  };

  return (
    <Card sx={{ maxWidth: 700, mx: "auto", mb: 4, p: 3 }}>
      <Typography variant="h6" gutterBottom>Add New Activity</Typography>
      <Box component="form" onSubmit={handleSubmit}>
        <Grid container spacing={2}>
          <Grid item xs={12} sm={4}>
            <FormControl fullWidth>
              <InputLabel>Activity Type</InputLabel>
              <Select
                value={activity.type}
                label="Activity Type"
                onChange={(e) => setActivity({ ...activity, type: e.target.value })}
              >
                <MenuItem value="RUNNING">Running</MenuItem>
                <MenuItem value="WALKING">Walking</MenuItem>
                <MenuItem value="CYCLING">Cycling</MenuItem>
              </Select>
            </FormControl>
          </Grid>
          <Grid item xs={12} sm={4}>
            <TextField
              fullWidth
              label="Duration (Minutes)"
              type="number"
              value={activity.duration}
              onChange={(e) => setActivity({ ...activity, duration: e.target.value })}
            />
          </Grid>
          <Grid item xs={12} sm={4}>
            <TextField
              fullWidth
              label="Calories Burned"
              type="number"
              value={activity.caloriesBurned}
              onChange={(e) => setActivity({ ...activity, caloriesBurned: e.target.value })}
            />
          </Grid>
          <Grid item xs={12}>
            <Button type="submit" variant="contained" color="primary" fullWidth>Add Activity</Button>
          </Grid>
        </Grid>
      </Box>
    </Card>
  );
};

export default ActivityForm;
