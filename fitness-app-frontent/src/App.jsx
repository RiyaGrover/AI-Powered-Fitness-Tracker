import { Box, Button } from "@mui/material";
import { useContext, useEffect, useState } from "react";
import { AuthContext } from "react-oauth2-code-pkce";
import { useDispatch } from "react-redux";
import { BrowserRouter as Router, Route, Routes, Navigate } from "react-router-dom";
import { setCredentials } from "./store/authSlice";
import ActivityForm from "./components/ActivityForm";
import ActivityList from "./components/ActivityList";
import ActivityDetail from "./components/ActivityDetail"; // <-- Make sure this exists

const ActivitiesPage = () => {
  return (
    <Box component="section" sx={{ p: 2, border: '1px dashed grey' }}>

      <ActivityForm onActivitiesAdded={() => window.location.reload()} />
      <ActivityList />
    </Box>
  );
};

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

  return (
    <Router>
      {!token ? (
        <Button variant="contained" color="error" onClick={() => logIn()}>
          Login
        </Button>
      ) : (
        <Routes>
          <Route path="/activities" element={<ActivitiesPage />} />
          <Route path="/activities/:id" element={<ActivityDetail />} />
          <Route path="/" element={<Navigate to="/activities" replace />} />
        </Routes>
      )}
    </Router>
  );
}

export default App;
