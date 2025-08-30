import axios from "axios";
import { use } from "react";


const API_URL ='/api/';

const api = axios.create({
    baseURL: API_URL
});

api.interceptors.request.use((config) => {
    const userId = localStorage.getItem('userId');
    const token = localStorage.getItem('token');
    if(token){
        config.headers['Authorization'] = `Bearer ${token}`;
    }
    if(userId) {
        config.headers['X-User-ID'] = userId;
    }
return config;
});

export const getActivities= () => api.get('/activities');
export const addActivity = (activity) => api.post('/activities', activity);
export const getActivityDetail = (id) => api.get(`/recommendations/activity/${id}`);
export const getWeeklyActivity = () => api.get('/users/dashboard/weekly-activity');
