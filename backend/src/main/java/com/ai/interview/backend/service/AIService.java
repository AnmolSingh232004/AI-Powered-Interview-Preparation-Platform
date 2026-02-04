package com.ai.interview.backend.service;

import com.ai.interview.backend.DTO.McqQuestionDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AIService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${ai.api.url}")
    private String apiUrl;

    @Value("${ai.api.key}")
    private String apiKey;

    /* ===================== MCQ GENERATION ===================== */

    public List<McqQuestionDto> generateMcqQuestionsFromAI(
            String role,
            String difficulty,
            int count,
            List<String> topics
    ) {

        String prompt = buildMcqPrompt(role, difficulty, count, topics);
        String rawResponse = callAI(prompt);
        return parseAndValidateMcqs(rawResponse);
    }

    /* ===================== AI FEEDBACK ===================== */

    public String generateFeedbackFromAI(
            String role,
            String difficulty,
            List<McqQuestionDto> questions,
            List<Integer> userAnswers,
            int score,
            int totalQuestions
    ) {

        StringBuilder sb = new StringBuilder();

        sb.append("""
        You are a senior technical interview coach.

        CONTEXT:
        Role: %s
        Difficulty: %s
        Score: %d / %d

        User answers are index-based (0–3).
        -1 means skipped.

        QUESTIONS & ANSWERS:
        """.formatted(role, difficulty, score, totalQuestions));

        for (int i = 0; i < questions.size(); i++) {
            sb.append("""
            Q%d: %s
            User Answer Index: %d

            """.formatted(
                    i + 1,
                    questions.get(i).getQuestion(),
                    userAnswers.get(i)
            ));
        }

        sb.append("""
        TASK:
        Provide structured interview feedback.

        RULES:
        - No correct answers
        - No AI mention
        - Max 250 words
        - Clear headings & bullet points

        FORMAT:

        ## Overall Performance
        ## Strengths
        ## Areas to Improve
        ## Skipped Questions
        ## Actionable Suggestions
        """);

        return callAI(sb.toString());
    }


    /* ===================== SHARED AI CALL ===================== */

    private String callAI(String prompt) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> requestBody = Map.of(
                "model", "llama-3.1-8b-instant",
                "messages", List.of(
                        Map.of("role", "user", "content", prompt)
                )
        );

        HttpEntity<Map<String, Object>> entity =
                new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                apiUrl,
                entity,
                Map.class
        );

        Map<String, Object> choice =
                (Map<String, Object>) ((List<?>) response.getBody().get("choices")).get(0);

        Map<String, Object> message =
                (Map<String, Object>) choice.get("message");

        return message.get("content").toString();
    }

    /* ===================== PARSING ===================== */

    private List<McqQuestionDto> parseAndValidateMcqs(String rawJson) {

        try {
            List<McqQuestionDto> questions =
                    objectMapper.readValue(
                            rawJson,
                            new TypeReference<List<McqQuestionDto>>() {}
                    );

            for (McqQuestionDto q : questions) {
                if (q.getOptions() == null || q.getOptions().size() != 4) {
                    throw new IllegalStateException("Each question must have exactly 4 options");
                }
                if (q.getCorrectOptionIndex() < 0 || q.getCorrectOptionIndex() > 3) {
                    throw new IllegalStateException("Correct option index must be 0-3");
                }
            }

            return questions;

        } catch (Exception e) {
            throw new IllegalStateException(
                    "Invalid AI MCQ JSON:\n" + rawJson,
                    e
            );
        }
    }

    /* ===================== PROMPTS ===================== */

    private String buildMcqPrompt(
            String role,
            String difficulty,
            int count,
            List<String> topics
    ) {

        return String.format("""
        You are a senior technical interviewer.

        ROLE: %s
        DIFFICULTY: %s
        TOPICS: %s

        TASK:
        Generate exactly %d multiple-choice interview questions.

        RULES:
        - Return ONLY valid JSON
        - No markdown, no commentary
        - EXACTLY 4 options per question
        - correctOptionIndex must be 0–3

        FORMAT:
        [
          {
            "question": "Question text",
            "options": ["A","B","C","D"],
            "correctOptionIndex": 0
          }
        ]
        """,
                role,
                difficulty,
                String.join(", ", topics),
                count
        );
    }

    public List<String> generateTopicsForRole(String role) {

        String prompt = String.format("""
        You are a senior technical interviewer.

        TASK:
        Generate a concise list of 5–7 core technical topics
        that should be evaluated for a %s role.

        RULES:
        - Return ONLY valid JSON
        - No explanations
        - No markdown

        FORMAT:
        ["Topic 1", "Topic 2", "Topic 3"]
        """, role);

        String raw = callAI(prompt);

        try {
            return objectMapper.readValue(
                    raw,
                    new TypeReference<List<String>>() {}
            );
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Invalid topic list returned by AI:\n" + raw,
                    e
            );
        }
    }


}
