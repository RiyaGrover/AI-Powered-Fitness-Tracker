import { useEffect, useState } from 'react';
import { getWeeklyActivity } from '../services/api.js';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';

const Dashboard = () => {
    const [data, setData] = useState([]);

    useEffect(() => {
        getWeeklyActivity()
            .then(response => {
                const rawData = response.data;

                // 1. Define the correct order of the days for sorting
                const dayOrder = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"];

                // 2. Aggregate the data by day to handle duplicates
                const aggregatedData = rawData.reduce((accumulator, currentItem) => {
                    // Standardize the day name (e.g., "WEDNESDAY" -> "Wednesday")
                    const day = currentItem.day.charAt(0).toUpperCase() + currentItem.day.slice(1).toLowerCase();

                    // Add calories to the existing day's total or initialize it
                    if (accumulator[day]) {
                        accumulator[day] += currentItem.caloriesBurned;
                    } else {
                        accumulator[day] = currentItem.caloriesBurned;
                    }
                    return accumulator;
                }, {});

                // 3. Map the aggregated data back to a sorted array for the chart
                const processedData = dayOrder.map(day => ({
                    day: day,
                    caloriesBurned: aggregatedData[day] || 0 // Default to 0 if no data exists for a day
                }));

                // Set the final, clean data to state
                setData(processedData);
            })
            .catch(error => console.error('Error fetching data:', error));
    }, []); // Empty dependency array ensures this runs only once on mount

    return (
        <div className="container mx-auto p-4">
            <h1 className="text-2xl font-bold mb-4">Weekly Activity Dashboard</h1>
            <div className="bg-white p-4 rounded-lg shadow-md">
                {data.length > 0 ? (
                    <div>
                        <h2 className="text-xl font-semibold mb-2">Weekly Calories Burned</h2>
                        <ResponsiveContainer width="100%" height={300}>
                            <LineChart
                                data={data}
                                margin={{
                                    top: 5,
                                    right: 30,
                                    left: 20,
                                    bottom: 5,
                                }}
                            >
                                <CartesianGrid strokeDasharray="3 3" />
                                <XAxis dataKey="day" />
                                <YAxis />
                                <Tooltip />
                                <Legend />
                                <Line type="monotone" dataKey="caloriesBurned" stroke="#8884d8" activeDot={{ r: 8 }} />
                            </LineChart>
                        </ResponsiveContainer>
                    </div>
                ) : (
                    <p>No activity data available.</p>
                )}
            </div>
        </div>
    );
}

export default Dashboard;