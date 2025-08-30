import axios from "axios";

const API_URL = '/api/users';

const api = axios.create({
  baseURL: API_URL,
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  const userId = localStorage.getItem('userId');
  if (token) {
    config.headers['Authorization'] = `Bearer ${token}`;
  }
  if (userId) {
    config.headers['X-User-ID'] = userId;
  }
  return config;
});

// Habits
export const getHabits = () => api.get('/habits');
export const addHabit = (habit) => api.post('/habits', habit);
export const deleteHabitById = (habitId) => api.delete(`/habits/${habitId}`);

// Dashboard stats
export const getDashboardHabits = () => api.get('/habits/dashboard');
