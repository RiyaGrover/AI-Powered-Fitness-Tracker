import React, { useState, useEffect } from 'react';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';

const Dashboard = () => {
    const [data, setData] = useState([]);

    useEffect(() => {
        // We will add the data fetching logic here in a later step
        // For now, let's use some placeholder data to see the chart
        const placeholderData = [
            { day: 'Mon', caloriesBurned: 400 },
            { day: 'Tue', caloriesBurned: 300 },
            { day: 'Wed', caloriesBurned: 600 },
            { day: 'Thu', caloriesBurned: 500 },
            { day: 'Fri', caloriesBurned: 750 },
            { day: 'Sat', caloriesBurned: 900 },
            { day: 'Sun', caloriesBurned: 800 },
        ];
        setData(placeholderData);
    }, []);

    return (
        <div style={{ width: '100%', height: 300 }}>
            <h2>Weekly Activity Dashboard</h2>
            <ResponsiveContainer>
                <LineChart data={data} margin={{ top: 5, right: 30, left: 20, bottom: 5 }}>
                    <CartesianGrid strokeDasharray="3 3" />
                    <XAxis dataKey="day" />
                    <YAxis />
                    <Tooltip />
                    <Line type="monotone" dataKey="caloriesBurned" stroke="#8884d8" activeDot={{ r: 8 }} />
                </LineChart>
            </ResponsiveContainer>
        </div>
    );
};

export default Dashboard;