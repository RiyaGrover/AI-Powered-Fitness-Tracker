import { Box, Button } from "@mui/material";
import { useContext, useEffect, useState } from "react";
import { AuthContext } from "react-oauth2-code-pkce";
import { useDispatch } from "react-redux";
import { BrowserRouter as Router, Route, Routes, Navigate, Link } from "react-router-dom";
import { setCredentials } from "./store/authSlice";

import ActivityForm from "./components/ActivityForm";
import ActivityList from "./components/ActivityList";
import ActivityDetail from "./components/ActivityDetail";
import Dashboard from "./components/Dashboard";
import HabitDashboard from "./components/HabitDashboard";

const ActivitiesPage = () => (
  <Box component="section" sx={{ p: 2, border: '1px dashed grey' }}>
    <Box sx={{ display: 'flex', gap: 2, mb: 2 }}>
      <Button component={Link} to="/dashboard" variant="contained">Go to Weekly Dashboard</Button>
      <Button component={Link} to="/habits" variant="contained">Go to Habit Tracker</Button>
    </Box>
    <ActivityForm onActivitiesAdded={() => window.location.reload()} />
    <ActivityList />
  </Box>
);

function App() {
  const { token, tokenData, logIn } = useContext(AuthContext);
  const dispatch = useDispatch();
  const [authReady, setAuthReady] = useState(false);

  useEffect(() => {
    if (token) {
      dispatch(setCredentials({ token, user: tokenData }));
      setAuthReady(true);
    }
  }, [token, tokenData, dispatch]);

  if (!token) {
    return (
      <Box sx={{ p: 4 }}>
        <Button variant="contained" color="error" onClick={logIn}>
          Login
        </Button>
      </Box>
    );
  }

  return (
    <Router>
      {/* Navbar */}
      <Box sx={{ p: 2, display: 'flex', gap: 2, backgroundColor: '#f5f5f5', mb: 2 }}>
        <Button component={Link} to="/activities" variant="outlined">Activities</Button>
        <Button component={Link} to="/dashboard" variant="outlined">Weekly Dashboard</Button>
        <Button component={Link} to="/habits" variant="outlined">Habit Tracker</Button>
      </Box>

      {/* Routes */}
      <Routes>
        <Route path="/activities" element={<ActivitiesPage />} />
        <Route path="/activities/:id" element={<ActivityDetail />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/habits" element={<HabitDashboard />} />
        <Route path="/" element={<Navigate to="/activities" replace />} />
      </Routes>
    </Router>
  );
}

export default App;
