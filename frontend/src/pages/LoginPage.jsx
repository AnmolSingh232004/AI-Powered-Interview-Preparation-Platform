import { useState } from "react";
import { useNavigate } from "react-router-dom";

function LoginPage({ setIsLoggedIn }) {

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const navigate = useNavigate();

    const handleLogin = async () => {

        const response = await fetch("http://localhost:8080/api/auth/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email, password })
        });

        if (!response.ok) {
            alert("Invalid credentials");
            return;
        }

        const data = await response.json();
        localStorage.setItem("token", data.token);

        // 🔥 THIS IS THE KEY LINE
        setIsLoggedIn(true);

        navigate("/dashboard");
    };

    return (
        <div style={{ padding: "40px" }}>
            <h2>Login</h2>

            <input
                placeholder="Email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
            />
            <br /><br />

            <input
                type="password"
                placeholder="Password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
            />
            <br /><br />

            <button onClick={handleLogin}>Login</button>
        </div>
    );
}

export default LoginPage;
