import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router';
import { getActivityDetail } from '../services/api';
import { Box, Card, CardContent, Divider, Typography, Grid } from '@mui/material';

const ActivityDetail = () => {
  const { id } = useParams();
  const [activity, setActivity] = useState(null);

  useEffect(() => {
    const fetchActivityDetail = async () => {
      try {
        const response = await getActivityDetail(id);
        setActivity(response.data);
      } catch (error) {
        console.error(error);
      }
    };
    fetchActivityDetail();
  }, [id]);

  if (!activity) return <Typography align="center" sx={{ mt: 4 }}>Loading...</Typography>;

  return (
    <Box sx={{ maxWidth: 800, mx: 'auto', p: 3 }}>
      <Typography variant="h4" gutterBottom align="center">Activity Details</Typography>

      {/* Activity Info */}
      <Card sx={{ mb: 3 }}>
        <CardContent>
          <Typography variant="h6">Basic Info</Typography>
          <Divider sx={{ my: 1 }} />
          <Grid container spacing={1}>
            <Grid item xs={6}><Typography>Type:</Typography></Grid>
            <Grid item xs={6}><Typography>{activity.type}</Typography></Grid>
            <Grid item xs={6}><Typography>Duration:</Typography></Grid>
            <Grid item xs={6}><Typography>{activity.duration} min</Typography></Grid>
            <Grid item xs={6}><Typography>Calories Burned:</Typography></Grid>
            <Grid item xs={6}><Typography>{activity.caloriesBurned}</Typography></Grid>
            <Grid item xs={6}><Typography>Date:</Typography></Grid>
            <Grid item xs={6}><Typography>{new Date(activity.createdAt).toLocaleString()}</Typography></Grid>
          </Grid>
        </CardContent>
      </Card>

      {/* AI Recommendations */}
      {activity.recommendation && (
        <Card sx={{ mb: 3 }}>
          <CardContent>
            <Typography variant="h6" gutterBottom>AI Recommendations</Typography>
            <Divider sx={{ mb: 2 }} />
            {activity.recommendation && (
              <Box>
                <Typography variant="subtitle1">Analysis:</Typography>
                <Typography paragraph>{activity.recommendation}</Typography>
              </Box>
            )}
            {activity.improvements?.length > 0 && (
              <Box>
                <Typography variant="subtitle1">Improvements:</Typography>
                {activity.improvements.map((imp, idx) => (
                  <Typography key={idx} paragraph>• {imp}</Typography>
                ))}
              </Box>
            )}
            {activity.suggestions?.length > 0 && (
              <Box>
                <Typography variant="subtitle1">Suggestions:</Typography>
                {activity.suggestions.map((sug, idx) => (
                  <Typography key={idx} paragraph>• {sug}</Typography>
                ))}
              </Box>
            )}
            {activity.safety?.length > 0 && (
              <Box>
                <Typography variant="subtitle1">Safety Guidelines:</Typography>
                {activity.safety.map((safe, idx) => (
                  <Typography key={idx} paragraph>• {safe}</Typography>
                ))}
              </Box>
            )}
          </CardContent>
        </Card>
      )}
    </Box>
  );
};

export default ActivityDetail;
