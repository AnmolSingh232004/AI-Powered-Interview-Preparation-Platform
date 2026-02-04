import { useState } from "react";
import { useNavigate } from "react-router-dom";
import {
    Box,
    Button,
    Card,
    CardContent,
    Container,
    MenuItem,
    Select,
    TextField,
    Typography,
    Stack,
    Radio,
    RadioGroup,
    FormControlLabel,
} from "@mui/material";

function DashboardPage({ setIsLoggedIn }) {
    const [role, setRole] = useState("Java Backend");
    const [difficulty, setDifficulty] = useState("Intermediate");
    const [count, setCount] = useState(5);

    const [questions, setQuestions] = useState([]);
    const [answers, setAnswers] = useState([]);
    const [currentQuestionIndex, setCurrentQuestionIndex] = useState(0);

    const [loading, setLoading] = useState(false);
    const [result, setResult] = useState(null);

    const navigate = useNavigate();

    const logout = () => {
        localStorage.removeItem("token");
        setIsLoggedIn(false);
        navigate("/login");
    };

    const generateQuestions = async () => {
        setLoading(true);
        setResult(null);

        const token = localStorage.getItem("token");

        const res = await fetch(
            import.meta.env.VITE_API_URL+ "/api/interview/questions",
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: "Bearer " + token,
                },
                body: JSON.stringify({ role, difficulty, count }),
            }
        );

        const data = await res.json();

        setQuestions(data);
        setAnswers(new Array(data.length).fill(-1)); // 🔥 important
        setCurrentQuestionIndex(0);
        setLoading(false);
    };

    const handleOptionSelect = (optionIndex) => {
        setAnswers((prev) => {
            const copy = [...prev];
            copy[currentQuestionIndex] = optionIndex;
            return copy;
        });
    };

    const submitTest = async () => {
        const token = localStorage.getItem("token");

        const res = await fetch(
            import.meta.env.VITE_API_URL + "/api/interview/submit",
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: "Bearer " + token,
                },
                body: JSON.stringify({
                    questions,
                    answers,
                }),
            }
        );

        const data = await res.json();
        setResult(data);
    };

    const currentQuestion = questions[currentQuestionIndex];

    return (
        <Box
            sx={{
                minHeight: "100vh",
                background:
                    "radial-gradient(circle at top, #1e293b, #020617)",
                py: 8,
            }}
        >
            <Container maxWidth="md">
                <Card sx={{ p: 4 }}>
                    <Typography variant="h4" fontWeight="bold">
                        🤖 InterviewAI
                    </Typography>

                    <Typography color="text.secondary" mb={3}>
                        AI-powered interview practice
                    </Typography>

                    {/* CONFIG */}
                    <Stack direction="row" spacing={2} mb={3}>
                        <Select
                            fullWidth
                            value={role}
                            onChange={(e) => setRole(e.target.value)}
                        >
                            <MenuItem value="Java Backend">
                                Java Backend
                            </MenuItem>
                            <MenuItem value="Frontend">Frontend</MenuItem>
                            <MenuItem value="DevOps">DevOps</MenuItem>
                        </Select>

                        <Select
                            fullWidth
                            value={difficulty}
                            onChange={(e) =>
                                setDifficulty(e.target.value)
                            }
                        >
                            <MenuItem value="Beginner">Beginner</MenuItem>
                            <MenuItem value="Intermediate">
                                Intermediate
                            </MenuItem>
                            <MenuItem value="Advanced">Advanced</MenuItem>
                        </Select>

                        <TextField
                            label="Questions"
                            value={count}
                            onChange={(e) =>
                                setCount(Number(e.target.value))
                            }
                            sx={{ width: 120 }}
                        />
                    </Stack>

                    <Button
                        variant="contained"
                        onClick={generateQuestions}
                        disabled={loading}
                    >
                        {loading
                            ? "Generating..."
                            : "⚡ Generate Questions"}
                    </Button>

                    {/* QUESTION VIEW */}
                    {currentQuestion && !result && (
                        <Box mt={4}>
                            <Card>
                                <CardContent>
                                    <Typography fontWeight="bold" mb={1}>
                                        Question {currentQuestionIndex + 1} of{" "}
                                        {questions.length}
                                    </Typography>

                                    <Typography mb={2}>
                                        {currentQuestion.question}
                                    </Typography>

                                    <RadioGroup
                                        value={
                                            answers[currentQuestionIndex]
                                        }
                                        onChange={(e) =>
                                            handleOptionSelect(
                                                Number(e.target.value)
                                            )
                                        }
                                    >
                                        {currentQuestion.options.map(
                                            (opt, idx) => (
                                                <FormControlLabel
                                                    key={idx}
                                                    value={idx}
                                                    control={<Radio />}
                                                    label={opt}
                                                />
                                            )
                                        )}
                                    </RadioGroup>
                                </CardContent>
                            </Card>

                            <Stack
                                direction="row"
                                justifyContent="space-between"
                                mt={2}
                            >
                                <Button
                                    disabled={currentQuestionIndex === 0}
                                    onClick={() =>
                                        setCurrentQuestionIndex(
                                            (prev) => prev - 1
                                        )
                                    }
                                >
                                    Previous
                                </Button>

                                {currentQuestionIndex <
                                questions.length - 1 ? (
                                    <Button
                                        variant="contained"
                                        onClick={() =>
                                            setCurrentQuestionIndex(
                                                (prev) => prev + 1
                                            )
                                        }
                                    >
                                        Next
                                    </Button>
                                ) : (
                                    <Button
                                        variant="contained"
                                        color="success"
                                        onClick={submitTest}
                                    >
                                        Submit Test
                                    </Button>
                                )}
                            </Stack>
                        </Box>
                    )}

                    {/* RESULT VIEW */}
                    {result && (
                        <Box mt={4}>
                            <Typography variant="h5" fontWeight="bold">
                                🎉 Result
                            </Typography>
                            <Typography>
                                Score: {result.correctAnswers} /{" "}
                                {result.totalQuestions}
                            </Typography>
                            <Typography>
                                Percentage:{" "}
                                {result.scorePercentage.toFixed(2)
                                }%
                            </Typography>
                            <Typography mt={2}>
                                {result.aiFeedback}
                            </Typography>
                        </Box>
                    )}

                    <Box mt={4} textAlign="right">
                        <Button color="error" onClick={logout}>
                            Logout
                        </Button>
                    </Box>
                </Card>
            </Container>
        </Box>
    );
}

export default DashboardPage;
