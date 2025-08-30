import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router';
import { getActivities } from '../services/api';
import { Card, CardContent, Grid, Typography, Box } from '@mui/material';

const ActivityList = () => {
  const [activities, setActivities] = useState([]);
  const navigate = useNavigate();

  const fetchActivities = async () => {
    try {
      const response = await getActivities();
      setActivities(response.data);
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    fetchActivities();
  }, []);

  if (activities.length === 0) {
    return <Typography align="center" sx={{ mt: 4 }}>No activities found.</Typography>;
  }

  return (
    <Box sx={{ maxWidth: 900, mx: "auto", mt: 3 }}>
      <Typography variant="h5" gutterBottom>All Activities</Typography>
      <Grid container spacing={2}>
        {activities.map((activity) => (
          <Grid item xs={12} sm={6} md={4} key={activity.id}>
            <Card
              sx={{
                cursor: 'pointer',
                height: '100%',
                display: 'flex',
                flexDirection: 'column',
                justifyContent: 'space-between',
                transition: 'transform 0.2s',
                '&:hover': { transform: 'scale(1.02)', boxShadow: 6 }
              }}
              onClick={() => navigate(`/activities/${activity.id}`)}
            >
              <CardContent>
                <Typography variant="h6" gutterBottom>{activity.type}</Typography>
                <Typography>Duration: {activity.duration} min</Typography>
                <Typography>Calories: {activity.caloriesBurned}</Typography>
                <Typography variant="caption" color="text.secondary">
                  {new Date(activity.createdAt).toLocaleDateString()}
                </Typography>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>
    </Box>
  );
};

export default ActivityList;
