import { useEffect, useState } from 'react';
import { getWeeklyActivity } from '../services/api.js';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer, Label } from 'recharts';
import { Box, Typography, Card } from '@mui/material';

const Dashboard = () => {
  const [data, setData] = useState([]);

  useEffect(() => {
    getWeeklyActivity()
      .then(res => {
        const rawData = res.data;
        const dayOrder = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"];

        const aggregated = rawData.reduce((acc, curr) => {
          const day = curr.day.charAt(0).toUpperCase() + curr.day.slice(1).toLowerCase();
          acc[day] = (acc[day] || 0) + curr.caloriesBurned;
          return acc;
        }, {});

        const processed = dayOrder.map(day => ({
          day,
          caloriesBurned: aggregated[day] || 0
        }));

        setData(processed);
      })
      .catch(err => console.error(err));
  }, []);

  return (
    <Box sx={{ p: 4, maxWidth: 900, mx: "auto" }}>
      <Typography variant="h4" gutterBottom align="center">Weekly Activity Dashboard</Typography>
      <Card sx={{ p: 3 }}>
        {data.length > 0 ? (
          <ResponsiveContainer width="100%" height={300}>
            <LineChart data={data} margin={{ top: 20, right: 30, left: 0, bottom: 20 }}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="day">
                <Label value="Day" offset={-10} position="insideBottom" />
              </XAxis>
              <YAxis label={{ value: 'Calories Burned', angle: -90, position: 'insideLeft' }} />
              <Tooltip />
              <Legend verticalAlign="top" height={36}/>
              <Line type="monotone" dataKey="caloriesBurned" stroke="#1976d2" activeDot={{ r: 8 }} />
            </LineChart>
          </ResponsiveContainer>
        ) : <Typography align="center">No activity data available.</Typography>}
      </Card>
    </Box>
  );
}

export default Dashboard;
