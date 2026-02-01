import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

function DashboardPage({ setIsLoggedIn }) {

    const [message, setMessage] = useState("");
    const navigate = useNavigate();

    useEffect(() => {
        loadDashboard();
    }, []);

    const loadDashboard = async () => {

        const token = localStorage.getItem("token");

        const response = await fetch("http://localhost:8080/api/user/me", {
            headers: {
                Authorization: "Bearer " + token
            }
        });

        if (response.status === 401) {
            localStorage.removeItem("token");
            setIsLoggedIn(false);
            navigate("/login");
            return;
        }

        const text = await response.text();
        setMessage(text);
    };

    const logout = () => {
        localStorage.removeItem("token");

        // 🔥 THIS IS THE KEY LINE
        setIsLoggedIn(false);

        navigate("/login");
    };

    return (
        <div style={{ padding: "40px" }}>
            <h2>Dashboard</h2>
            <p>{message}</p>

            <button onClick={logout}>Logout</button>
        </div>
    );
}

export default DashboardPage;
