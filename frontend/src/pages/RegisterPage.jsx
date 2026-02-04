import { useState } from "react";
import { useNavigate } from "react-router-dom";
import {
    Box,
    Button,
    Card,
    CardContent,
    Container,
    TextField,
    Typography,
    MenuItem,
    Select
} from "@mui/material";

function RegisterPage() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [role, setRole] = useState("USER");
    const [error, setError] = useState("");

    const navigate = useNavigate();

    const register = async () => {
        setError("");

        try {
            const response = await fetch(import.meta.env.VITE_API_URL + "/api/auth/register", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({
                    email,
                    password,
                    role
                }),
            });
            const data = await response.json();

            if (!response.ok) {
                setError(data.message || "Register failed.");
                return;
            }

            // After successful register → go to login
            navigate("/login");

        } catch (err) {
            setError("User already exists or invalid data");
        }
    };

    return (
        <Box
            sx={{
                minHeight: "100vh",
                display: "flex",
                alignItems: "center",
                background:
                    "radial-gradient(circle at top, #1e293b, #020617)",
            }}
        >
            <Container maxWidth="sm">
                <Card sx={{ p: 4 }}>
                    <CardContent>

                        <Typography variant="h4" align="center" gutterBottom>
                            🤖 InterviewAI
                        </Typography>

                        <Typography
                            align="center"
                            color="text.secondary"
                            mb={4}
                        >
                            Create your account
                        </Typography>

                        <TextField
                            fullWidth
                            label="Email"
                            margin="normal"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                        />

                        <TextField
                            fullWidth
                            type="password"
                            label="Password"
                            margin="normal"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                        />

                        <Select
                            fullWidth
                            value={role}
                            onChange={(e) => setRole(e.target.value)}
                            sx={{ mt: 2 }}
                        >
                            <MenuItem value="USER">User</MenuItem>
                            <MenuItem value="ADMIN">Admin</MenuItem>
                        </Select>

                        {error && (
                            <Typography color="error" mt={2}>
                                {error}
                            </Typography>
                        )}

                        <Button
                            fullWidth
                            variant="contained"
                            size="large"
                            sx={{ mt: 4 }}
                            onClick={register}
                        >
                            Register
                        </Button>

                        <Button
                            fullWidth
                            sx={{ mt: 2 }}
                            onClick={() => navigate("/login")}
                        >
                            Already have an account? Login
                        </Button>

                    </CardContent>
                </Card>
            </Container>
        </Box>
    );
}

export default RegisterPage;
