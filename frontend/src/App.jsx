import { Routes, Route, Navigate } from "react-router-dom";
import { useState, useEffect } from "react";
import LoginPage from "./pages/LoginPage";
import DashboardPage from "./pages/DashboardPage";

function App() {

    const [isLoggedIn, setIsLoggedIn] = useState(false);

    // 🔹 Sync state with localStorage ONCE on app load
    useEffect(() => {
        const token = localStorage.getItem("token");
        setIsLoggedIn(!!token);
    }, []);

    return (
        <Routes>

            <Route
                path="/login"
                element={
                    isLoggedIn
                        ? <Navigate to="/dashboard" />
                        : <LoginPage setIsLoggedIn={setIsLoggedIn} />
                }
            />

            <Route
                path="/dashboard"
                element={
                    isLoggedIn
                        ? <DashboardPage setIsLoggedIn={setIsLoggedIn} />
                        : <Navigate to="/login" />
                }
            />

            <Route path="*" element={<Navigate to="/login" />} />

        </Routes>
    );
}

export default App;
